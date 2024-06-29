package pearl.ch.services.controller.mss.fillers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import pearl.ch.services.service.mss.fillers.FillersService;
import pearl.ch.services.service.mss.languages.LanguagesService;
import pearl.ch.services.service.mss.types.MssTypesService;
import pearl.ch.services.service.shops.ShopsService;

/**
 * 
 * @author Nikola Mitic
 *
 */

@RestController
@CrossOrigin
@RequestMapping("fillers/")
public class FillersController {

	@Autowired
	private FillersService fillersService;

	@Autowired
	private ShopsService shopsService;

	@Autowired
	private LanguagesService languagesService;

	@Autowired
	private MssTypesService typesService;

	@RequestMapping("/getAllFillers")
	@Transactional
	public @ResponseBody ResponseEntity<?> fillersService() {

		JSONObject jsonRespond = new JSONObject();

		jsonRespond.put("allFillers", fillersService.getAllFillers());
		jsonRespond.put("shops", shopsService.getShops());
		jsonRespond.put("languages", languagesService.getLanguages());
		jsonRespond.put("mssTypes", typesService.getMssTypes());

		return new ResponseEntity<Object>(jsonRespond, HttpStatus.OK);
	}

	@PostMapping("/deleteFillers")
	@Transactional
	public @ResponseBody JSONObject deleteFiller(@RequestParam int fillerId) {

		JSONObject entity = fillersService.deleteFillers(fillerId);

		return entity;
	}

	@RequestMapping("/getAllForForm")
	@Transactional
	public @ResponseBody ResponseEntity<?> getAllForForm(@RequestParam("fillerId") int fillerId) {

		JSONObject entity = fillersService.getAllForForm(fillerId);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@PostMapping("/insertFiller")
	@Transactional
	public @ResponseBody ResponseEntity<?> insertFiller(@RequestBody Map<String, ?> fillerData) {

		JSONObject jsonFillerData = new JSONObject(fillerData);

		JSONObject entity = fillersService.insertFiller(Integer.parseInt(jsonFillerData.getAsString("fillerId")),
				Integer.parseInt(jsonFillerData.getAsString("languageId")),
				Integer.parseInt(jsonFillerData.getAsString("shopId")),
				Integer.parseInt(jsonFillerData.getAsString("typeId")), jsonFillerData.getAsString("name"),
				jsonFillerData.getAsString("sql"));

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@PostMapping("/copyFiller")
	public @ResponseBody ResponseEntity<?> copyFiller(@RequestParam("fillerId") int fillerId) {

		JSONObject entity = fillersService.copyFiller(fillerId);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);

	}

	@PostMapping("/checkSql")
	@Transactional
	public @ResponseBody ResponseEntity<?> checkSql(@RequestBody Map<String, ?> sqlQuery) {

		JSONObject jsonSqlQuery = new JSONObject(sqlQuery);

		JSONObject entity = fillersService.checkSql(jsonSqlQuery.getAsString("sql"));

		return new ResponseEntity<Object>(entity, HttpStatus.OK);

	}

}
