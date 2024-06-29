package pearl.ch.services.entity.mssdb.mss.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MssTrackingClickedPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "client_uuid")
	private String clientUUId;
	
	@Column(name = "template_id")	
	private int templateId;
	
	private String link;

}
