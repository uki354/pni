package pearl.ch.services.dto.mss.mssCore;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DataForMail {
	
	private BigInteger clientsId;
	private String mailTo;
	private String mailFrom;
	private String replyTo;
	private String subject;
	private String htmlTemplate;
	

}
