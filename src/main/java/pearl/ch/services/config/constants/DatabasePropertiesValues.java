package pearl.ch.services.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabasePropertiesValues {

	public static String urlDbch1;

	@Value("${spring.datasource.write.jdbc-url}")
	public void setUrlDbch1(String urlDbch1) {
		DatabasePropertiesValues.urlDbch1 = urlDbch1;
	}

	public static String usernameDbch1;

	@Value("${spring.datasource.write.username}")
	public void setUsernameDbch1(String usernameDbch1) {
		DatabasePropertiesValues.usernameDbch1 = usernameDbch1;
	}

	public static String passwordDbch1;

	@Value("${spring.datasource.write.password}")
	public void setPasswordDbch1(String passwordDbch1) {
		DatabasePropertiesValues.passwordDbch1 = passwordDbch1;
	}

}
