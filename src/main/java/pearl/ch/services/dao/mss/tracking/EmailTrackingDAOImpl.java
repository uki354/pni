package pearl.ch.services.dao.mss.tracking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.entity.mssdb.mss.MssTrackingClicked;
import pearl.ch.services.entity.mssdb.mss.MssTrackingOpen;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingOpenPK;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingClickedPK;

@Repository
public class EmailTrackingDAOImpl implements EmailTrackingDAO {

	@PersistenceContext(unitName = "mssRead")
    private EntityManager entityManager;
	
	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

    /**
     * Saves the EmailTrackingOpen data by merging it into the persistent storage.
     * <p>
     * The method takes an instance of the EmailTrackingOpen class and persists it into the database.
     * If an entity with the same identifier already exists in the database, it will be updated (merged)
     * with the values from the provided instance. If no entity with the same identifier is found,
     * a new record will be created in the database.
     * <p>
     * Note: The method uses EntityManager to interact with the underlying persistence context.
     * After the merge operation, the changes are synchronized to the database immediately
     * using entityManager.flush().
     *
     * @param emailTrackingOpen The EmailTrackingOpen instance to be saved or updated.
     * @throws javax.persistence.PersistenceException if an error occurs while interacting with the persistence context.
     * @see MssTrackingOpen
     * @see EntityManager
     */
    @Override
    @WriteTransactional
    public void saveEmailDataOpen(MssTrackingOpen emailTrackingOpen) {
    	entityManagerWrite.merge(emailTrackingOpen);
    	entityManagerWrite.flush();
    }


    /**
     * Retrieves the EmailTrackingOpen entity from the persistent storage based on the given MssToSendPK.
     * <p>
     * The method uses the provided MssToSendPK to search for an EmailTrackingOpen entity in the database.
     * If the entity with the specified identifier exists in the database, it is returned by the method.
     * If no matching entity is found, the method returns null.
     *
     * @param mssToSendPK The primary key used to search for the EmailTrackingOpen entity.
     * @return The EmailTrackingOpen entity corresponding to the provided MssToSendPK, or null if not found.
     * @throws javax.persistence.PersistenceException if an error occurs while interacting with the persistence context.
     */
    @ReadTransactional
    public MssTrackingOpen getEmailTrackingOpen(MssTrackingOpenPK mssTrackingOpenPK) {
        return entityManager.find(MssTrackingOpen.class, mssTrackingOpenPK);
    }


    /**
     * Saves the EmailRedirectTracking data by merging it into the persistent storage.
     * <p>
     * The method takes an instance of the EmailRedirectTracking class and persists it into the database.
     * If an entity with the same identifier already exists in the database, it will be updated (merged)
     * with the values from the provided instance. If no entity with the same identifier is found,
     * a new record will be created in the database.
     * <p>
     * Note: The method uses EntityManager to interact with the underlying persistence context.
     * After the merge operation, the changes are synchronized to the database immediately
     * using entityManager.flush().
     *
     * @param emailRedirectTracking The EmailRedirectTracking instance to be saved or updated.
     * @throws javax.persistence.PersistenceException if an error occurs while interacting with the persistence context.
     * @see MssTrackingClicked
     * @see EntityManager
     */
    @Override
    @WriteTransactional
    public void saveEmailRedirect(MssTrackingClicked emailRedirectTracking) {
    	entityManagerWrite.merge(emailRedirectTracking);
    	entityManagerWrite.flush();

    }


    /**
     * Retrieves the EmailRedirectTracking entity from the persistent storage based on the given EmailRedirectPK.
     * <p>
     * The method uses the provided EmailRedirectPK to search for an EmailRedirectTracking entity in the database.
     * If the entity with the specified identifier exists in the database, it is returned by the method.
     * If no matching entity is found, the method returns null.
     *
     * @param pk The primary key used to search for the EmailRedirectTracking entity.
     * @return The EmailRedirectTracking entity corresponding to the provided EmailRedirectPK, or null if not found.
     * @throws javax.persistence.PersistenceException if an error occurs while interacting with the persistence context.
     */
    @Override
    @ReadTransactional
    public MssTrackingClicked getEmailRedirectTracking(MssTrackingClickedPK pk) {
        return entityManager.find(MssTrackingClicked.class, pk);
    }


}
