package pearl.ch.services.dao.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONObject;
import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.entity.dbch1.users.UsersDbch1;
import pearl.ch.services.entity.mssdb.users.Roles;
import pearl.ch.services.entity.mssdb.users.Users;

@Repository
public class UsersDAOImpl implements UsersDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;

	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	@PersistenceContext(unitName = "dbch1")
	private EntityManager entityManagerDbch1;

	@Override
	@ReadTransactional
	public Users findByUserName(int userId) {
		return entityManager
				.createQuery("SELECT u from Users u JOIN FETCH u.roles WHERE u.users_id = :userId", Users.class)
				.setParameter("userId", userId).getResultStream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	}

	@Override
	public UserDetails loadUserByUsername(String username) {

		Users user = findByUserName(Integer.parseInt(username));
		return new User(username, user.getCrypted_password(), mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	@WriteTransactional
	public void updateUserDataAfterLogin(String username) {
		entityManager.createQuery("UPDATE Users u SET u.last_login_date = :date WHERE u.users_id = :username")
				.setParameter("username", Integer.parseInt(username)).setParameter("date", new Date()).executeUpdate();
	}

	@ReadTransactional
	public JSONObject getUsersList() {

		JSONObject jsonRespond = new JSONObject();

		List<Users> users = new ArrayList<Users>();

		try (Session session = entityManager.unwrap(Session.class);) {

			users = session.createQuery("FROM Users ORDER BY users_id ASC", Users.class).getResultList();

			jsonRespond.put("users", users);

		} catch (Exception exc) {
			exc.printStackTrace();

		}

		return jsonRespond;
	}

	@WriteTransactional
	public JSONObject editUserData(int userId, String firstName, String lastName, String email, int status) {

		JSONObject jsonRespond = new JSONObject();
		String response = "";

		try (Session session = entityManagerWrite.unwrap(Session.class);) {

			Users user = session.get(Users.class, userId);

			user.setFirst_name(firstName);
			user.setLast_name(lastName);
			user.setEmail(email);
			user.setStatus(status);

			session.update(user);

			response = "true";
			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;
	}

	@ReadTransactional
	public JSONObject getUserRoles(int userId) {

		JSONObject jsonRespond = new JSONObject();
		String response = "";

		List<Roles> availableRoles;
		List<Roles> assignedRoles;

		try (Session session = entityManager.unwrap(Session.class);) {

			Users selectedUser = session.get(Users.class, userId);

			jsonRespond.put("selectedUser", selectedUser);

			availableRoles = session
					.createQuery("FROM Roles WHERE roles_id NOT IN "
							+ "(SELECT ur.roles_id FROM Users u JOIN u.roles ur WHERE u.users_id = :userId)", Roles.class)
					.setParameter("userId", userId).getResultList();
			
			System.out.println(availableRoles);

			jsonRespond.put("availableRoles", availableRoles);

			assignedRoles = new ArrayList<>(selectedUser.getRoles());

			jsonRespond.put("assignedRoles", assignedRoles);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;
	}

	@WriteTransactional
	public JSONObject setUserRoles(int userId, int[] roles) {

		JSONObject jsonRespond = new JSONObject();
		String response = "";
		

		try (Session session = entityManagerWrite.unwrap(Session.class);) {
			
			Users user = session.find(Users.class, userId);

			for (int role : roles) {
				Roles roleProxy = session.getReference(Roles.class, role);				
				user.getRoles().add(roleProxy);
			}			
			
			session.save(user);


			response = "true";
			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;
	}

	@WriteTransactional
	public JSONObject removeUserRoles(int userId, int[] roles) {

		JSONObject jsonRespond = new JSONObject();
		String response = "";


		try (Session session = entityManagerWrite.unwrap(Session.class);) {			
			
			Users user = session.find(Users.class, userId);			
			
			Set<Roles> rolesToRemove = Arrays.stream(roles)
                    .mapToObj(roleId -> session.getReference(Roles.class, roleId))
                    .collect(Collectors.toSet());		
			
			user.getRoles().removeAll(rolesToRemove);			
			
			session.update(user);

			response = "true";
			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			response = "false";
			jsonRespond.put("response", response);

		}

		return jsonRespond;
	}

	@ReadTransactional
	public JSONObject getNewUsers() {

		JSONObject jsonRespond = new JSONObject();

		List<Users> usersMssdb;
		List<UsersDbch1> dbch1Users;

		try (Session session = entityManager.unwrap(Session.class);
				Session sessionDbch1 = entityManagerDbch1.unwrap(Session.class);) {

			usersMssdb = session.createQuery("FROM Users", Users.class).getResultList();
			dbch1Users = sessionDbch1.createQuery("FROM UsersDbch1 WHERE status = 1", UsersDbch1.class).getResultList();

			List<UsersDbch1> newUsers = dbch1Users.stream()
					.filter(user -> usersMssdb.stream().noneMatch(u -> u.getUsers_id() == user.getUsers_id()))
					.collect(Collectors.toList());

			jsonRespond.put("newUsers", newUsers);

		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return jsonRespond;
	}

	@WriteTransactional
	public JSONObject importNewUsers(int[] usersId) {

		String response = "";

		JSONObject jsonRespond = new JSONObject();

		try (Session session = entityManagerWrite.unwrap(Session.class);
				Session sessionDbch1 = entityManagerDbch1.unwrap(Session.class);) {

			for (int userId : usersId) {

				UsersDbch1 userDbch1 = sessionDbch1.get(UsersDbch1.class, userId);
				
				Users newUser = new Users();

				String password = userDbch1.getCrypted_password();
				password = password.replace("{bcrypt}", "");

				newUser.setUsers_id(userId);
				newUser.setFirst_name(userDbch1.getFirst_name());
				newUser.setLast_name(userDbch1.getLast_name());
				newUser.setEmail(userDbch1.getEmail());
				newUser.setStatus(1);
				newUser.setCrypted_password(password);
				newUser.setCreate_date(new Date());

				session.save(newUser);

			}

			response = "true";
			jsonRespond.put("response", response);

		} catch (Exception exc) {
			exc.printStackTrace();

			jsonRespond.put("response", response);
		}

		return jsonRespond;
	}
	
	
	

	@Override
	@ReadTransactional
	public String getUserEmailById(int id) {
		return entityManager.createQuery("SELECT u.email FROM Users u WHERE u.users_id = :id", String.class)
				.setParameter("id", id).getResultStream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(
			Collection<pearl.ch.services.entity.mssdb.users.Roles> collection) {
		return collection.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
