package pearl.ch.services.entity.mssdb.mss.fillers;

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

@Entity
@Table(name = "mss_fillers")
@Data
public class MSSFillers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="filler_id")
	private Integer filler_id;
	
	@Column(name="name")
	private String  name;
	
	@Column(name = "shop_id")
	private int shop_id;
	
	@Column(name="language_id") 
	private Integer  language_id;
	
	@Column(name = "mss_type_id")
	private int mss_type_id;
	
	@Column(name="sql")
	private String  sql;
	
	@Column(name = "cruser")
	private int cruser;
	
	@Column(name="crdate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date crdate;
	
	@Column(name = "upduser")
	private Integer upduser;
	
	@Column(name="upddate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date upddate;

}

