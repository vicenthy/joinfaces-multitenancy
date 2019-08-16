package com.example.multitenancy.multitenancy.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordencoder;
	
	@Override
	protected void configure(HttpSecurity http) {
		try {
			http.csrf().disable();
			http
				.userDetailsService(userDetailsService())
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/**.xhtml").permitAll()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.authenticationProvider(new AutenticacaoFilter())
				.formLogin()
				.defaultSuccessUrl("/app/index.xhtml")
				.and()
				.logout()
				.logoutUrl("/sair")
				.deleteCookies("JSESSIONID");
		}
		
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}

}

	
	
	
	@Override
	protected UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();
		UserDetails user = User.builder()
						.password(passwordencoder.encode("admin"))
						.username("admin")
						.authorities("ROLE_ADMIN")
						.build();
		result.createUser( user);
		return result;
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	 return new BCryptPasswordEncoder();
	}
	

}
