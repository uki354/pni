package pearl.ch.services.controller.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import pearl.ch.services.config.security.jwt.JwtTokenUtil;
import pearl.ch.services.model.jwt.JwtRequest;
import pearl.ch.services.model.jwt.JwtResponse;
import pearl.ch.services.service.users.UsersService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UsersService usersService;

	@PostMapping("/authenticateTheUser")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		final String username = authenticationRequest.getUsername();
		final String password = authenticationRequest.getPassword();

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		

		final String token = jwtTokenUtil.generateToken(username, authentication.getAuthorities());

		usersService.updateUserDataAfterLogin(username);

		JSONObject entity = new JSONObject();
		entity.put("token", new JwtResponse(token));

		return ResponseEntity.ok(entity);
	}

	/**
	 * This Method is using from AWS to check if the Application is up and running
	 * 
	 * @return String "Application is up and running" and Http Code 200
	 */
	@GetMapping("/isAlive")
	public ResponseEntity<String> isAlive() {
		return ResponseEntity.ok("Application is up and running");
	}

}
