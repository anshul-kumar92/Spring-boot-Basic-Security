package com.PosManagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@Order(2)

//extends WebSecurityConfigurerAdapter for customized Security
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter{

	
	//override that method
	private DigestAuthenticationEntryPoint getDigestEntryPoint()
	{
		DigestAuthenticationEntryPoint digestEntryPoint=new DigestAuthenticationEntryPoint();
		digestEntryPoint.setRealmName("admin-digest-realm");
		digestEntryPoint.setKey("somedigestkey");
		return digestEntryPoint;
	}
	@Bean
	public  PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}
	
	//override that method with credential
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication()
		     .withUser("digestsiva")
		        .password(passwordEncoder().encode("digestsecret"))
		         .roles("USER")
		         
		         .and()
		               .withUser("admin")
		                   .password(passwordEncoder().encode("adminsecret"))
		                	.roles("ADMIN");	 
}
	
	//Just call Super class
	@Bean
	public UserDetailsService userDetailsServiceBean() throws Exception
	{
		return super.userDetailsService();
	}
	
	private DigestAuthenticationFilter getDigestAuthfilter() throws Exception
	{
		DigestAuthenticationFilter digestfilter=new DigestAuthenticationFilter();
		
		digestfilter.setUserDetailsService(userDetailsServiceBean());
		
		digestfilter.setAuthenticationEntryPoint(getDigestEntryPoint());
		return digestfilter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.antMatcher("/admin/**")
		.addFilter(getDigestAuthfilter()).exceptionHandling()
		 .authenticationEntryPoint(getDigestEntryPoint())
		   .and().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
	}
}
