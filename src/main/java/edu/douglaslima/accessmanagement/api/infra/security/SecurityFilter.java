package edu.douglaslima.accessmanagement.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recoverToken(request);
		String username = tokenService.validateToken(token);
		if (username != null) {
			UserDetails user = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(userAuthentication);
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Recovers the token string from the request's header "Authorization".
	 * 
	 * @param request a {@code HttpServletRequest} object
	 * @return the token string if it is present, otherwise null
	 */
	private String recoverToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}

}
