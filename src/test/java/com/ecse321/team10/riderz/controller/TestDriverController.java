package com.ecse321.team10.riderz.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestDriverController {
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testGetDriverByUsername() throws Exception {
		 this.mockMvc.perform(get("/driver?operator=testmei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("{operator: testmei, rating: 0, personsRated: 0, tripsCompleted: 0}")));
	}
	
	@Test
	public void testGetAllDrivers() throws Exception {
		 this.mockMvc.perform(get("/driver/all")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("")));
	}
	
	@Test
	public void testGetDriverRating() throws Exception {
		 this.mockMvc.perform(get("/driver/rating?operator=testmei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("")));
	}
	
	@Test
	public void testGetDriverRatingFrequency() throws Exception {
		 this.mockMvc.perform(get("/driver/rating/frequency?operator=testmei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("")));
	}
	
	@Test
	public void testGetDriverTripsCompleted() throws Exception {
		 this.mockMvc.perform(get("/driver/trip?operator=testmei")).andDo(print()).andExpect(status().isOk())
         .andExpect(content().string(containsString("")));
	}
}
