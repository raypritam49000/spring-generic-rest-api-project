package com.generic.rest.api.project.secuirty;

import com.generic.rest.api.project.jsonwebtoken.AuthTokenDetailsDTO;
import com.generic.rest.api.project.jsonwebtoken.JsonWebTokenUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonWebTokenAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenAuthenticationProvider.class);

	private static Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	public JsonWebTokenAuthenticationProvider() {

	}
	public static String getActiveProfile() {
		return env != null ? env.getActiveProfiles()[0] : null;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticatedUser = null;
		if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
				&& authentication.getPrincipal() != null) {
			String tokenHeader = (String) authentication.getPrincipal();
			UserDetails userDetails = null;
			try {
				userDetails = parseToken(tokenHeader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (userDetails != null) {
				authenticatedUser = new JsonWebTokenAuthentication(userDetails, tokenHeader);
			}
		} else {
			// It is already a JsonWebTokenAuthentication
			authenticatedUser = authentication;
		}
		return authenticatedUser;
	}

	private UserDetails parseToken(String tokenHeader) {
		UserDetails principal = null;
		AuthTokenDetailsDTO authTokenDetails = JsonWebTokenUtility.parseAndValidate(tokenHeader);

		if (authTokenDetails != null) {
			List<GrantedAuthority> authorities = authTokenDetails.getGrantedAuthorities().stream()
					.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			principal = new User(authTokenDetails.getEmail(), "", authorities);
		}

		return principal;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
				|| authentication.isAssignableFrom(JsonWebTokenAuthentication.class);
	}

}
