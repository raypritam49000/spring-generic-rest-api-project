package com.generic.rest.api.project.secuirty;

import com.generic.rest.api.project.exception.CustomAccessDeniedHandler;
import com.generic.rest.api.project.exception.CustomAuthenticationEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public abstract class JsonWebTokenSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenSecurityConfig.class);
	private JsonWebTokenAuthenticationProvider authenticationProvider;

	private JsonWebTokenAuthenticationFilter jsonWebTokenFilter;


	@Autowired
	public void setAuthenticationProvider(JsonWebTokenAuthenticationProvider authenticationProvider){
		this.authenticationProvider = authenticationProvider;
	}


	@Lazy
	@Autowired
	public void setJsonWebTokenFilter(JsonWebTokenAuthenticationFilter jsonWebTokenFilter){
		this.jsonWebTokenFilter = jsonWebTokenFilter;
	}


	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);

		logger.info("common::configure().2..");
		http
				// disable CSRF, http basic, form login
				.csrf().disable() //
				.httpBasic().disable() //
				.formLogin().disable()
				// ReST is stateless, no sessions

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				.and()
				// return 403 when not authenticated
				.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
						.accessDeniedHandler(new CustomAccessDeniedHandler());
				//.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
						//.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

		// Let child classes set up authorization paths
		setupAuthorization(http);

		http.addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	protected abstract void setupAuthorization(HttpSecurity http) throws Exception;

}
