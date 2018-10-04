package com.ecse321.team10.riderz.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecse321.team10.riderz.dto.UserDto;
import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.sql.MySQLJDBC;

@RestController
@RequestMapping("users")
public class UsersController {
	
	@Autowired
	private MySQLJDBC sql;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private UserDto convertToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}
	
	private static final Logger logger = LogManager.getLogger(RiderzController.class);
	
	@PostMapping("addUser")
	public UserDto addUser(	@RequestParam String username,
							@RequestParam String password,
							@RequestParam String email,
							@RequestParam String phone,
							@RequestParam String firstName,
							@RequestParam String lastName) {
		User user = new User(username, password, email, phone, firstName, lastName);
		if(sql.connect() && sql.insertUser(user)) {
			sql.closeConnection();
			return convertToDto(user);
		}else{
			sql.closeConnection();
			return null;
		}

	}
	
	@GetMapping("getUser")
	public UserDto getUser(	@RequestParam("username") String username) {
		if(sql.connect()){
			User user = sql.getUserByUsername(username);
			sql.closeConnection();
			return convertToDto(user);
		}
		sql.closeConnection();
		return null;
	}
	
	@GetMapping("getAllUsers")
	public List<UserDto> getAllUser() {
		if(sql.connect()){
			List<UserDto> users = new ArrayList<UserDto>();
			for(User user : sql.getAllUsers()){
				users.add(convertToDto(user));
			}
			sql.closeConnection();
			return users;
		}
		else{
			sql.closeConnection();
			return null;
		}
	}

	//Note: Will need a verification to allow this to happen
	//otherwise anyone can just delete user via url
	@DeleteMapping("deleteUser")
	public void deleteUser(@RequestParam("username") String username){
		if(sql.connect()){
			sql.deleteUser(username);
			sql.closeConnection();
		}
	}

}
