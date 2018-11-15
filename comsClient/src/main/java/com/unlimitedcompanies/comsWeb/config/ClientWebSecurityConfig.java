package com.unlimitedcompanies.comsWeb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ClientWebSecurityConfig extends WebSecurityConfigurerAdapter
{	
	@Override
	public void configure(WebSecurity web)
	{
		web.ignoring()
				.antMatchers("/js/**")
				.antMatchers("/images/**")
				.antMatchers("/css/**");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.antMatcher("/**").authorizeRequests()
							  .anyRequest()
							  .permitAll()
			.and().logout().clearAuthentication(true)
						   .invalidateHttpSession(true)
						   .deleteCookies("JSESSIONID")
						   .logoutSuccessUrl("/")
			.and().csrf().disable();
	}
}
