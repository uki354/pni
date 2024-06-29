package pearl.ch.services.service.shops;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pearl.ch.services.dao.mss.shops.ShopsDAO;
import pearl.ch.services.entity.mssdb.shops.Shops;

@Service
public class ShopsServiceImpl implements ShopsService {

	@Autowired
	private ShopsDAO shopsDAO;

	@Override
	public List<Shops> getShops() {

		return shopsDAO.getShops();
	}

	@Override
	public Shops getShopByID(int shop_id) {
		return shopsDAO.getShopByID(shop_id);
	}

}
