package pearl.ch.services.service.mss.types;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pearl.ch.services.dao.mss.types.MssTypesDAO;
import pearl.ch.services.entity.mssdb.mss.MssTypes;

@Service
public class MssTypesServiceImpl implements MssTypesService {

	@Autowired
	public MssTypesDAO mSSTypesDAO;

	@Override
	public List<MssTypes> getMssTypes() {
		return mSSTypesDAO.getMssTypes();
	}

	@Override
	public List<MssTypes> getMssTypesByShop(int shopId) {

		return mSSTypesDAO.getMssTypesByShop(shopId);
	}

}
