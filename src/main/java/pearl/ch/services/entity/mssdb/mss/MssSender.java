package pearl.ch.services.entity.mssdb.mss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mss_sender")
@Data
@NoArgsConstructor
public class MssSender {
	
	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "shop_id")
	private int shop_id;

	@Column(name = "language_id")
	private int language_id;

	@Column(name = "email")
	private String email;

}
