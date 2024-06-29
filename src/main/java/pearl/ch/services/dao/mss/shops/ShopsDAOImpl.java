package pearl.ch.services.dao.mss.shops;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.entity.mssdb.shops.Shops;

@Repository
@ReadTransactional
public class ShopsDAOImpl implements ShopsDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;

	@Override
	public List<Shops> getShops() {
		return entityManager.createQuery("from Shops", Shops.class).getResultList();		
	}

	@Override
	public Shops getShopByID(int shop_id) {
		return entityManager.createQuery("from Shops where shop_id = :shop_id", Shops.class)
				.setParameter("shop_id", shop_id).getSingleResult();
	}

}
