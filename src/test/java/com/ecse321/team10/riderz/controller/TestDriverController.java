package com.ecse321.team10.riderz.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    private static final Logger logger = LogManager.getLogger(TestTripController.class);

	@Autowired
    private MockMvc mockMvc;
	MySQLJDBC sql = new MySQLJDBC();
	
	@BeforeClass
    public static void setupClass() {
        logger.info("************** Starting Driver Controller Tests **************");
    }

    @AfterClass
    public static void teardownClass() {
        logger.info("************** Ending Driver Controller Tests **************");
    }
	
	@Before
	public void setup() {
		User user = new User("unitTest-mei", "uNiTtEsTmEi", "mei.q@mail.ca", "1234445555", "Mei", "Test");
		sql.connect();
		sql.purgeDatabase();
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
	public void testGetAllDriversWithFilter() throws Exception {
		 sql.connect();
		 sql.addRating("unitTest-mei", 4.0);
		 sql.closeConnection();
		 this.mockMvc.perform(get("/driver/all/rating?cutoff=3.0")).andDo(print()).andExpect(status().isOk())
	     .andExpect(content().string(containsString("{\"operator\":\"unitTest-mei\",\"rating\":4.0,\"personsRated\":1,\"tripsCompleted\":0}")));
	
		 this.mockMvc.perform(get("/driver/all/personsRated?cutoff=1")).andDo(print()).andExpect(status().isOk())
	     .andExpect(content().string(containsString("{\"operator\":\"unitTest-mei\",\"rating\":4.0,\"personsRated\":1,\"tripsCompleted\":0}")));
	
		 sql.connect();
		 sql.incrementTripsCompleted("unitTest-mei");
		 sql.closeConnection();
		 this.mockMvc.perform(get("/driver/all/tripsCompleted?cutoff=1")).andDo(print()).andExpect(status().isOk())
	     .andExpect(content().string(containsString("{\"operator\":\"unitTest-mei\",\"rating\":4.0,\"personsRated\":1,\"tripsCompleted\":1}")));
	
	}
	
	@Test
	public void testNewAndDeleteDriver() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/delete?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("true")));
		
		this.mockMvc.perform(MockMvcRequestBuilders.put("/driver/new?operator=unitTest-mei")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("true")));
	}	
}
