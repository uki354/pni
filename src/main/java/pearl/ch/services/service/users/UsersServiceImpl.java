package pearl.ch.services.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;
import pearl.ch.services.dao.users.UsersDAO;
import pearl.ch.services.entity.mssdb.users.Users;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersDAO usersDAO;

	@Override
	public Users findByUserName(int userId) {
		return usersDAO.findByUserName(userId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usersDAO.loadUserByUsername(username);
	}

	@Override
	public void updateUserDataAfterLogin(String username) {
		usersDAO.updateUserDataAfterLogin(username);

	}

	public JSONObject getUsersList() {
		return usersDAO.getUsersList();
	}

	public JSONObject editUserData(int userId, String firstName, String lastName, String email, int status) {
		return usersDAO.editUserData(userId, firstName, lastName, email, status);
	}

	public JSONObject getUserRoles(int userId) {
		return usersDAO.getUserRoles(userId);
	}

	public JSONObject setUserRoles(int userId, int[] roles) {
		return usersDAO.setUserRoles(userId, roles);
	}

	public JSONObject removeUserRoles(int userId, int[] roles) {
		return usersDAO.removeUserRoles(userId, roles);
	}

	public JSONObject getNewUsers() {
		return usersDAO.getNewUsers();
	}

	public JSONObject importNewUsers(int[] usersId) {
		return usersDAO.importNewUsers(usersId);
	}

	@Override
	public boolean isAdmin() {
		return usersDAO.isAdmin(getAuthenticatedUser());
	}

	@Override
	public String getUserEmail() { 
		return usersDAO.getUserEmailById(getAuthenticatedUser());
		
	}
	
	
	private int getAuthenticatedUser() {
		return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	

}
