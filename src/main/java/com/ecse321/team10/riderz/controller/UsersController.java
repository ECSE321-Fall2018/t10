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
import org.springframework.web.bind.annotation.PutMapping;
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
	public UserDto getUser(	@RequestParam String username) {
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
		return null;
	}

	//Note: Implemented basic authentication
	//todo is to better security
	@DeleteMapping("deleteUser")
	public void deleteUser(@RequestParam String username, @RequestParam String password){
		if(sql.connect() && sql.verifyLogin(username,password)){
			sql.deleteUser(username);
			sql.closeConnection();
		}
	}
	

	@PostMapping("login")
	public boolean verifyLogin( @RequestParam String username, @RequestParam String password) {
		if(sql.connect()){
			boolean login=sql.verifyLogin(username,password);
			sql.closeConnection();
			return login;
		}
		return false;						
	}

	@GetMapping("getPhone")
	public String getPhone( @RequestParam String username ){
		if(sql.connect()){
			
			String phone = sql.getPhone(username);
			sql.closeConnection();
			return phone;
		}
		return null;
	}

	@PutMapping("setPhone")
	public boolean setPhone( @RequestParam String username,@RequestParam String phone){
		if(sql.connect()){
			boolean numberChanged = sql.setPhone(username, phone);
			sql.closeConnection();
			return numberChanged;
		}
		return false;
	}

	@GetMapping("getEmail")
	public String getEmail( @RequestParam String username ){
		if(sql.connect()){
			String email = sql.getEmail(username);
			sql.closeConnection();
			return email;
		}
		return null;
	}

	@PutMapping("setEmail")
	public boolean setEmail(@RequestParam String username,@RequestParam String email ){
		if(sql.connect()){
			boolean emailChanged = sql.setEmail(username,email);
			sql.closeConnection();
			return emailChanged;
		}
		return false;
	}

	//Successfully changes password but can implement email verification later
	@PutMapping("setPassword")
	public boolean setPassword(@RequestParam String username,@RequestParam String password, @RequestParam String newPassword){
		if(sql.connect() && sql.verifyLogin(username,password)){
			boolean changedPass= sql.setPassword(username,newPassword);
			sql.closeConnection();
			return changedPass;
		}
		sql.closeConnection();
		return false;
	}



}
