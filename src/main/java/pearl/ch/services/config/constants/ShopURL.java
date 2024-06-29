package pearl.ch.services.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class ShopURL {

	private String pearlURL;

	@Value("${spring.url.pearl.ch}")
	public void setPearlURL(String pearlURL) {
		this.pearlURL = pearlURL;
	}

	private String casativoURL;

	@Value("${spring.url.casativo.ch}")
	public void setCasativoURL(String casativoURL) {
		this.casativoURL = casativoURL;
	}

	private String naturaPuntoURL;

	@Value("${spring.url.natura-punto.ch}")
	public void setNaturaPuntoURL(String naturaPuntoURL) {
		this.naturaPuntoURL = naturaPuntoURL;
	}

	public String getShopURLByShopId(int shopId) {
		switch (shopId) {
		case 1:
			return pearlURL;
		case 2:
			return casativoURL;
		case 3:
			return naturaPuntoURL;

		default:
			return null;
		}
	}

}
