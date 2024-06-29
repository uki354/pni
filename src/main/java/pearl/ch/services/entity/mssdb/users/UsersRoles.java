package pearl.ch.services.entity.mssdb.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import pearl.ch.services.entity.mssdb.users.pk.UsersRolesPK;


@Entity
@Table(name = "users_roles")
@IdClass(UsersRolesPK.class)
@Data
@NoArgsConstructor
public class UsersRoles {

	
	@Id
	@Column(name = "users_id")
	private int users_id;

	@Id
	@Column(name = "roles_id")
	private int roles_id;
	
}
