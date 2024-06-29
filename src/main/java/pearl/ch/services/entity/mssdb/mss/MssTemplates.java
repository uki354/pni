package pearl.ch.services.entity.mssdb.mss;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import pearl.ch.services.enums.TemplateState;

@Entity
@Table(name = "mss_templates")
@Data
public class MssTemplates implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mss_template_id")
	private int mss_template_id;

	@Column(name = "language_id")
	private int language_id;

	@Column(name = "shop_id")
	private int shop_id;

	@Column(name = "mss_type_id")
	private int mss_type_id;

	@Column(name = "name")
	private String name;

	@Column(name = "mail_subject")
	private String mail_subject;

	@Column(name = "mail_from")
	private String mail_from;

	@Column(name = "coorders_url")
	private String coorders_url;

	@Column(name = "html_template")
	private String html_template;

	@Column(name = "json_template")
	private String json_template;
	
	@Column(name = "online_version_url")
	private String online_version_url;

	@Column(name = "filler_id")
	private Integer filler_id;
	
	@Column(name = "state")
	@Enumerated(EnumType.STRING)
	private TemplateState state;

	@Column(name = "cruser")
	private int cruser;

	@Column(name = "crdate", columnDefinition = "DATETIME")
	private LocalDateTime crdate;

	@Column(nullable = true, name = "upduser")
	private Integer upduser;

	@Column(name = "upddate", columnDefinition = "DATETIME")
	private LocalDateTime upddate;

}
