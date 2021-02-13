package com.gabrielf.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.gabrielf.cursomc.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService service;
	
	@Bean
	public boolean instantiateDatabase() throws Exception {
		
		service.instantiateTestDatabase();
		return true;
	}

}
