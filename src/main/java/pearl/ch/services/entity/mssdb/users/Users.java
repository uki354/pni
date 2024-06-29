package pearl.ch.services.entity.mssdb.users;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class Users {
	
	@Id
	@Column(name = "users_id")
	private int users_id;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "first_name")
	private String first_name;

	@Column(name = "email")
	private String email;

	@Column(name = "crypted_password")
	private String crypted_password;

	@Column(name = "status")
	private int status;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;

	@Column(name = "last_login_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_login_date;

	@Column(name = "last_login_ip_address")
	private String last_login_ip_address;

	@Column(name = "last_token_value")
	private String last_token_value;

	@Column(name = "last_token_expire_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_token_expire_date;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private Collection<Roles> roles;


}
