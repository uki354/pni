package pearl.ch.services.service.mss.tracking;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.dao.mss.tracking.EmailTrackingDAO;
import pearl.ch.services.entity.mssdb.mss.MssTrackingClicked;
import pearl.ch.services.entity.mssdb.mss.MssTrackingOpen;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingClickedPK;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingOpenPK;
import pearl.ch.services.service.client.ClientService;
import ua_parser.Client;
import ua_parser.Parser;

@Service
@RequiredArgsConstructor
public class EmailTrackingServiceImpl implements EmailTrackingService {

	private final EmailTrackingDAO emailDataDao;
	private final ClientService clientService;

	
	/**
	 * Saves EmailDataOpen object to database
	 *
	 * @param clientUUId       The unique identifier of the client who opened the email.
	 * @param userAgentHeader  The User-Agent header from the client's HTTP request.
	 * @param templateId       The ID of the email template that was opened.
	 */
	@Override
	public void saveEmailDataOpen(String clientUUId, String userAgentHeader, int templateId) {
		final Parser parser = new Parser();
		final Client client = parser.parse(userAgentHeader);

		emailDataDao.saveEmailDataOpen(MssTrackingOpen.builder().clientId(clientService.getClientIdByClientUUId(clientUUId, templateId))
				.count(1)
				.device(client.device.family)
				.os(appendNonNullValues(client.os.family, client.os.major, client.os.minor))
				.userAgent(appendNonNullValues(client.userAgent.family, client.userAgent.major, client.userAgent.minor))
				.openAt(LocalDateTime.now()).lastOpenAt(LocalDateTime.now())
				.mssTrackingOpenPK(new MssTrackingOpenPK(clientUUId,templateId))				
				.build());
	}


	/**
	 * Saves EmailRedirectTracking object to database. If pk already exists.
	 * Update existing one
	 *
	 * @param clientUUId       The unique identifier of the client who clicked the link.
	 * @param userAgentHeader  The User-Agent header from the client's HTTP request.
	 * @param templateId       The ID of the email template that contained the link.
	 * @param redirectLink     The link that the user clicked on.
	 */
	@Override
	public void saveEmailRedirect(String clientUUId, String userAgentHeader, int templateId, String redirectLink) {
		final Parser parser = new Parser();
		final Client client = parser.parse(userAgentHeader);

		MssTrackingClickedPK pk = new MssTrackingClickedPK(clientUUId,templateId, redirectLink);

		if (getEmailRedirectTracking(pk) != null) {
			updateEmailRedirect(pk);
		} else {
			emailDataDao.saveEmailRedirect(
					MssTrackingClicked.builder().clientId(clientService.getClientIdByClientUUId(clientUUId, templateId)).count(1).device(client.device.family)
							.os(appendNonNullValues(client.os.family, client.os.major, client.os.minor))
							.userAgent(appendNonNullValues(client.userAgent.family, client.userAgent.major,
									client.userAgent.minor))
							.openAt(LocalDateTime.now()).lastOpenAt(LocalDateTime.now()).pk(pk).build());

		}

	}

	@Override
	/**
	 * Updates EmailRedirectTracking object in database. By incrementing count value 
	 * and updating time stamp of being last opened at.
	 *
	 * @param pk The primary key representing the email redirect tracking data.
	 */
	public void updateEmailRedirect(MssTrackingClickedPK pk) {
		MssTrackingClicked emailRedirectTracking = emailDataDao.getEmailRedirectTracking(pk);
		emailRedirectTracking.setLastOpenAt(LocalDateTime.now());
		emailRedirectTracking.setCount(emailRedirectTracking.getCount() + 1);
		emailDataDao.saveEmailRedirect(emailRedirectTracking);
	}
	
	/**
	 * Retrieves the email redirect tracking data associated with the provided primary key.
	 *
	 * @param pk The primary key representing the email redirect tracking data.
	 * @return   The EmailRedirectTracking object associated with the provided primary key,
	 *           or null if no matching data is found.
	 */
	@Override
	public MssTrackingClicked getEmailRedirectTracking(MssTrackingClickedPK pk) {
		return emailDataDao.getEmailRedirectTracking(pk);
	}
	
	/**
	 * Retrieves the email tracking open data associated with the provided primary key.
	 *
	 * @param pk The primary key representing the email tracking open data.
	 * @return   The EmailTrackingOpen object associated with the provided primary key,
	 *           or null if no matching data is found.
	 */
	@Override
	public MssTrackingOpen getEmailTrackingOpen(MssTrackingOpenPK pk) {
		return emailDataDao.getEmailTrackingOpen(pk);
	}

	/**
	 * Updates the email tracking open data when a user opens an email multiple times.
	 * Increment count field & last open time stamp.
	 *
	 * @param pk The primary key representing the email tracking open data.
	 */
	@Override
	public void updateEmailDataOpen(MssTrackingOpenPK pk) {
		MssTrackingOpen emailTrackingOpen = emailDataDao.getEmailTrackingOpen(pk);
		emailTrackingOpen.setCount(emailTrackingOpen.getCount() + 1);
		emailTrackingOpen.setLastOpenAt(LocalDateTime.now());
		emailDataDao.saveEmailDataOpen(emailTrackingOpen);
	}
	
	
	/**
	 * Asynchronously collects email data for tracking purposes and saves the click event data.
	 * This method is intended to be invoked asynchronously using the @Async annotation,
	 * allowing the data collection to happen independently in a separate thread.
	 *
	 * @param clientUUID    The unique identifier of the client who clicked the link.
	 * @param templateId    The ID of the email template that contained the link.
	 * @param redirectLink  The link to which the user will be redirected.
	 * @param request       The HTTP servlet request containing the user-agent header.
	 */
	@Async
	@Override
	public void collectEmailDataAsync(String clientUUID, int templateId, String redirectLink, HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		saveEmailRedirect(clientUUID,userAgent,templateId,redirectLink);
	}
	
	/**
	 * Concatenates non-null values from the provided String arguments and returns the result in lower case.
	 *
	 * @param s The String arguments from which non-null values will be concatenated.
	 * @return  A concatenated String containing non-null values in lower case, or an empty String if all arguments are null.
	 */
	private String appendNonNullValues(String... s) {
		StringBuilder sb = new StringBuilder();
		for (String a : s) {
			if (a != null) {
				sb.append(" " + a.toLowerCase());
			}
		}

		return sb.toString();
	}

}
