package pearl.ch.services.service.mss.email;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void sendEmail(String to, String subject, String text, String emailFrom, String replyTo) {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) emailSender;
		Properties mailProps = mailSenderImpl.getJavaMailProperties();
		mailProps.put("mail.smtp.from", "<bouncer.swiss@pearl.ch>");
		mailSenderImpl.setJavaMailProperties(mailProps);

		try {

			helper.setTo(to);
			helper.setFrom(emailFrom);
			helper.setSubject(subject);
			helper.setText(text, true);
			helper.setReplyTo(replyTo);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		emailSender.send(message);
	}

	@Override
	public String getReplyTo(int shop, int langaugeId) {
		String replyTo;

		switch (shop) {
		case 1:
			if (langaugeId == 1 || langaugeId == 2) {
				replyTo = "serviceteam@pearl.ch";
			} else if (langaugeId == 6) {
				replyTo = "servizioassistenza@pearl.ch";
			} else if (langaugeId == 7) {
				replyTo = "serviceclients@pearl.ch";
			} else {
				replyTo = "serviceteam@pearl.ch";
			}
			break;
		case 2:
			replyTo = "info@casativo.ch";
			break;
		case 3:
			replyTo = "info@natura-punto.ch";
			break;
		default:
			replyTo = "serviceteam@pearl.ch";
		}
		return replyTo;
	}
}