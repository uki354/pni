package pearl.ch.services.dao.mss.tracking;

import pearl.ch.services.entity.mssdb.mss.MssTrackingClicked;
import pearl.ch.services.entity.mssdb.mss.MssTrackingOpen;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingOpenPK;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingClickedPK;

public interface EmailTrackingDAO {
	
	void saveEmailDataOpen(MssTrackingOpen emailTrackingOpen);
	void saveEmailRedirect(MssTrackingClicked emailRedirectTracking);
	MssTrackingOpen getEmailTrackingOpen(MssTrackingOpenPK mssTrackingOpenPK);
	MssTrackingClicked getEmailRedirectTracking(MssTrackingClickedPK pk);

}
