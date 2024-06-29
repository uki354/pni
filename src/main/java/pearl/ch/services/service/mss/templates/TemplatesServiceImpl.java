package pearl.ch.services.service.mss.templates;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.aws.AWSConfig;
import pearl.ch.services.aws.AWSS3Service;
import pearl.ch.services.dao.mss.templates.TemplatesDAO;
import pearl.ch.services.dto.mss.templates.TemplatesDTO;
import pearl.ch.services.entity.dbch1.clients.Clients;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.entity.mssdb.shops.Shops;
import pearl.ch.services.enums.TemplateState;
import pearl.ch.services.service.client.ClientService;
import pearl.ch.services.service.mss.email.EmailService;
import pearl.ch.services.service.mss.fillers.FillersService;
import pearl.ch.services.service.shops.ShopsService;

@Service
@RequiredArgsConstructor
public class TemplatesServiceImpl implements TemplatesService {

	private final TemplatesDAO templatesDAO;
	private final ClientService clientService;
	private final EmailService emailService;
	private final AWSS3Service awsService;
	private final AWSConfig awsConfig;
	private final ShopsService mssShopsService;
	private final FillersService fillersService;

	@Override
	public List<TemplatesDTO> getAll(int limit) {
		return templatesDAO.getAll(limit);
	}

	@Override
	public List<MssTemplates> getTemplatesByState(TemplateState state) {
		return templatesDAO.getTemplatesByState(state);
	}

	@Override
	public MssTemplates getTemplateById(int Id) {

		return templatesDAO.getTemplateById(Id);
	}

	@Override
	public List<MssTemplates> getTemplatesByIds(List<Integer> ids) {
		return templatesDAO.getTemplatesByIds(ids);
	}

	@Override
	public boolean copyTemplate(int id, int userId) {
		return uploadMailToAWS(templatesDAO.copyTemplate(id, userId));
	}

	@Override
	public int saveTemplate(MssTemplates mssTemplate, int userId) {
		int tempId = templatesDAO.saveTemplate(mssTemplate, userId);
		mssTemplate.setMss_template_id(tempId);
		uploadMailToAWS(mssTemplate);
		return tempId;
	}

	@Override
	public boolean updateTemplate(MssTemplates mssTemplate, int userId) {
		boolean updated = false;
		updated = templatesDAO.updateTemplate(mssTemplate, userId);
		if (updated)
			uploadMailToAWS(mssTemplate);
		return updated;
	}

	@Override
	public boolean updateTemplateState(int templateId, TemplateState state) {
		return templatesDAO.updateTemplateState(templateId, state);
	}

	@Override
	public String getCoordersProductsTemplate(String coordersUrl) {

		String htmlContentString = "";
		try {
			URL url = URI.create(coordersUrl).toURL();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			StringBuffer htmlContent = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				htmlContent.append(inputLine);
			}
			in.close();

			htmlContentString = htmlContent.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return htmlContentString;
	}

	@Override
	public boolean sendTestMail(String mailAdress, String mailSubject, String mailBody, String mailFrom, int shop,
			int language) {

		try {

			String replyTo = emailService.getReplyTo(shop, language);
			Clients clients = clientService.getRandomClient();

			mailBody = mailBody.replaceAll("\\{\\{email\\}\\}", clients.getEmail());
			mailBody = mailBody.replaceAll("\\{\\{respected\\}\\}", clientService.getClientsSalutation(clients.getClients_id(), language));
			emailService.sendEmail(mailAdress, mailSubject, mailBody, mailFrom, replyTo);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<MSSFillers> getMssFillers(int shopId, int languageId) {
		return fillersService.getMssFillersByLangAndShopId(shopId, languageId);
	}
	
	

	public boolean uploadMailToAWS(MssTemplates mssTemplates) {
		try {
			String uploadFolder = "newsletter/" + mssTemplates.getMss_template_id() + "/";

			Shops shop = mssShopsService.getShopByID(mssTemplates.getShop_id());
			String bucket = getBillingBucketForShop(shop);

			awsService.uploadFileToS3(new ByteArrayInputStream(mssTemplates.getHtml_template().getBytes()),
					uploadFolder + "index.html", shop, "html", bucket);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	private String getBillingBucketForShop(Shops shop) {
		switch (shop.getShop_id()) {
		case 1:
			return awsConfig.s3PearlExportBucket;
		case 2:
			return awsConfig.s3CasativoExportBucket;
		case 3:
			return awsConfig.s3NaturaExportBucket;
		default:
			throw new IllegalArgumentException("Can not find cooresponding bucket for: " + shop);
		}
	}
}
