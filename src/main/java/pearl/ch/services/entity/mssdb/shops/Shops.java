package pearl.ch.services.entity.mssdb.shops;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "mss_shops")
@Data
public class Shops {
	
	@Id
	@Column(name = "shop_id")
	private int shop_id;
	
	@Column(name = "name")
	private String name;

}
