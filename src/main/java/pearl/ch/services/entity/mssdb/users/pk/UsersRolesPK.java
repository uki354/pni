package pearl.ch.services.entity.mssdb.users.pk;

import java.io.Serializable;
import java.util.Objects;

public class UsersRolesPK implements Serializable {
	private static final long serialVersionUID = 1L;

	private int users_id;
	private int roles_id;

	public UsersRolesPK() {
	}

	public UsersRolesPK(int users_id, int roles_id) {
		super();
		this.users_id = users_id;
		this.roles_id = roles_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getRoles_id() {
		return roles_id;
	}

	public void setRoles_id(int roles_id) {
		this.roles_id = roles_id;
	}

	@Override
	public int hashCode() {		
		return Objects.hash(users_id,roles_id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UsersRolesPK that = (UsersRolesPK) o;
		return Objects.equals(users_id, that.users_id) &&
				Objects.equals(roles_id, that.roles_id);
	
	}
}
