package pearl.ch.services.model.jwt;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 2194780228688816816L;

	private String username;
	private String password;
}