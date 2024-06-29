package pearl.ch.services.entity.dbch1.clients;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients_additional_info")
@Data
@NoArgsConstructor
public class ClientsAdditionalInfo {
	
	
	@Id
	@Column(name = "clients_id")
	private BigInteger clients_id;

	@Column(name = "language")
	private String language;
	
	@Column(name = "date_of_birth")
	private Date date_of_birth;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "free_flag")
	private String free_flag;
	
	@Column(name = "bearbg")
	private String bearbg;
	
	@Column(name = "dubious")
	private String dubious;
	
	@Column(name = "email_for_invoice")
	private String email_for_invoice;
	
}
