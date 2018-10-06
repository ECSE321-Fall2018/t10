package com.ecse321.team10.riderz.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.sql.MySQLJDBC;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestDriverController {
	@Autowired
    private MockMvc mockMvc;
	MySQLJDBC sql = new MySQLJDBC();
	
	@Before
	public void setup() {
		User user = new User("unitTest-mei", "uNiTtEsTmEi", "mei.q@mail.ca", "1234445555", "Mei", "Test");
		sql.connect();
		sql.insertUser(user);
		sql.insertDriver("unitTest-mei");
		sql.closeConnection();
	}
	
	@After
	public void tearDown() {
		sql.connect();
		sql.deleteDriver("unitTest-mei");
		sql.deleteUser("unitTest-mei");
		sql.closeConnection();
	}
	
	@Test
	public void testGetDriverByUsername() throws Exception {
		 this.mockMvc.perform(get("/driver?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("{\"operator\":\"unitTest-mei\",\"rating\":0.0,\"personsRated\":0,\"tripsCompleted\":0}")));
	}
	
	@Test
	public void testGetAllDrivers() throws Exception {
		 this.mockMvc.perform(get("/driver/all")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("{\"operator\":\"unitTest-mei\",\"rating\":0.0,\"personsRated\":0,\"tripsCompleted\":0}")));
	}
	
	@Test
	public void testGetDriverRating() throws Exception {
		 this.mockMvc.perform(get("/driver/rating?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("0.0")));
	}
	
	@Test
	public void testUpdateDriverRating() throws Exception {
		 this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/rating?operator=unitTest-mei&rating=4.0")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("true")));
		 //verify
		 this.mockMvc.perform(get("/driver/rating?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("4.0")));
	}
	
	@Test
	public void testGetDriverRatingFrequency() throws Exception {
		 this.mockMvc.perform(get("/driver/rating/frequency?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("0")));
	}
	
	@Test
	public void testIncrementDriverRatingFrequency() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/rating/frequency?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("true")));
		
		 this.mockMvc.perform(get("/driver/rating/frequency?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("1")));
	}
	
	@Test
	public void testGetDriverTripsCompleted() throws Exception {
		 this.mockMvc.perform(get("/driver/trip?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("0")));
	}
	
	@Test
	public void testIncrementDriverTripsCompleted() throws Exception {
		 this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/trip?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("true")));
		 
		 this.mockMvc.perform(get("/driver/trip?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("1")));
	}
	
	@Test
	public void testNewAndDeleteDriver() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/delete?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("true")));
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/new?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("true")));
	}	
}
