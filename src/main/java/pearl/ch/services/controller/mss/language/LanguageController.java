package pearl.ch.services.controller.mss.language;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pearl.ch.services.entity.mssdb.languages.Languages;
import pearl.ch.services.service.mss.languages.LanguagesService;

@RestController
@CrossOrigin
@RequestMapping("mss")
public class LanguageController {

	
	@Autowired
	private LanguagesService languagesService;
	
	@GetMapping("/getLanguages")
	public List<Languages> getAll() {
		
		return languagesService.getLanguages();
	}
	
}
