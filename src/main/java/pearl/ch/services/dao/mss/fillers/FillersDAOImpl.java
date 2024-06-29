package pearl.ch.services.dao.mss.fillers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONObject;
import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.config.db.jdbc.Postgres;
import pearl.ch.services.entity.mssdb.languages.Languages;
import pearl.ch.services.entity.mssdb.mss.MssTypes;
import pearl.ch.services.entity.mssdb.mss.fillers.MSSFillers;
import pearl.ch.services.entity.mssdb.shops.Shops;

@Repository
public class FillersDAOImpl implements FillersDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;
	
	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@ReadTransactional
	public List<MSSFillers> getAllFillers() {

		List<MSSFillers> allFillers = null;

		try (Session session = entityManager.unwrap(Session.class);){

			allFillers = session.createQuery("FROM MSSFillers ORDER BY filler_id DESC", MSSFillers.class).getResultList();

		} catch (Exception exc) {
			exc.printStackTrace();

		}

		return allFillers;
	}

	@WriteTransactional
	public JSONObject deleteFillers(int fillerId) {

		JSONObject jsonRespond = new JSONObject();

		String response = "";


		try (Session session = entityManagerWrite.unwrap(Session.class);){

			MSSFillers filler = session.get(MSSFillers.class, fillerId);
			session.delete(filler);

			response = "true";

			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;

	}

	@ReadTransactional
	public JSONObject getAllForForm(int fillerId) {

		JSONObject jsonRespond = new JSONObject();

		List<Shops> shops = new ArrayList<Shops>();
		List<Languages> languages = new ArrayList<Languages>();
		List<MssTypes> mssTypes = new ArrayList<MssTypes>();

		try (Session session = entityManager.unwrap(Session.class);){

			shops = session.createQuery("FROM Shops", Shops.class).getResultList();
			languages = session.createQuery("FROM Languages", Languages.class).getResultList();
			mssTypes = session.createQuery("FROM MssTypes", MssTypes.class).getResultList();

			jsonRespond.put("shops", shops);
			jsonRespond.put("languages", languages);
			jsonRespond.put("mssTypes", mssTypes);

			if (fillerId != 0) {
				MSSFillers mssFiller;

				mssFiller = session.get(MSSFillers.class, fillerId);
				jsonRespond.put("language", mssFiller.getLanguage_id());
				jsonRespond.put("shop", mssFiller.getShop_id());
				jsonRespond.put("typeId", mssFiller.getMss_type_id());
				jsonRespond.put("name", mssFiller.getName());
				jsonRespond.put("sql", mssFiller.getSql());

			}

		} catch (Exception exc) {
			exc.printStackTrace();

		}

		return jsonRespond;

	}

	@WriteTransactional
	public JSONObject insertFiller(int fillerId, int languageId, int shopId, int typeId, String name, String sql,
			int userId) {		
		
		sql = addDistinctAndCleanQuery(sql);
		JSONObject jsonRespond = new JSONObject();
		String response = "";

		Date date = new Date();

		try (Session session = entityManagerWrite.unwrap(Session.class);){
			
			MSSFillers mssFillers = session.get(MSSFillers.class, fillerId);

			if (mssFillers == null) {

				mssFillers = new MSSFillers();
				mssFillers.setCruser(userId);
				mssFillers.setCrdate(date);
			} else {

				mssFillers.setUpduser(userId);
				mssFillers.setUpddate(date);
			}

			mssFillers.setName(name);
			mssFillers.setShop_id(shopId);
			mssFillers.setLanguage_id(languageId);
			mssFillers.setSql(sql);
			mssFillers.setMss_type_id(typeId);

			session.saveOrUpdate(mssFillers);

			response = "true";
			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;

	}
	
	@WriteTransactional
	public JSONObject copyFiller(int fillerId, int userId) {

		JSONObject jsonRespond = new JSONObject();
		String response = "";
		Date date = new Date();

		try (Session session = entityManagerWrite.unwrap(Session.class);){

			MSSFillers fillerToCopy = session.get(MSSFillers.class, fillerId);

			if (fillerToCopy != null) {

				MSSFillers fillerCopy = new MSSFillers();

				// copy all properties except those in arguments list
				BeanUtils.copyProperties(fillerToCopy, fillerCopy);

				// setting name of copied template to standard: COPY_srcName
				fillerToCopy.setName("COPY_" + fillerToCopy.getName());

				// set creation date and user who created template copy
				fillerCopy.setCruser(userId);
				fillerCopy.setCrdate(date);
				fillerCopy.setUpduser(null);
				fillerCopy.setUpddate(null);

				session.save(fillerCopy);

				response = "true";

			} else {
				// If we couldn't find a filler with the given ID in the database, the operation
				// failed.
				response = "false";
			}

			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;

	}

	public JSONObject checkSql(String sql) {
		
		sql = addDistinctAndCleanQuery(sql);
		
		JSONObject jsonRespond = new JSONObject();
		String response = "";

		if (!isSelectQuery(sql)) {
			jsonRespond.put("response", "none");
			return jsonRespond;
		}

		Postgres pgsql = (Postgres) Postgres.dbch1;

		pgsql.openConnection();

		int count = 1;

		try {

			// Execute PostgreSQL query
			ResultSet rs = pgsql.executeQuery(sql);

			// check if there is no returned objects or there is no table with that name
			if (Objects.isNull(rs)) {
				response = "none";
			} else {
				// get column type
				ResultSetMetaData meta = rs.getMetaData();

				// check how many columns it got
				if (meta.getColumnCount() == 1) {
					// check is the column type int
					if (!meta.getColumnName(1).equals("email")) {
						response = "Not column";
					} else {

						if (!rs.next()) {
							response = "No rows";
						} else {
							// get count with .last() and .getRow() fun
							rs.last();
							count = rs.getRow();

							jsonRespond.put("count", count);
						}
					}
				} else {
					response = "columns";
				}
			}

			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "error";
			jsonRespond.put("response", response);

		} finally {
			pgsql.closeConnection();
		}

		return jsonRespond;

	}

	private boolean isSelectQuery(String sql) {
		String modifyingRegex = "\\b(UPDATE|DELETE|INSERT|TRUNCATE|ALTER|CREATE|DROP|MERGE|REPLACE)\\b";
		Pattern pattern = Pattern.compile(modifyingRegex, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(sql);

		return !matcher.find();
	}
	
	private String addDistinctAndCleanQuery(String sqlQuery) {
		
        String cleanedQuery = sqlQuery.replace(";", "").trim();
        
        if (!cleanedQuery.toUpperCase().contains("DISTINCT")) {
            cleanedQuery = cleanedQuery.replaceFirst("(?i)SELECT", "SELECT DISTINCT");
        }

        return cleanedQuery;
    }

	@Override
	@ReadTransactional
	public List<MSSFillers> getMssFillersByLangAndShopId(int shop_id, int langugageId) {
		
		return entityManager
				.createQuery("FROM MSSFillers WHERE shop_id = :shop_id AND language_id = :language_id",
						MSSFillers.class)
				.setParameter("shop_id", shop_id).setParameter("language_id", langugageId).getResultList();
	}

}
