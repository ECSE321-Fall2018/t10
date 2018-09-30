package com.ecse321.team10.riderz.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecse321.team10.riderz.sql.MySQLJDBC;

@RestController
public class UsersController {
	
	@Autowired
	private MySQLJDBC sql;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger logger = LogManager.getLogger(RiderzController.class);
	@PostMapping("/addUser/{username}")
	public UserDto addUser(	@PathVariable("username") String username,
							@RequestParam String password,
							@RequestParam String email,
							@RequestParam String phone,
							@RequestParam String firstname,
							@RequestParam String lastName) {
		
	}
}
