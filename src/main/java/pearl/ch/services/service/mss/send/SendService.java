package pearl.ch.services.service.mss.send;

import java.util.List;

import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.enums.TemplateState;

public interface SendService {

	List<MssTemplates> getTemplatesByState(TemplateState state);

	MssTemplates getTemplate(int templateId);

	List<MSSFillers> getFillers();

	boolean sendNewsletter(int templateId);

	void sendNewsletterAt(int templateId, String timestamp, String timeZoneOffset,
			int repeatAfter);

	List<MssTemplates> getTemplatesByIds(List<Integer> ids);

}
