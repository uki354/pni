package pearl.ch.services.dao.mss.fillers;

import java.util.List;

import net.minidev.json.JSONObject;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;

public interface FillersDAO {

	List<MSSFillers> getAllFillers();
	
	JSONObject deleteFillers(int fillerId);

	JSONObject getAllForForm(int fillerId);
	
	JSONObject insertFiller(int fillerId, int languageId, int shopId, int typeId, String name, String sql, int userId);
	
	JSONObject copyFiller(int fillerId, int userId);
	
	JSONObject checkSql(String sql);
	
	List<MSSFillers> getMssFillersByLangAndShopId(int shop_id, int languageId);
}
