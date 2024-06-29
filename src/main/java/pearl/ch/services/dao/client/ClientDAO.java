package pearl.ch.services.dao.client;

import java.math.BigInteger;

import pearl.ch.services.entity.dbch1.clients.Clients;

public interface ClientDAO {
	
	Clients getRandomClient();
	
	Clients getClientById(BigInteger clientId);

	BigInteger getClientIdByClientUUId(String clientUUId, int templateId);

}
