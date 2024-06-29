package pearl.ch.services.controller.mss.mailFrom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pearl.ch.services.entity.mssdb.mss.MssSender;
import pearl.ch.services.service.mss.mailFrom.MailFromService;

@RestController
@CrossOrigin
@RequestMapping("mss")
public class MailFromController {
	
	@Autowired
	private MailFromService mailFromService;
	
	@GetMapping("/getMailFrom")
	public List<MssSender> getMailFrom(
			@RequestParam("shopId") int shopId,
			@RequestParam("languageId") int languageId) {
		
		return mailFromService.getMailFrom(shopId, languageId);
	
	}

}
