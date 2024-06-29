package pearl.ch.services.service.mss.mailFrom;

import java.util.List;

import pearl.ch.services.entity.mssdb.mss.MssSender;

public interface MailFromService {
	
	public List<MssSender> getMailFrom(int shopId, int languageId);

}
