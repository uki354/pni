package pearl.ch.services.dao.mss.languages;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.entity.mssdb.languages.Languages;

@Repository
@ReadTransactional
public class LanguagesDAOImpl implements LanguagesDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;

	@Override
	public List<Languages> getLanguages() {

		return entityManager.createQuery("from Languages", Languages.class).getResultList();
	}

}
