package pearl.ch.services.config.security.jwt;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String requestTokenHeaderString = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		if (requestTokenHeaderString != null && requestTokenHeaderString.startsWith("Bearer ")) {
			jwtToken = requestTokenHeaderString.substring(7);

			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			} catch (IllegalArgumentException | ExpiredJwtException ex) {
				ex.printStackTrace();
			}
		}

		/* Once we get the token, validate it. */
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// UserDetails userDetails =
			// this.userDetailsService.loadUserByUsername(username);

			final Collection<SimpleGrantedAuthority> authorities = jwtTokenUtil.getAuthoritiesFromToken(jwtToken);
			/*
			 * If token is valid, configure Spring Security to manually set the
			 * authentication.
			 */

			final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
					authorities);

			/*
			 * After setting the Authentication in the context, we specify that the current
			 * user is authenticated. So it passes the Spring Security Configuration
			 * successfully.
			 */
			SecurityContextHolder.getContext().setAuthentication(token);

		}

		filterChain.doFilter(request, response);
	}

}