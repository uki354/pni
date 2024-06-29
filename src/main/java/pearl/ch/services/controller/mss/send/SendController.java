package pearl.ch.services.controller.mss.send;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.enums.TemplateState;
import pearl.ch.services.service.mss.send.SendService;

@RestController
@CrossOrigin
@RequestMapping("mss/send")
@RequiredArgsConstructor
public class SendController {
	
	private final SendService sendService;

	@GetMapping("/getTemplatesByState")
	public List<MssTemplates> getTemplates(@RequestParam("state") TemplateState state) {
		return sendService.getTemplatesByState(state);
	}

	@GetMapping("/getTemplate")
	public MssTemplates getTemplate(@RequestParam("templateId") int templateId) {

		return sendService.getTemplate(templateId);

	}

	@GetMapping("/getTemplatesById")
	public List<MssTemplates> getTemplatesByIds(@RequestParam List<Integer> ids) {
		return sendService.getTemplatesByIds(ids);
	}

	@GetMapping("/getFillers")
	public List<MSSFillers> getFillers() {

		return sendService.getFillers();

	}

	@PostMapping("/sendNewsletter")
	public @ResponseBody boolean sendNewsletter(@RequestBody Map<String, ?> newsletterData) {

		JSONObject jsonNewsletterData = new JSONObject(newsletterData);

		return sendService.sendNewsletter(Integer.parseInt(jsonNewsletterData.getAsString("templateId")));

	}

	@PostMapping("/sendNewsletterAt")
	public @ResponseBody boolean sendNewsletterAt(@RequestBody Map<String, ?> newsletterData) {

		sendService.sendNewsletterAt(Integer.parseInt((String) newsletterData.get("templateId")),
				(String) newsletterData.get("timestamp"), (String) newsletterData.get("timeZone"),
				(int) newsletterData.get("repeatAfter"));

		return true;
	}

}