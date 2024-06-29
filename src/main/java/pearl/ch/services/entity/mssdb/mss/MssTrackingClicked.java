package pearl.ch.services.entity.mssdb.mss;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pearl.ch.services.entity.mssdb.mss.pk.MssTrackingClickedPK;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "mss_tracking_clicked")
public class MssTrackingClicked {
	
	@Embedded
	@Id
	private MssTrackingClickedPK pk;
	
	@Column(name = "clients_id")
	private BigInteger clientId;

	@Column(name = "user_agent")
	private String userAgent;

	@Column(name = "open_at")
	private LocalDateTime openAt;

	@Column(name = "last_open_at")
	private LocalDateTime lastOpenAt;

	private String os;

	private String device;

	private int count;

}
