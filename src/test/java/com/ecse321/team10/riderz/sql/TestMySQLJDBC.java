package com.ecse321.team10.riderz.sql;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.Itinerary;
import com.ecse321.team10.riderz.model.Location;
import com.ecse321.team10.riderz.model.Reservation;
import com.ecse321.team10.riderz.model.Trip;
import com.ecse321.team10.riderz.model.User;

public class TestMySQLJDBC {
	MySQLJDBC sql = new MySQLJDBC();
	
	@Before
	public void setup() {
		sql.connect();
	}
	
	@After
	public void tearDown() {
		sql.closeConnection();
	}

	@Test
	public void testInsertUser() {
		User testUser = new User("heqiantest", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.deleteUser("heqiantest"));
	}

	//==================
	//  Itinerary Tests
	//==================
	@Test
	public void testInsertUpdateDeleteItinerary() {
		User testUser = new User("user1", "password1", "user1@gmail.com", "5141234567", "user1FirstName", "user1LastName");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.insertTrip("user1"));
		int tripID = sql.getLastTripByUsername("user1").getTripID();

		Timestamp startingTime = sql.convertStringToTimestamp("2019-02-20 20:00:00.000");
		Timestamp endingTime = sql.convertStringToTimestamp("2019-02-21 02:00:00.000");

		Itinerary itinerary = new Itinerary(tripID, 12.555, -14.352, startingTime,
											13.242, -14.875, endingTime, 5);

		Timestamp startingTime2 = sql.convertStringToTimestamp("2019-01-12 20:00:00.000");
		Timestamp endingTime2 = sql.convertStringToTimestamp("2019-01-13 20:00:00.000");

