package pearl.ch.services.dao.mss.templates;

import java.util.List;

import pearl.ch.services.dto.mss.templates.TemplatesDTO;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.enums.TemplateState;

public interface TemplatesDAO {

	 List<TemplatesDTO> getAll(int limit);
	
	 List<MssTemplates> getTemplatesByState(TemplateState state);

	 MssTemplates getTemplateById(int Id);
	
	 List<MssTemplates> getTemplatesByIds(List<Integer> ids);

	 MssTemplates copyTemplate(int id, int userId);

	 int saveTemplate(MssTemplates mssTemplate, int userId);

	 boolean updateTemplate(MssTemplates mssTemplate, int userId);
	
	 boolean updateTemplateState(int templateId, TemplateState state);

}
