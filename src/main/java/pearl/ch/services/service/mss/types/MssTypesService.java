package pearl.ch.services.service.mss.types;

import java.util.List;

import pearl.ch.services.entity.mssdb.mss.MssTypes;

public interface MssTypesService {

	public List<MssTypes> getMssTypes();

	public List<MssTypes> getMssTypesByShop(int shopId);

}
