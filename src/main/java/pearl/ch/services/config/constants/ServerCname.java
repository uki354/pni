package pearl.ch.services.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import pearl.ch.services.entity.mssdb.shops.Shops;

@Configuration
@Getter
public class ServerCname {

	@Value("${server.cname.pearl}")
	public String serverCnamePearl;

	@Value("${server.cname.naturaPunto}")
	public String serverCnameNaturaPunto;

	@Value("${server.cname.casativo}")
	public String serverCnameCasativo;

	@Value("${server.cname.internal")
	public String serverCnameInternal;

	public String getCnameByShop(Shops shop) {
		switch (shop.getShop_id()) {
		case 1:
			return serverCnamePearl;
		case 2:
			return serverCnameCasativo;
		case 3:
			return serverCnameNaturaPunto;

		default:
			return null;
		}
	}

}
