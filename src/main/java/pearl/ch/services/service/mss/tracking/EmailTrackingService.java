package pearl.ch.services.service.mss.tracking;

import javax.servlet.http.HttpServletRequest;

import pearl.ch.services.entity.mssdb.mss.MssTrackingClicked;
import pearl.ch.services.entity.mssdb.mss.MssTrackingOpen;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingOpenPK;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingClickedPK;

public interface EmailTrackingService {
	
	void saveEmailDataOpen(String clientUUId, String userAgentHeader, int templateId);
	MssTrackingOpen getEmailTrackingOpen(MssTrackingOpenPK pk);
	void updateEmailDataOpen(MssTrackingOpenPK pk);
	void saveEmailRedirect(String clientUUId, String userAgentHeader, int templateId, String redirectLink);
	void updateEmailRedirect(MssTrackingClickedPK pk);
	MssTrackingClicked getEmailRedirectTracking(MssTrackingClickedPK pk);
	void collectEmailDataAsync(String clientUUID, int templateId, String redirectLink, HttpServletRequest request);

}
