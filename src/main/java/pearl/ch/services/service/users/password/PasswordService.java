package pearl.ch.services.service.users.password;

import org.springframework.http.ResponseEntity;

public interface PasswordService {

	
	public boolean checkIfUserExists(String email);
	
	public ResponseEntity<?> updatePassword(String token,String password);
	
}
