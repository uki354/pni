package pearl.ch.services.service.client;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.dao.client.ClientDAO;
import pearl.ch.services.entity.dbch1.clients.Clients;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientDAO clientDao;

	/**
	 * Retrieves the client's ID based on the provided client UUID and template ID.
	 *
	 * The method queries the underlying data access object (clientDao) to find the
	 * client's ID corresponding to the given client UUID and template ID.
	 *
	 * @param clientUUId The unique identifier (UUID) of the client.
	 * @param templateId The ID of the template associated with the client.
	 * @return The client's ID as a BigInteger value, or null if no matching client
	 *         is found.
	 */
	@Override
	public BigInteger getClientIdByClientUUId(String clientUUId, int templateId) {
		return clientDao.getClientIdByClientUUId(clientUUId, templateId);
	}

	@Override
	public Clients getRandomClient() {
		return clientDao.getRandomClient();
	}

	@Override
	public String getClientsSalutation(BigInteger clientId, int languageId) {
		
		String salutation = "";
		
		if (clientId.signum() == 0) {
			switch (languageId) {
			case 2:
				salutation = "Guten Tag";
				break;
			case 6:
				salutation = "Buongiorno";
				break;
			case 7:
				salutation = "Bonjour";
				break;
			}
			return salutation;
		}

		Clients client = clientDao.getClientById(clientId);

		switch (languageId) {
		case 2:
			switch (client.getGender()) {
			case "m":
				salutation = "Sehr geehrter Herr " + client.getLast_name();
				break;
			case "f":
				salutation = "Sehr geehrte Frau " + client.getLast_name();
				break;
			case "c":
				salutation = "Guten Tag " + client.getLast_name();
				break;
			default:
				salutation = "Sehr geehrter Kunde / Sehr geehrte Kundin";
			}
			break;
		case 6:
			switch (client.getGender()) {
			case "m":
				salutation = "Egregio Signor " + client.getLast_name();
				break;
			case "f":
				salutation = "Gentile Signora " + client.getLast_name();
				break;
			case "c":
				salutation = "Buongiorno " + client.getLast_name();
				break;
			default:
				salutation = "Gentile Signor";
			}
			break;
		case 7:
			switch (client.getGender()) {
			case "m":
				salutation = "Cher Monsieur " + client.getLast_name();
				break;
			case "f":
				salutation = "Chère Madame " + client.getLast_name();
				break;
			case "c":
				salutation = "Bonjour " + client.getLast_name();
				break;
			default:
				salutation = "Chère cliente, cher client";
			}
			break;
		}

		return salutation;
	}

}
