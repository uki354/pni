package pearl.ch.services.service.mss.languages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pearl.ch.services.dao.mss.languages.LanguagesDAO;
import pearl.ch.services.entity.mssdb.languages.Languages;

@Service
public class LanguagesServiceImpl implements LanguagesService {

	@Autowired
	private LanguagesDAO languagesDAO;
	
	@Override
	public List<Languages> getLanguages() {
		
		return languagesDAO.getLanguages();
	}

}
