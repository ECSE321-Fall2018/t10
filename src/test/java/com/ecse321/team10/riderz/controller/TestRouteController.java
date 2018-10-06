package com.ecse321.team10.riderz.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.ecse321.team10.riderz.model.Itinerary;
import com.ecse321.team10.riderz.model.User;
//import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.sql.MySQLJDBC;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestRouteController {
	@Autowired
    private MockMvc mockMvc;
	MySQLJDBC sql = new MySQLJDBC();
	
	int tripID;
	int tripID2;
	int tripID3;
	
	@Before
	public void setup() {
		
		User user = new User("unitTest-Sav", "unitTestSavoie", "mat_savoie@hotmail.com", "1234445555", "Mat", "Sav");
		User user2 = new User("unitTest-MatSav", "unitTestMatSavoie2", "mat_savoie2@hotmail.com", "1121234445555", "Matthieu", "Savi");
		
		sql.connect();
		sql.insertUser(user);
		sql.insertUser(user2);
		sql.insertTrip("unitTest-Sav");
		tripID = sql.getLastTripByUsername("unitTest-Sav").getTripID();
		sql.insertTrip("unitTest-Sav");
		tripID2 = sql.getLastTripByUsername("unitTest-Sav").getTripID();
		sql.insertTrip("unitTest-MatSav");
		tripID3 = sql.getLastTripByUsername("unitTest-MatSav").getTripID();
		sql.closeConnection();
	}
	
	@After
	public void tearDown() {
		sql.connect();
		sql.deleteTrip(tripID, "unitTest-Sav");
		sql.deleteTrip(tripID2, "unitTest-Sav");
		sql.deleteTrip(tripID3, "unitTest-MatSav");
		sql.deleteUser("unitTest-Sav");
		sql.deleteUser("unitTest-MatSav");
		sql.closeConnection();
	}
	
	/**
	 * IMPORTANT NOTE: 
	 * *** FOR LOCAL TESTING ONLY ***
	 * The code below is commented. The reason being that when testing locally, the URLS return Timestamp that have a 5 hours difference. Time zone issue is at the source of this problem (only occurs locally).
	 */
	/*
	@Test
	public void testItinerary () throws Exception {
		
		// Testing insert Itinerary
		// tripID
		this.mockMvc.perform(get("/insertItinerary/" + tripID + "/22.2222/-33.33333/2050-01-02 02:00:00.000/12.232323/-52.525252/2051-01-01 02:30:00.000/1")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":22.2222,\"startingLatitude\":-33.33333,\"startingTime\":\"2050-01-02T07:00:00.000+0000\",\"endingLongitude\":12.232323,\"endingLatitude\":-52.525252,\"endingTime\":\"2051-01-01T07:30:00.000+0000\",\"seatsLeft\":1}")));
	
		// Testing insert Itinerary
		// tripID2
		this.mockMvc.perform(get("/insertItinerary/" + tripID2 + "/15.33534/12.44412/2019-01-02 02:00:00.000/45.45658/-73.86932/2019-01-02 02:30:00.000/1")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID2 +",\"startingLongitude\":15.33534,\"startingLatitude\":12.44412,\"startingTime\":\"2019-01-02T07:00:00.000+0000\",\"endingLongitude\":45.45658,\"endingLatitude\":-73.86932,\"endingTime\":\"2019-01-02T07:30:00.000+0000\",\"seatsLeft\":1}")));
		
		// Testing update Itinerary
		this.mockMvc.perform(get("/updateItinerary/" + tripID + "/45.41998/-73.883442/2019-01-01 02:00:00.000/45.45618/-73.86232/2019-01-01 02:30:00.000/3")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T07:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T07:30:00.000+0000\",\"seatsLeft\":3}")));
	
		// Testing get Itinerary by tripID
		this.mockMvc.perform(get("/getItineraryByTripID/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T07:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T07:30:00.000+0000\",\"seatsLeft\":3}")));
	
		// Testing get Itinerary near destinations (1 Itinerary)
		this.mockMvc.perform(get("/getItineraryNearDestination/45.45688/-73.86992/1000.00000/2019-01-01 04:30:00.000")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T07:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T07:30:00.000+0000\",\"seatsLeft\":3}")));
		
		// Testing get Itineraries near destination (2 Itinerary)
		this.mockMvc.perform(get("/getItineraryNearDestination/45.45688/-73.86992/1000000.00000/2019-01-02 04:30:00.000")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T07:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T07:30:00.000+0000\",\"seatsLeft\":3},{\"tripID\":"+ tripID2 +",\"startingLongitude\":15.33534,\"startingLatitude\":12.44412,\"startingTime\":\"2019-01-02T07:00:00.000+0000\",\"endingLongitude\":45.45658,\"endingLatitude\":-73.86932,\"endingTime\":\"2019-01-02T07:30:00.000+0000\",\"seatsLeft\":1}")));
		
		// Testing increment seats left by tripID
		this.mockMvc.perform(get("/incrementSeatsLeft/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("The number of seats left in the itinerary " + tripID +" was incremented.")));
		
		// Testing decrement seats left by tripID
		this.mockMvc.perform(get("/decrementSeatsLeft/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("The number of seats left in the itinerary "+ tripID +" was decremented.")));
		
		// Testing delete Itinerary by tripID
		this.mockMvc.perform(get("/deleteItinerary/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Itinerary " +tripID +" was deleted.")));		
		
		this.mockMvc.perform(get("/deleteItinerary/" + tripID2)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Itinerary " +tripID2 +" was deleted.")));
	}
	*/
	
	/**
	 * Testing Itinerary
	 * 
	 * IMPORTANT NOTE:
	 * *** Testing for Travis CI ***
	 * 
	 */
	@Test
	public void testItinerary () throws Exception {
		
		// Testing insert Itinerary
		// tripID
		this.mockMvc.perform(get("/insertItinerary/" + tripID + "/22.2222/-33.33333/2050-01-02 02:00:00.000/12.232323/-52.525252/2051-01-01 02:30:00.000/1")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":22.2222,\"startingLatitude\":-33.33333,\"startingTime\":\"2050-01-02T02:00:00.000+0000\",\"endingLongitude\":12.232323,\"endingLatitude\":-52.525252,\"endingTime\":\"2051-01-01T02:30:00.000+0000\",\"seatsLeft\":1}")));
		
		// tripID2
		this.mockMvc.perform(get("/insertItinerary/" + tripID2 + "/15.33534/12.44412/2019-01-02 02:00:00.000/45.45658/-73.86932/2019-01-02 02:30:00.000/1")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID2 +",\"startingLongitude\":15.33534,\"startingLatitude\":12.44412,\"startingTime\":\"2019-01-02T02:00:00.000+0000\",\"endingLongitude\":45.45658,\"endingLatitude\":-73.86932,\"endingTime\":\"2019-01-02T02:30:00.000+0000\",\"seatsLeft\":1}")));
		
		// Testing update Itinerary
		this.mockMvc.perform(get("/updateItinerary/" + tripID + "/45.41998/-73.883442/2019-01-01 02:00:00.000/45.45618/-73.86232/2019-01-01 02:30:00.000/3")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T02:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T02:30:00.000+0000\",\"seatsLeft\":3}")));
	
		// Testing get Itinerary by tripID
		this.mockMvc.perform(get("/getItineraryByTripID/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T02:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T02:30:00.000+0000\",\"seatsLeft\":3}")));
	
		// Testing get Itinerary near destinations (1 Itinerary)
		this.mockMvc.perform(get("/getItineraryNearDestination/45.45688/-73.86992/1000.00000/2019-01-01 04:30:00.000")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T02:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T02:30:00.000+0000\",\"seatsLeft\":3}")));
		
		// Testing get Itineraries near destination (2 Itinerary)
		this.mockMvc.perform(get("/getItineraryNearDestination/45.45688/-73.86992/1000000.00000/2019-01-02 04:30:00.000")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+ tripID +",\"startingLongitude\":45.41998,\"startingLatitude\":-73.883442,\"startingTime\":\"2019-01-01T02:00:00.000+0000\",\"endingLongitude\":45.45618,\"endingLatitude\":-73.86232,\"endingTime\":\"2019-01-01T02:30:00.000+0000\",\"seatsLeft\":3},{\"tripID\":"+ tripID2 +",\"startingLongitude\":15.33534,\"startingLatitude\":12.44412,\"startingTime\":\"2019-01-02T02:00:00.000+0000\",\"endingLongitude\":45.45658,\"endingLatitude\":-73.86932,\"endingTime\":\"2019-01-02T02:30:00.000+0000\",\"seatsLeft\":1}")));
		
		// Testing increment seats left by tripID
		this.mockMvc.perform(get("/incrementSeatsLeft/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("The number of seats left in the itinerary " + tripID +" was incremented.")));
		
		// Testing decrement seats left by tripID
		this.mockMvc.perform(get("/decrementSeatsLeft/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("The number of seats left in the itinerary "+ tripID +" was decremented.")));
		
		// Testing delete Itinerary by tripID
		this.mockMvc.perform(get("/deleteItinerary/" + tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Itinerary " +tripID +" was deleted.")));		
		
		this.mockMvc.perform(get("/deleteItinerary/" + tripID2)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Itinerary " +tripID2 +" was deleted.")));
	}

	/**
	 * Testing Location
	 */
	@Test
	public void testLocation () throws Exception {
		
		// Testing insert Location
		this.mockMvc.perform(get("/insertLocation/unitTest-Sav/45.4199124/-75.983142")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Location of unitTest-Sav has been inserted into the database.")));
		
		// Testing insert Location
		this.mockMvc.perform(get("/insertLocation/unitTest-MatSav/45.4199111/-75.983111")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Location of unitTest-MatSav has been inserted into the database.")));
		
		// Testing get Location by userName
		this.mockMvc.perform(get("/getLocationByUsername/unitTest-MatSav")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"operator\":\"unitTest-MatSav\",\"longitude\":45.4199111,\"latitude\":-75.983111}")));
		
		// Testing get location near
		this.mockMvc.perform(get("/getLocationNear/45.4199131/-75.983121/1000")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"operator\":\"unitTest-MatSav\",\"longitude\":45.4199111,\"latitude\":-75.983111},{\"operator\":\"unitTest-Sav\",\"longitude\":45.4199124,\"latitude\":-75.983142}")));
		
		// Testing update Location
		this.mockMvc.perform(get("/updateLocation/unitTest-MatSav/11.111111/-75.111111")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Location of unitTest-MatSav has been updated.")));
		
		// Testing get delete by userName
		this.mockMvc.perform(get("/deleteLocation/unitTest-MatSav")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Location of unitTest-MatSav has been deleted.")));

		this.mockMvc.perform(get("/deleteLocation/unitTest-Sav")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Location of unitTest-Sav has been deleted.")));		
	}
	
	/**
	 * Testing Reservation
	 */
	@Test
	public void testReservation () throws Exception{
		
		// Testing insert Reservation
		this.mockMvc.perform(get("/insertReservation/unitTest-Sav/"+ tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Reservation of unitTest-Sav has been inserted for the tripID: " + tripID)));
		
		this.mockMvc.perform(get("/insertReservation/unitTest-Sav/"+ tripID2)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Reservation of unitTest-Sav has been inserted for the tripID: " + tripID2)));
		/*
		// Testing get reservations by user name
		this.mockMvc.perform(get("/getReservationByUsername/unitTest-Sav/")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"operator\":\"unitTest-Sav\",\"tripID\":"+ tripID +"},{\"operator\":\"unitTest-Sav\",\"tripID\":"+tripID2+"}")));
		*/
		this.mockMvc.perform(get("/getReservationByUsername/unitTest-Sav/")).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("{\"tripID\":"+tripID+",\"operator\":\"unitTest-Sav\"},{\"tripID\":"+tripID2+",\"operator\":\"unitTest-Sav\"}")));
		
		// Testing delete Reservation
		this.mockMvc.perform(get("/deleteReservation/unitTest-Sav/"+ tripID)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Reservation of unitTest-Sav has been deleted for the tripID: " + tripID)));
		
		this.mockMvc.perform(get("/deleteReservation/unitTest-Sav/"+ tripID2)).andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Reservation of unitTest-Sav has been deleted for the tripID: " + tripID2)));
	}
}
