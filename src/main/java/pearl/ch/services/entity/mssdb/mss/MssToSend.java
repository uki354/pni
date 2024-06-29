package pearl.ch.services.entity.mssdb.mss;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "mss_to_send")
@Data
public class MssToSend implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mssToSendSeq")
	@SequenceGenerator(name = "mssToSendSeq", sequenceName = "mss_to_send_id_seq", allocationSize = 2000)
	private BigInteger id;
	
	@Column(name = "clients_id")
	private BigInteger clientId;
	
	@Column(name = "template_id")
	private int templateId;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "date_of_birth")
	private Date date_of_birth;
	
	@Column(name = "state")
	private int state;
	
	@Column(name = "send_time")
	private Date send_time;
	
	@Column(name = "client_uuid")
	private String clientUUId;
	
}



