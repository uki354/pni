package pearl.ch.services.service.mss.fillers;

import java.util.List;

import net.minidev.json.JSONObject;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;

public interface FillersService {

	List<MSSFillers> getAllFillers();

	JSONObject deleteFillers(int fillerId);

	JSONObject getAllForForm(int fillerId);

	JSONObject insertFiller(int fillerId, int languageId, int shopId, int typeId, String name, String sql);

	JSONObject copyFiller(int fillerId);

	JSONObject checkSql(String sql);
	
	List<MSSFillers> getMssFillersByLangAndShopId(int shop_id, int langugageId);
	
}
