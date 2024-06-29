package pearl.ch.services.dao.mss.core;

import pearl.ch.services.dto.mss.mssCore.DataForMail;

public interface CoreDAO {

	boolean filling(int templateId);

	DataForMail prepareDataForMail(int templateId);

	boolean setSendState(String email, int templateId, int state);

}
