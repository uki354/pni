package pearl.ch.services.dao.users.password;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONObject;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.config.constants.ServerCname;
import pearl.ch.services.entity.mssdb.users.Users;
import pearl.ch.services.service.mss.email.EmailService;

@Repository
@WriteTransactional
public class PasswordDAOImpl implements PasswordDAO {
	
	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ServerCname serverCname;

	public boolean checkIfUserExists(String email) {
		
		boolean checkUser = false;

		try(Session session = entityManagerWrite.unwrap(Session.class);) {
			// Convert the provided email to the lower case.
			email.toLowerCase();

			// Assign a random universally unique identifier to a token.
			String token = UUID.randomUUID().toString();

			// Set the expiration date to last 24 hours.
			Date tokenDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1440));

			// Check if the email exists inside the 'users' table.
			Query findEmail = session.createQuery("UPDATE Users SET last_token_value = '" + token + "', "
					+ "last_token_expire_date = '" + tokenDate + "' WHERE email = '" + email + "'");
			int rowsUpdated = findEmail.executeUpdate();

			// If it exists, send an email with a link.
			if (rowsUpdated == 1) {
				String subject = "Mailingsystem Passwort zurücksetzen";
				String message = "Klicken Sie auf <a href=\"" + serverCname.getServerCnameInternal()
						+ "set-password?token=" + token + "\">den Link</a>, um Ihr Passwort neu zu setzen";

				emailService.sendEmail(email, subject, message, email, email);
				checkUser = true;
			}

			// If it doesn't exist, return an error message.
			else {
				checkUser = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			checkUser = false;
		}
		return checkUser;
	}

	/*
	 * This function updates the user's password with the provided password. The
	 * password is stored inside the 'users' table using the BCrypt hashing
	 * algorithm.
	 */
	public ResponseEntity<?> updatePassword(String token, String password) {

		Users users = getUserByToken(token);

		JSONObject entity = new JSONObject();

		if (users != null) {
			try {
				// Create a new BCryptPasswordEncoder object.
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

				// Set the encrypted password from the user's password in BCrypt format.
				String cryptedPassword = passwordEncoder.encode(password);

				// Update user's token and password.
				users.setLast_token_value(token + "?expired");
				users.setCrypted_password(cryptedPassword);

				int userid = users.getUsers_id();

				entity.put("isPasswordChanged", "true");
				entity.put("username", userid);

			} catch (Exception ex) {
				ex.printStackTrace();

			}

		} else {
			entity.put("isPasswordChanged", "false");
			entity.put("tokenExpired", "true");
		}

		return ResponseEntity.ok(entity);
	}

	/*
	 * This function checks if the token value is valid and if the token date didn't
	 * expire, and then lets the user change its password; otherwise, it returns the
	 * user to the 'forgotPassword.jsp' with one of the error messages.
	 */

	private Users getUserByToken(String token) {
		
		Users users = null;

		try (Session session = entityManagerWrite.unwrap(Session.class);){
			// Query and get the User object into the 'theUser' variable.
			users = session.createQuery("FROM Users WHERE last_token_value = '" + token + "'", Users.class)
					.getSingleResult();

			// Check if the user with that token value exists.
			if (users != null) {
				Date tokenDate = users.getLast_token_expire_date();
				Date now = new Date();

				// Check if the token date expired.
				if (now.after(tokenDate)) {
					users = null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			users = null;
		}

		return users;
	}
}
