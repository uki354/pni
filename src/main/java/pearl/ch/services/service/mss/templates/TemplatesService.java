package pearl.ch.services.service.mss.templates;

import java.util.List;

import pearl.ch.services.dto.mss.templates.TemplatesDTO;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.enums.TemplateState;


public interface TemplatesService {
	
	
	MssTemplates getTemplateById(int Id);
	 
	String getCoordersProductsTemplate(String coordersUrl);
	
	List<TemplatesDTO> getAll(int limit);
	
	List<MssTemplates> getTemplatesByState(TemplateState state);
	 
	List<MssTemplates> getTemplatesByIds(List<Integer> ids);
	 
	List<MSSFillers> getMssFillers(int shopId, int languageId);	
	
	int saveTemplate(MssTemplates mssTemplate, int userId);
	
	boolean copyTemplate(int id,int userId);	
	
	boolean updateTemplate(MssTemplates mssTemplate, int userId);
	
	boolean updateTemplateState(int templateId, TemplateState state);
	
	boolean sendTestMail(String mailAdress, String mailSubject, String mailBody, String mailFrom, int shop, int language);	 
	
	boolean uploadMailToAWS(MssTemplates mssTemplates);

}
