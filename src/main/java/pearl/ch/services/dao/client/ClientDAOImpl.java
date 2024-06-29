package pearl.ch.services.dao.client;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.entity.dbch1.clients.Clients;

@Repository
@ReadTransactional
@RequiredArgsConstructor
public class ClientDAOImpl implements ClientDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManagerRead;

	@PersistenceContext(unitName = "dbch1")
	private EntityManager entityManagerDbch1;

	/**
	 * Retrieves the client's ID based on the provided client UUID and template ID.
	 *
	 * The method executes a native SQL query to find the client's ID from the
	 * 'mss_to_send' table in the database, based on the given client UUID and
	 * template ID.
	 *
	 * @param clientUUId The unique identifier (UUID) of the client.
	 * @param templateId The ID of the template associated with the client.
	 * @return The client's ID as a BigInteger value, or null if no matching client
	 *         is found.
	 */
	@Override
	public BigInteger getClientIdByClientUUId(String clientUUId, int templateId) {
		return   (BigInteger) entityManagerRead
				.createNativeQuery(
						"SELECT clients_id FROM mss_to_send WHERE client_uuid=:uuid AND template_id=:templateId")
				.setParameter("uuid", clientUUId).setParameter("templateId", templateId).getSingleResult();

	}

	@Override
	public Clients getRandomClient() {
		return entityManagerDbch1
				.createQuery("SELECT c FROM Clients c JOIN FETCH c.clientsAdditionalInfo ORDER BY RANDOM()",
						Clients.class)
				.setMaxResults(1).getSingleResult();
	}

	@Override
	public Clients getClientById(BigInteger clientId) {
		return entityManagerDbch1.createQuery("FROM Clients WHERE clients_id = :clients_id", Clients.class)
				.setParameter("clients_id", clientId).getSingleResult();

	}
}
