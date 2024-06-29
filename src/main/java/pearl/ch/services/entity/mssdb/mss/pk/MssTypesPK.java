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
public class MssTypesPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "mss_type_id")
	private int mss_type_id;
	@Column(name = "shop_id")
	private int shop_id;

}
