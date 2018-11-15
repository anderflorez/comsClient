package com.unlimitedcompanies.comsWeb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.unlimitedcompanies.comsWeb"})
@EnableWebMvc
public class ClientWebConfiguration implements WebMvcConfigurer
{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}
}
