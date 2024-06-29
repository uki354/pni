package pearl.ch.services.controller.mss.shops;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pearl.ch.services.entity.mssdb.shops.Shops;
import pearl.ch.services.service.shops.ShopsService;

@RestController
@CrossOrigin
@RequestMapping("mss/")
public class ShopsController {
	
	@Autowired
	private ShopsService shopsService;
	
	@GetMapping("/getShops")
	public List<Shops> getShops(){
		
		List<Shops> shops = shopsService.getShops();
		
		return shops;
	}

}
