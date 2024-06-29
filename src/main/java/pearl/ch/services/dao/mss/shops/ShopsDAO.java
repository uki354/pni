package pearl.ch.services.dao.mss.shops;

import java.util.List;

import pearl.ch.services.entity.mssdb.shops.Shops;


public interface ShopsDAO {

	public List<Shops> getShops();
	
	public Shops getShopByID(int shop_id);
	
}
