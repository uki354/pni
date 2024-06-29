package pearl.ch.services.dao.users.password;

import org.springframework.http.ResponseEntity;

public interface PasswordDAO {

	public boolean checkIfUserExists(String email);
	
	public ResponseEntity<?> updatePassword(String token,String password);
	
}
