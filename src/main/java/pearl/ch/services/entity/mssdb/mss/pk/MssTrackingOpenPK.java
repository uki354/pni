package pearl.ch.services.entity.mssdb.mss.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MssTrackingOpenPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "client_uuid")
	private String clientUUId;
	
	@Column(name = "template_id")
	private int templateId;

}
