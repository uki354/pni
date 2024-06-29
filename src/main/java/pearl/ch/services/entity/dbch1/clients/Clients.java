package pearl.ch.services.entity.dbch1.clients;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Clients {
	
	@Id
	@Column(name = "clients_id")
	private BigInteger clients_id;

	@Column(name = "gender")
	private String gender;

	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "last_name")
	private String last_name;

	@Column(name = "addition_1")
	private String addition_1;
	
	@Column(name = "addition_2")
	private String addition_2;
	
	@Column(name = "street")
	private String street;

	@Column(name = "zip_code")
	private int zip_code;
	
	@Column(name = "country_code")
	private String country_code;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mediacode")
	private Integer mediacode;

	@Column(name = "crdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date crdate;
	
	@Column(name = "upddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date upddate;
	
    @OneToOne
    @JoinColumn(name = "clients_id")
    private ClientsAdditionalInfo clientsAdditionalInfo;

}
