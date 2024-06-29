package pearl.ch.services.service.users;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.minidev.json.JSONObject;
import pearl.ch.services.entity.mssdb.users.Users;

public interface UsersService extends UserDetailsService {

	Users findByUserName(int userId);

	void updateUserDataAfterLogin(String username);

	JSONObject getUsersList();

	JSONObject editUserData(int userId, String firstName, String lastName, String email, int status);

	JSONObject getUserRoles(int userId);

	JSONObject setUserRoles(int userId, int[] roles);

	JSONObject removeUserRoles(int userId, int[] roles);

	JSONObject getNewUsers();

	JSONObject importNewUsers(int[] usersId);

	boolean isAdmin();

	String getUserEmail();

}
