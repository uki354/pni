package pearl.ch.services.entity.mssdb.mss;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import pearl.ch.services.entity.mssdb.mss.pk.MssTypesPK;

@Entity
@Data
@Table(name = "mss_types")
public class MssTypes implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MssTypesPK mssTypesPK;

	@Column(name = "name")
	private String name;

}
