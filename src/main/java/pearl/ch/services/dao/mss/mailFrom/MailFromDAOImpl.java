package pearl.ch.services.dao.mss.mailFrom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.entity.mssdb.mss.MssSender;

@Repository
@ReadTransactional
public class MailFromDAOImpl implements MailFromDAO{
	
	@PersistenceContext(unitName = "mssRead")
	public EntityManager entityManager;

	@Override
	public List<MssSender> getMailFrom(int shopId, int languageId) {
		
		return entityManager.createQuery("FROM MssSender WHERE shop_id = :shop_id AND language_id = :language_id", MssSender.class)
				.setParameter("shop_id", shopId)
				.setParameter("language_id", languageId).getResultList();
	}

}