		Itinerary itinerary2 = new Itinerary(tripID, 12.526, -14.512, startingTime2,
											 14.525, -15.925, endingTime2, 4);
		assertEquals(true, sql.insertItinerary(itinerary));
		assertEquals(true, sql.updateItinerary(itinerary2));
		assertEquals(true, sql.deleteItinerary(tripID));
		assertEquals(true, sql.deleteTrip(tripID, "user1"));
		assertEquals(true, sql.deleteUser("user1"));
	}

	@Test
	public void testGetItineraryByTripID() {
		User testUser = new User("user2", "password2", "user2@gmail.com", "5141234567", "user2FirstName", "user2LastName");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.insertTrip("user2"));
		int tripID = sql.getLastTripByUsername("user2").getTripID();

		Timestamp startingTime = sql.convertStringToTimestamp("2019-02-20 20:00:00.000");
		Timestamp endingTime = sql.convertStringToTimestamp("2019-02-21 02:00:00.000");

		Itinerary itinerary = new Itinerary(tripID, 12.555, -14.352, startingTime,
											13.242, -14.875, endingTime, 5);

		assertEquals(true, sql.insertItinerary(itinerary));
		Itinerary returnedItinerary = sql.getItineraryByTripID(tripID);
		assertEquals(tripID, returnedItinerary.getTripID());
		assertEquals(12.555, returnedItinerary.getStartingLongitude(), 0.0);
		assertEquals(-14.352, returnedItinerary.getStartingLatitude(), 0.0);
		assertEquals(startingTime, returnedItinerary.getStartingTime());
		assertEquals(13.242, returnedItinerary.getEndingLongitude(), 0.0);
		assertEquals(-14.875, returnedItinerary.getEndingLatitude(), 0.0);
		assertEquals(endingTime, returnedItinerary.getEndingTime());
		assertEquals(5, returnedItinerary.getSeatsLeft());
		assertEquals(true, sql.deleteItinerary(tripID));
		assertEquals(true, sql.deleteTrip(tripID, "user2"));
		assertEquals(true, sql.deleteUser("user2"));
	}

	@Test
	public void testSeatsIncrementDecrement() {
		User testUser = new User("user3", "password3", "user3@gmail.com", "5141234567", "user3FirstName", "user3LastName");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.insertTrip("user3"));
		int tripID = sql.getLastTripByUsername("user3").getTripID();

		Timestamp startingTime = sql.convertStringToTimestamp("2019-02-20 20:00:00.000");
		Timestamp endingTime = sql.convertStringToTimestamp("2019-02-21 02:00:00.000");

		Itinerary itinerary = new Itinerary(tripID, 12.555, -14.352, startingTime,
											13.242, -14.875, endingTime, 5);

		assertEquals(true, sql.insertItinerary(itinerary));
		assertEquals(true, sql.incrementSeatsLeft(tripID));

		Itinerary returnedItinerary = sql.getItineraryByTripID(tripID);
		assertEquals(6, returnedItinerary.getSeatsLeft());

		assertEquals(true, sql.decrementSeatsLeft(tripID));
		returnedItinerary = sql.getItineraryByTripID(tripID);
		assertEquals(5, returnedItinerary.getSeatsLeft());

		assertEquals(true, sql.deleteItinerary(tripID));
		assertEquals(true, sql.deleteTrip(tripID, "user3"));
		assertEquals(true, sql.deleteUser("user3"));
	}

	@Test
	public void testGetItineraryNearDestination() {
		User testUser = new User("user4", "password4", "user1@gmail.com", "5141234567", "user4FirstName", "user4LastName");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.insertTrip("user4"));
		int tripID = sql.getLastTripByUsername("user4").getTripID();

		Timestamp startingTime = sql.convertStringToTimestamp("2019-02-20 20:00:00.000");
		Timestamp endingTime = sql.convertStringToTimestamp("2019-02-21 02:00:00.000");

		Itinerary itinerary = new Itinerary(tripID, 12.555, -14.352, startingTime,
											13.242, -14.875, endingTime, 5);

		Timestamp arrivalTime = sql.convertStringToTimestamp("2019-02-21 20:00:00.000");

		assertEquals(true, sql.insertItinerary(itinerary));

		ArrayList<Itinerary> a = sql.getItineraryNearDestination(13.243, -14.8752, 10000, arrivalTime);
		System.out.println(a.toString());
		assertEquals(true, a.size() != 0);

		a = sql.getItineraryNearDestination(13.200, -14.8752, 50, arrivalTime);
		assertEquals(true, a.size() == 0);

		arrivalTime = sql.convertStringToTimestamp("2018-03-20 20:00:00.000");
		a = sql.getItineraryNearDestination(13.243, -14.8752, 5000, arrivalTime);
		assertEquals(true, a.size() == 0);

		assertEquals(true, sql.deleteItinerary(tripID));
		assertEquals(true, sql.deleteTrip(tripID, "user4"));
		assertEquals(true, sql.deleteUser("user4"));
	}

	//==================
	//  Location Tests
	//==================
	@Test
	public void testInsertDeleteLocation() {
		User testUser = new User("user5", "password5", "user5@gmail.com", "5141234567", "user5FirstName", "user5LastName");
		assertEquals(true, sql.insertUser(testUser));

		Location location = new Location("user5", 25.00, 35.00);
		assertEquals(true, sql.insertLocation(location));

		assertEquals(true, sql.deleteLocation("user5"));
		assertEquals(true, sql.deleteUser("user5"));
	}

	@Test
	public void testUpdateGetLocation() {
		User testUser = new User("user6", "password6", "user6@gmail.com", "5141234567", "user6FirstName", "user6LastName");
		assertEquals(true, sql.insertUser(testUser));

		Location location = new Location("user6", 25.000, 35.000);
		assertEquals(true, sql.insertLocation(location));

		Location returnedLocation = sql.getLocationByUsername("user6");
		assertEquals(location.getOperator(), returnedLocation.getOperator());
		assertEquals(location.getLongitude(), returnedLocation.getLongitude(), 0.0);
		assertEquals(location.getLatitude(), returnedLocation.getLatitude(), 0.0);

		location = new Location("user6", 35.000, -45.000);
		assertEquals(true, sql.updateLocation(location));
		returnedLocation = sql.getLocationByUsername("user6");
		assertEquals(location.getOperator(), returnedLocation.getOperator());
		assertEquals(location.getLongitude(), returnedLocation.getLongitude(), 0.0);
		assertEquals(location.getLatitude(), returnedLocation.getLatitude(), 0.0);

		assertEquals(true, sql.deleteLocation("user6"));
		assertEquals(true, sql.deleteUser("user6"));
	}

	@Test
	public void testGetLocationNear() {
		User testUser = new User("user7", "password7", "user7@gmail.com", "5141234567", "user7FirstName", "user7LastName");
		assertEquals(true, sql.insertUser(testUser));

		Location location = new Location("user7", 25.000, 35.000);
		assertEquals(true, sql.insertLocation(location));

		ArrayList<Location> a = sql.getLocationNear(25.002, 35.003, 10000);
		assertEquals(true, a.size() != 0);

		a = sql.getLocationNear(25.002, 35.003, 50);
		assertEquals(true, a.size() == 0);

		a = sql.getLocationNear(-150, 25, 10000);
		assertEquals(true, a.size() == 0);

		assertEquals(true, sql.deleteLocation("user7"));
		assertEquals(true, sql.deleteUser("user7"));
	}

	//====================
	//  Reservation Tests
	//====================
	@Test
	public void testReservation() {
		User testUser1 = new User("user8", "password8", "user8@gmail.com", "5141234567", "user8FirstName", "user8LastName");
		assertEquals(true, sql.insertUser(testUser1));

		User testUser2 = new User("user9", "password9", "user9@gmail.com", "5141234567", "user9FirstName", "user9LastName");
		assertEquals(true, sql.insertUser(testUser2));

		assertEquals(true, sql.insertTrip(testUser1.getUsername()));
		int tripID1 = sql.getLastTripByUsername(testUser1.getUsername()).getTripID();

		assertEquals(true, sql.insertTrip(testUser1.getUsername()));
		int tripID2 = sql.getLastTripByUsername(testUser1.getUsername()).getTripID();

		Reservation reservation1 = new Reservation(testUser2.getUsername(), tripID1);
		Reservation reservation2 = new Reservation(testUser2.getUsername(), tripID2);
		assertEquals(true, sql.insertReservation(reservation1));
		assertEquals(true, sql.insertReservation(reservation2));

		ArrayList<Reservation> a = sql.getReservationByUsername(testUser2.getUsername());
		assertEquals(true, a.size() == 2);

		assertEquals(true, sql.deleteReservation(reservation1));
		assertEquals(true, sql.deleteReservation(reservation2));
		a = sql.getReservationByUsername(testUser2.getUsername());
		assertEquals(true, a.size() == 0);

		assertEquals(true, sql.deleteTrip(tripID1, testUser1.getUsername()));
		assertEquals(true, sql.deleteTrip(tripID2, testUser1.getUsername()));
		assertEquals(true, sql.deleteUser(testUser1.getUsername()));
		assertEquals(true, sql.deleteUser(testUser2.getUsername()));
	}
}
