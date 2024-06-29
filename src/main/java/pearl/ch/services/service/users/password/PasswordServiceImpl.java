package pearl.ch.services.service.users.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pearl.ch.services.dao.users.password.PasswordDAO;

@Service
public class PasswordServiceImpl implements PasswordService {

	@Autowired
	private PasswordDAO passwordDAO;
	
	public boolean checkIfUserExists(String email) {
		
		return passwordDAO.checkIfUserExists(email);
		
	}
	
	public ResponseEntity<?> updatePassword(String token,String password){
		
		return passwordDAO.updatePassword(token, password);
	}
	
}
