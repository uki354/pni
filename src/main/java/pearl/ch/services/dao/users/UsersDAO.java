package pearl.ch.services.dao.users;

import org.springframework.security.core.userdetails.UserDetails;

import net.minidev.json.JSONObject;
import pearl.ch.services.entity.mssdb.users.Users;

public interface UsersDAO {

	Users findByUserName(int userId);

	UserDetails loadUserByUsername(String username);

	void updateUserDataAfterLogin(String username);

	JSONObject getUsersList();

	JSONObject editUserData(int userId, String firstName, String lastName, String email, int status);

	JSONObject getUserRoles(int userId);

	JSONObject setUserRoles(int userId, int[] roles);

	JSONObject removeUserRoles(int userId, int[] roles);

	JSONObject getNewUsers();

	JSONObject importNewUsers(int[] usersId);

	boolean isAdmin(int userId);

	String getUserEmailById(int id);

}
