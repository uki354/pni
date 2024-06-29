package pearl.ch.services.dao.mss.types;

import java.util.List;

import pearl.ch.services.entity.mssdb.mss.MssTypes;

public interface MssTypesDAO {
	
	public List<MssTypes> getMssTypes();
	
	public List<MssTypes> getMssTypesByShop(int shopId);

}
