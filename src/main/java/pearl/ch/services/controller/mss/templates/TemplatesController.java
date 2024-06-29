package pearl.ch.services.controller.mss.templates;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import pearl.ch.services.dto.mss.templates.TemplatesDTO;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.service.mss.templates.TemplatesService;

@RestController
@RequestMapping("mss/templates")
@CrossOrigin
public class TemplatesController {

	@Autowired
	public TemplatesService mssTemplatesService;

	@GetMapping("/getAll")
	public List<TemplatesDTO> getAll(@RequestParam(name = "limit") int limit) {

		return mssTemplatesService.getAll(limit);
	}

	@GetMapping("/getTemplateById/{id}")
	public ResponseEntity<MssTemplates> getTemplateById(@PathVariable(value = "id") Integer id) {

		MssTemplates mssTemplate = mssTemplatesService.getTemplateById(id);

		if (mssTemplate != null) {
			return new ResponseEntity<>(mssTemplate, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(mssTemplate, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/copyTemplate/{id}/{userId}")
	public @ResponseBody boolean copyTemplate(@PathVariable(value = "id") Integer id,
			@PathVariable(value = "userId") Integer userId) {

		return mssTemplatesService.copyTemplate(id, userId);
	}
	
	@PostMapping("/saveTemplate/{userId}")
	public @ResponseBody int saveTemplate(@RequestBody MssTemplates mssTemplate,
			@PathVariable(value = "userId") int userId) {

		return mssTemplatesService.saveTemplate(mssTemplate, userId);
	}

	@PostMapping("updateTemplate/{userId}")
	public @ResponseBody boolean updateTemplate(@RequestBody MssTemplates mssTemplate,
			@PathVariable(value = "userId") int userId) {
		return mssTemplatesService.updateTemplate(mssTemplate, userId);
	}



	@PostMapping("/getCoordersProductsTemplate")
	public JSONObject getCoordersProductsTemplate(@RequestParam(name = "coordersURL") String coordersUrl) {

		JSONObject jsonRespond = new JSONObject();

		jsonRespond.put("coordersProducts", mssTemplatesService.getCoordersProductsTemplate(coordersUrl));
		return jsonRespond;
	}

	@PostMapping("/sendTestMail")
	public @ResponseBody boolean sendTestMail(@RequestBody Map<String, ?> mailData) {

		JSONObject jsonMailData = new JSONObject(mailData);
		
		return mssTemplatesService.sendTestMail(
				jsonMailData.getAsString("mailAddressForTest"), 
				jsonMailData.getAsString("mailSubject"), 
				jsonMailData.getAsString("mailBody"), 
				jsonMailData.getAsString("mailFrom"),
				Integer.parseInt(jsonMailData.getAsString("shop")),
			    Integer.parseInt(jsonMailData.getAsString("language")));
	}

	@GetMapping("/getMssFillers")
	public List<MSSFillers> getMssFillers(@RequestParam("shopId") int shopId,
			@RequestParam("languageId") int languageId) {

		return mssTemplatesService.getMssFillers(shopId, languageId);

	}



}
