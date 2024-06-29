package pearl.ch.services.controller.mss.types;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pearl.ch.services.entity.mssdb.mss.MssTypes;
import pearl.ch.services.service.mss.types.MssTypesService;

@RestController
@CrossOrigin
@RequestMapping("mss/")
public class MssTypesController {

	@Autowired
	private MssTypesService mSSTypesService;

	@GetMapping("/getMssTypesByShop")
	public List<MssTypes> getMssTypes(@RequestParam("shopId") int shopId) {

		return mSSTypesService.getMssTypesByShop(shopId);
	}

}
