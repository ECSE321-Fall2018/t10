package com.ecse321.team10.riderz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ecse321.team10.riderz.sql.MySQLJDBC;

@SpringBootApplication
public class RiderzApplication {
	private MySQLJDBC sql;
	public static void main(String[] args) {
		SpringApplication.run(RiderzApplication.class, args);
	}
	
	@Bean
	public MySQLJDBC jdbcManager() {
		sql = new MySQLJDBC();
		return sql;
	}
}
