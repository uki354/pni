package pearl.ch.services.controller.users.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import pearl.ch.services.service.users.password.PasswordService;

@RestController
@CrossOrigin
public class PasswordController {
	
	@Autowired
	public PasswordService passwordService;
	
	/* 
	 * This function checks if the user's email exists in the database, and if it
	 * does, then sends a message to the provided email with a link for resetting
	 * the password; if it doesn't, it returns the error message.  
	 * */
	@PostMapping("/requestNewPassword")
	public ResponseEntity<?> requestNewPassword(String email) {

		JSONObject entity = new JSONObject();

		boolean checkUser = passwordService.checkIfUserExists(email);
		
		entity.put("checkUser", checkUser);
		
		return ResponseEntity.ok(entity);
	}
	
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(String token,String password){		
		
		return passwordService.updatePassword(token, password);
	}

}
