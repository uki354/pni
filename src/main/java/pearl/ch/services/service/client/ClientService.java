package pearl.ch.services.service.client;

import java.math.BigInteger;

import pearl.ch.services.entity.dbch1.clients.Clients;

public interface ClientService {
	
	Clients getRandomClient();
	
	String getClientsSalutation(BigInteger clientId, int language);
	
	BigInteger getClientIdByClientUUId(String clientUUId, int templateId);

}
