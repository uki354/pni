package pearl.ch.services.dao.mss.types;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.entity.mssdb.mss.MssTypes;

@Repository
public class MssTypesDAOImpl implements MssTypesDAO {

	@PersistenceContext(unitName = "mssRead")
	public EntityManager entityManager;

	@Override
	@ReadTransactional
	public List<MssTypes> getMssTypes() {
		return entityManager.createQuery("from MssTypes", MssTypes.class).getResultList();
	}

	@Override
	@ReadTransactional
	public List<MssTypes> getMssTypesByShop(int shopId) {
		return entityManager.createQuery("from MssTypes WHERE shop_id = :shop_id", MssTypes.class)
				.setParameter("shop_id", shopId).getResultList();		
	}

}