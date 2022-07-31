package com.example.bankingsystem_dbconnection.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bankingsystem_dbconnection.service.UserControlService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private JwtRequestFilter jwtRequestFilter;	

	private UserControlService userControlService;

	@Autowired
	public JwtSecurityConfig(UserControlService userControlService) {
		this.userControlService = userControlService;
	}

	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userControlService).passwordEncoder(passwordEncoder());
	}
	@Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	//Configuring authorize for HTTP Methods with roles
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers("/auth")
		.permitAll()
		.antMatchers(HttpMethod.DELETE, "/accounts/{id}").hasAuthority("REMOVE_ACCOUNT")
		.antMatchers(HttpMethod.POST, "/accounts").hasAuthority("CREATE_ACCOUNT")	
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
}