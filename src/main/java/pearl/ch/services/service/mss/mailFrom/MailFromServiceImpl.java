package pearl.ch.services.service.mss.mailFrom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pearl.ch.services.dao.mss.mailFrom.MailFromDAO;
import pearl.ch.services.entity.mssdb.mss.MssSender;

@Service
public class MailFromServiceImpl implements MailFromService {
	
	@Autowired
	public MailFromDAO  mailFromDAO;

	@Override
	public List<MssSender> getMailFrom(int shopId, int languageId) {
		
		return mailFromDAO.getMailFrom(shopId, languageId);
	}

}
