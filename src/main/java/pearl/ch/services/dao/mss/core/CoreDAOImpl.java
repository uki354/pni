package pearl.ch.services.dao.mss.core;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.config.constants.ServerCname;
import pearl.ch.services.config.constants.ShopURL;
import pearl.ch.services.config.constants.UnsubscribeEncryptionConfig;
import pearl.ch.services.dto.mss.mssCore.DataForMail;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.MssToSend;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.entity.mssdb.shops.Shops;
import pearl.ch.services.service.client.ClientService;
import pearl.ch.services.service.mss.email.EmailService;
import pearl.ch.services.service.shops.ShopsService;

@Repository
@RequiredArgsConstructor
public class CoreDAOImpl implements CoreDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;

	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@PersistenceContext(unitName = "dbch1")
	private EntityManager entityManagerDbch1;

	private final ShopURL shopURL;
	private final ServerCname cname;
	private final ShopsService shopsService;
	private final EmailService emailService;
	private final ClientService clientService;
	private final UnsubscribeEncryptionConfig unsubscribeEncryptionConfig;

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	@WriteTransactional
	public boolean filling(int templateId) {

		try (Session session = entityManager.unwrap(Session.class);
				Session sessionDbch1 = entityManagerDbch1.unwrap(Session.class);) {

			if (countEntriesToSend(templateId) == 0) {

				MssTemplates template = session.get(MssTemplates.class, templateId);
				MSSFillers filler = session.get(MSSFillers.class, template.getFiller_id());

				ScrollableResults scrollableResults = sessionDbch1.createNativeQuery("WITH cte AS ( "
						+ "SELECT *, ROW_NUMBER() OVER(PARTITION BY LOWER(c.email) ORDER BY COALESCE(c.upddate, c.crdate) DESC) AS row_num "
						+ "FROM clients c " + ") "
						+ "SELECT COALESCE(c.clients_id, 0) AS clients_id, COALESCE(c.gender, '') AS gender, "
						+ "COALESCE(c.first_name, '') AS first_name, COALESCE(c.last_name, '') AS last_name, "
						+ "LOWER(COALESCE(c.email, f.email)) AS email, COALESCE(a.date_of_birth, '1970-01-01') AS date_of_birth, "
						+ "0 AS state " + "FROM cte c "
						+ "LEFT JOIN clients_additional_info a ON c.clients_id = a.clients_id "
						+ "RIGHT JOIN (" + filler.getSql()	+ ") f ON LOWER(c.email) = LOWER(f.email) "
						+ "WHERE c.row_num = 1 OR c.row_num IS NULL ORDER BY RANDOM()").setReadOnly(true).setFetchSize(1000)
						.setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);

				int batchSize = 2000;
				int counter = 0;

				while (scrollableResults.next()) {
					Object[] client = scrollableResults.get();

					MssToSend mssToSend = new MssToSend();
					mssToSend.setClientId((BigInteger) client[0]);
					mssToSend.setTemplateId(templateId);
					mssToSend.setEmail((String) client[4]);
					mssToSend.setFirst_name((String) client[2]);
					mssToSend.setLast_name((String) client[3]);
					mssToSend.setGender((String) client[1]);
					mssToSend.setDate_of_birth((Date) client[5]);
					mssToSend.setState((int) client[6]);
					mssToSend.setClientUUId(UUID.randomUUID().toString());

					entityManagerWrite.persist(mssToSend);

					if (++counter % batchSize == 0) {
						entityManagerWrite.flush();
						entityManagerWrite.clear();
					}
				}

				entityManagerWrite.flush();
				entityManagerWrite.clear();
				scrollableResults.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@ReadTransactional
	public DataForMail prepareDataForMail(int templateId) {

		MssToSend client = new MssToSend();

		String subject = "";
		String mailFrom = "";
		String htmlTemplate = "";
		String replyTo = "";

		DataForMail data = new DataForMail();

		try (Session session = entityManager.unwrap(Session.class);) {

			MssTemplates template = session.get(MssTemplates.class, templateId);

			subject = template.getMail_subject();
			mailFrom = template.getMail_from();
			int shop = template.getShop_id();
			int language = template.getLanguage_id();
			htmlTemplate = template.getHtml_template();

			replyTo = emailService.getReplyTo(shop, language);

			client = session
					.createQuery("FROM MssToSend m WHERE m.state = 0 AND template_id = :templateId", MssToSend.class)
					.setParameter("templateId", templateId).setMaxResults(1).getResultList().stream().findFirst()
					.orElse(null);

			if (client == null) {
				return null;
			}

			htmlTemplate = htmlTemplate.replaceAll("\\{\\{firstName\\}\\}", client.getFirst_name());
			htmlTemplate = htmlTemplate.replaceAll("\\{\\{lastName\\}\\}", client.getLast_name());
			htmlTemplate = htmlTemplate.replaceAll("\\{\\{email\\}\\}", client.getEmail());
			htmlTemplate = htmlTemplate.replaceAll("\\{\\{dateOfBirth\\}\\}", client.getDate_of_birth().toString());
			htmlTemplate = htmlTemplate.replaceAll("\\{\\{respected\\}\\}",
					clientService.getClientsSalutation(client.getClientId(), language));

			htmlTemplate = htmlTemplate.replaceAll("\\{\\{unsubscribe\\}\\}", getUnsubscribeURL(template, client));

			htmlTemplate = replaceLinks(htmlTemplate, client.getClientUUId(), templateId,
					shopsService.getShopByID(shop));
			htmlTemplate = appendTrackingEmptyImageTag(htmlTemplate, client.getClientUUId(), templateId,
					shopsService.getShopByID(shop));

			data.setMailTo(client.getEmail());
			data.setMailFrom(mailFrom);
			data.setReplyTo(replyTo);
			data.setSubject(subject);
			data.setHtmlTemplate(htmlTemplate);
			data.setClientsId(client.getClientId());

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WriteTransactional
	public boolean setSendState(String  email, int templateId, int state) {
		return entityManagerWrite
				.createQuery("UPDATE MssToSend mts SET mts.state = :state, "
						+ "mts.send_time = :time WHERE mts.email = :email AND templateId = :templateId")
				.setParameter("email", email).setParameter("templateId", templateId).setParameter("state", state)
				.setParameter("time", new Date()).executeUpdate() > 0;
	}

	@ReadTransactional
	private long countEntriesToSend(int templateId) {
		return (long) entityManager
				.createQuery("SELECT COUNT(*) FROM MssToSend WHERE template_id = :template_id and state=0")
				.setParameter("template_id", templateId).getSingleResult();
	}

	/**
	 * Appends an empty image tag to the provided HTML template for tracking email
	 * opens. Image tag is used for tracking, so when user opens email and html
	 * template is rendered img tag will send GET request to provided url in href
	 * attribute. Which is server used for collecting data. The method searches for
	 * the start and end index of the <body> tag in the provided HTML template. If
	 * the <body> tag is found, the method constructs an empty image tag with a URL
	 * that includes the provided client UUID and template ID, and appends it right
	 * before the </body> tag.
	 *
	 * Note: If the <body> tag is not provided in the HTML template, method will
	 * append tracking <img> to the template as the last item.
	 *
	 * @param htmlTemplate The HTML template to which the empty image tag will be
	 *                     appended.
	 * @param clientUUId   The unique identifier (UUID) of the client to be included
	 *                     in the tracking URL.
	 * @param templateId   The ID of the template associated with the client to be
	 *                     included in the tracking URL.
	 * @return The modified HTML template with the appended empty image tag, or the
	 *         original template if no <body> tag is found.
	 */

	private String appendTrackingEmptyImageTag(String htmlTemplate, String clientUUId, int templateId, Shops shop) {
		int htmlBodyStartIndex = htmlTemplate.indexOf("<body>");
		int htmlBodyEndIndex = htmlTemplate.indexOf("</body>");

		String emptyImageTag = "<img src=\"" + cname.getCnameByShop(shop) + "/email/open?clientUUId=" + clientUUId
				+ "&templateId=" + templateId
				+ "\" alt=\"\" width=\"1\" height=\"1\" style=\"border:0;display:block;\">";

		if (htmlBodyStartIndex != -1 && htmlBodyEndIndex != -1) {
			String modifiedHtmlTemplate = htmlTemplate.substring(0, htmlBodyEndIndex) + emptyImageTag
					+ htmlTemplate.substring(htmlBodyEndIndex);
			return modifiedHtmlTemplate;
		}

		return htmlTemplate + emptyImageTag;
	}

	/**
	 * Replaces certain links in the provided HTML content with new redirect links
	 * based on given criteria.
	 *
	 * The method searches for anchor tags (<a>) in the input HTML content using a
	 * regular expression pattern. If the link contained in the anchor tag contains
	 * the specified keywords ("pearl", "natura", "casativo"), the method replaces
	 * the link with a new redirect link, incorporating the provided client UUID and
	 * template ID.
	 *
	 * @param html       The HTML content containing anchor tags whose links need
	 *                   replacement.
	 * @param clientUUId The unique identifier (UUID) of the client to be included
	 *                   in the redirect link.
	 * @param templateId The ID of the template associated with the client to be
	 *                   included in the redirect link.
	 * @return A new string with the updated HTML content containing the replaced
	 *         redirect links.
	 * 
	 */
	private String replaceLinks(String html, String clientUUId, int templateId, Shops shop) {
		String patternString = "<a\\s+href=\"(.*?)\"";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(html);

		StringBuilder result = new StringBuilder();
		int lastIndex = 0;

		while (matcher.find()) {
			String link = matcher.group(1);
			if (link.contains("pearl") || link.contains("natura") || link.contains("casativo")) {
				String replacedLink = cname.getCnameByShop(shop) + "/email/openLink?redirectLink=" + encodeURL(link)
						+ "&clientUUId=" + clientUUId + "&templateId=" + templateId;
				result.append(html, lastIndex, matcher.start(1));
				result.append(replacedLink);
				lastIndex = matcher.end(1);
			}
		}
		result.append(html.substring(lastIndex));
		return result.toString();
	}

	/**
	 * Encodes the provided URL string using UTF-8 encoding. Used for escaping chars
	 * provided in links.
	 *
	 * The method uses the UTF-8 encoding to safely encode special characters in the
	 * URL string. It catches any UnsupportedEncodingException that might occur
	 * during the encoding process and prints the stack trace. If an exception
	 * occurs, the original URL string is returned without encoding.
	 *
	 * @param url The URL string to be encoded.
	 * @return The encoded URL string or the original URL string if encoding fails.
	 */
	private String encodeURL(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	private String getUnsubscribeURL(MssTemplates template, MssToSend client) throws Exception {
		String shop = shopURL.getShopURLByShopId(template.getShop_id());
		shop += "newsletter_subscription.php";

		String encryptionKey, encryptionIv;

		switch (template.getShop_id()) {
		case 1:
			encryptionKey = unsubscribeEncryptionConfig.getPEARL_UNSUBSCRIBE_TOKEN_ENCRYPTION_KEY();
			encryptionIv = unsubscribeEncryptionConfig.getPEARL_UNSUBSCRIBE_TOKEN_ENCRYPTION_IV();
			break;
		case 2:
			encryptionKey = unsubscribeEncryptionConfig.getCASATIVO_UNSUBSCRIBE_TOKEN_ENCRYPTION_KEY();
			encryptionIv = unsubscribeEncryptionConfig.getCASATIVO_UNSUBSCRIBE_TOKEN_ENCRYPTION_IV();
			break;
		case 3:
			encryptionKey = unsubscribeEncryptionConfig.getNATURA_UNSUBSCRIBE_TOKEN_ENCRYPTION_KEY();
			encryptionIv = unsubscribeEncryptionConfig.getNATURA_UNSUBSCRIBE_TOKEN_ENCRYPTION_IV();
			break;
		default:
			encryptionKey = unsubscribeEncryptionConfig.getPEARL_UNSUBSCRIBE_TOKEN_ENCRYPTION_KEY();
			encryptionIv = unsubscribeEncryptionConfig.getPEARL_UNSUBSCRIBE_TOKEN_ENCRYPTION_IV();

		}
		// generate Token by Email

		byte[] keyBytes = encryptionKey.getBytes("UTF-8");
		byte[] ivBytes = encryptionIv.getBytes("UTF-8");

		if (keyBytes.length != 32 || ivBytes.length != 16) {
			throw new IllegalArgumentException("Encryption key and IV must be 16 bytes long");
		}

		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		byte[] encryptedBytes = cipher.doFinal(client.getEmail().getBytes("UTF-8"));
		return shop + "?unsub=1&token="
				+ URLEncoder.encode(Base64.getEncoder().encodeToString(encryptedBytes), "UTF-8");
	}
	
	
	

}
