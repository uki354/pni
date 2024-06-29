package pearl.ch.services.service.mss.fillers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;
import pearl.ch.services.dao.mss.fillers.FillersDAO;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;

@Service
public class FillersServiceImpl implements FillersService {

	@Autowired
	private FillersDAO fillersDAO;

	public List<MSSFillers> getAllFillers() {

		return fillersDAO.getAllFillers();

	}

	public JSONObject deleteFillers(int fillerId) {

		return fillersDAO.deleteFillers(fillerId);
	}

	public JSONObject getAllForForm(int fillerId) {
		return fillersDAO.getAllForForm(fillerId);
	}

	public JSONObject insertFiller(int fillerId, int languageId, int shopId, int typeId, String name, String sql) {

		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		int userId = Integer.parseInt(currentUserName);

		return fillersDAO.insertFiller(fillerId, languageId, shopId, typeId, name, sql, userId);
	}

	public JSONObject copyFiller(int fillerId) {

		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		int userId = Integer.parseInt(currentUserName);

		return fillersDAO.copyFiller(fillerId, userId);
	}

	public JSONObject checkSql(String sql) {
		return fillersDAO.checkSql(sql);
	}

	@Override
	public List<MSSFillers> getMssFillersByLangAndShopId(int shop_id, int langugageId) {
		return fillersDAO.getMssFillersByLangAndShopId(shop_id, langugageId);
	}

}
