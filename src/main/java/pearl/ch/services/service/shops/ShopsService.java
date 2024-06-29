package pearl.ch.services.service.shops;

import java.util.List;

import pearl.ch.services.entity.mssdb.shops.Shops;

public interface ShopsService {

	public List<Shops> getShops();
	
	public Shops getShopByID(int shop_id);
}
