package pearl.ch.services.entity.dbch1.users;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UsersDbch1 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "users_id")
	private int users_id;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "first_name")
	private String first_name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private byte[] password;
	
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
	private String last_token_expire_date;

}
