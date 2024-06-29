package pearl.ch.services.service.mss.email;

public interface EmailService {
	
	void sendEmail(String to, String subject, String message, String mailFrom, String replyTo);
	
	String getReplyTo(int shop, int langaugeId);
}
