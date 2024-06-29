package pearl.ch.services.controller.users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import pearl.ch.services.service.users.UsersService;

@RestController
@CrossOrigin
@RequestMapping("users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@RequestMapping("/getUsersList")
	public @ResponseBody ResponseEntity<?> getUsersList() {

		JSONObject entity = usersService.getUsersList();

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/editUser")
	public @ResponseBody ResponseEntity<?> getEditUserData(@RequestBody Map<String, ?> usersData) {

		JSONObject jsonUsersData = new JSONObject(usersData);

		JSONObject entity = usersService.editUserData(Integer.parseInt(jsonUsersData.getAsString("userId")),
				jsonUsersData.getAsString("firstName"), jsonUsersData.getAsString("lastName"),
				jsonUsersData.getAsString("email"), Integer.parseInt(jsonUsersData.getAsString("status")));

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/getUserRoles")
	public @ResponseBody ResponseEntity<?> getUserRoles(@RequestParam("userId") int userId) {

		JSONObject entity = usersService.getUserRoles(userId);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/setUserRoles")
	public @ResponseBody ResponseEntity<?> setUserRoles(@RequestParam("userId") int userId,
			@RequestParam(value = "roles[]") int[] roles) {

		JSONObject entity = usersService.setUserRoles(userId, roles);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/removeUserRoles")
	public @ResponseBody ResponseEntity<?> removeUserRoles(@RequestParam("userId") int userId,
			@RequestParam(value = "roles[]") int[] roles) {

		JSONObject entity = usersService.removeUserRoles(userId, roles);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/getNewUsers")
	public @ResponseBody ResponseEntity<?> getNewUsers() {

		JSONObject entity = usersService.getNewUsers();

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/importNewUsers")
	public @ResponseBody ResponseEntity<?> importNewUsers(@RequestParam(value = "usersId[]") int[] usersId) {

		JSONObject entity = usersService.importNewUsers(usersId);

		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	@RequestMapping("/is-admin")
	public @ResponseBody ResponseEntity<?> isAdmin() {
		JSONObject entity = new JSONObject();
		boolean isAdmin = usersService.isAdmin();
		entity.put("isAdmin", isAdmin);
		return new ResponseEntity<Object>(entity, HttpStatus.OK);

	}
	
	@GetMapping("/getUserEmail")
	public String getUserEmail() {
		return usersService.getUserEmail();
	}

}
