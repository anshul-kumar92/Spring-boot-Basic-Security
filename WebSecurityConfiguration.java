package com.PosManagement;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{ //extends WebSecurityConfigurerAdapter for customized Security
	//Basic Authentication
	protected void configure(HttpSecurity http) throws Exception //override that method
	{
		
		//Basic Authenicated
		/*http .authorizeRequests()
		       .anyRequest().authenticated()
		        .and().httpBasic();
		*/
		
		//Form Basic Security
		http.authorizeRequests()
		     .antMatchers("/mylogin","/h2-console").permitAll()
		      .anyRequest().authenticated()
		       .and()
		         .formLogin().loginPage("/mylogin").defaultSuccessUrl("/hello",true)
		         //Disable the CSRF
		         .and().csrf().disable()
		         ;
	}
	
	// If i want hide css in form
	@Override
	public void configure(WebSecurity web) throws Exception
	{
		web.ignoring().antMatchers("/css/**","/webjars/**");
	}
	
	@Autowired
	private DataSource datasource;
	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		
		DelegatingPasswordEncoder encoder=(DelegatingPasswordEncoder)PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.jdbcAuthentication()
		    .dataSource(datasource).passwordEncoder(getPasswordEncoder());
	}
	

}
