package pearl.ch.services.dao.mss.mailFrom;

import java.util.List;

import pearl.ch.services.entity.mssdb.mss.MssSender;

public interface MailFromDAO {
	
	public List<MssSender> getMailFrom(int shopId, int languageId);

}
