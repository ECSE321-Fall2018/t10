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
import com.ecse321.team10.riderz.model.Driver;
/**
 * Test Class for the MySQLJDBC class
 * 
 */
public class TestMySQLJDBC {
	MySQLJDBC sql = new MySQLJDBC();
	
	@Before
	public void setup() {
		sql.connect();
		sql.purgeDatabase();
	}
	
	@After
	public void tearDown() {
		sql.closeConnection();
	}

	//==================
	//	User Tests
	//==================
	@Test
	public void testInsertUser() {
		User testUser = new User("heqiantestUser", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		assertEquals(false, sql.insertUser(testUser));
		assertEquals(true, sql.deleteUser("heqiantestUser"));
	}
	
	@Test 
	public void testGetUser() {
		User testUser = new User("heqiantestUser", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		User userQueried = sql.getUserByUsername(testUser.getUsername());
		assertEquals(testUser.getUsername(), userQueried.getUsername());
		assertEquals(testUser.getEmail(), userQueried.getEmail());
		assertEquals(testUser.getPhone(), userQueried.getPhone());
		assertEquals(testUser.getFirstName(), userQueried.getFirstName());
		assertEquals(testUser.getLastName(), userQueried.getLastName());
		assertEquals(true, sql.deleteUser("heqiantestUser"));
		assertEquals(null, sql.getUserByUsername(testUser.getUsername()));
	}
	
	@Test 
	public void testGetAllUsers() {
		int numberUsers = sql.getAllUsers().size();
		User testUser = new User("heqiantestUser", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
			assertEquals(numberUsers + 1, sql.getAllUsers().size());
		}
		assertEquals(true, sql.deleteUser("heqiantestUser"));
	}
	
	//==================
	//	Car Tests
	//==================
	//***Make sure to delete Car first and then delete User. There is a foreign key dependency.
	
	@Test
	public void testInsertCar() {
		User testUser = new User("heqiantestCar", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		Car testCar = new Car("heqiantestCar", "Ford", "Escape", 2010, 5, 12.5, "ABC123");
		if(sql.getCarByOperator("heqiantestCar") == null) {
			assertEquals(true, sql.insertCar(testCar));
		}
		assertEquals(true, sql.deleteCar("heqiantestCar"));
		assertEquals(true, sql.deleteUser("heqiantestCar"));
	}
	
	@Test
	public void testGetCar() {
		User testUser = new User("heqiantestCar", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		Car testCar = new Car("heqiantestCar", "Ford", "Escape", 2010, 5, 12.5, "ABC123");
		if(sql.getCarByOperator("heqiantestCar") == null) {
			assertEquals(true, sql.insertCar(testCar));
		}
		Car carQueried = sql.getCarByOperator(testCar.getOperator());
		assertEquals(testCar.getOperator(), carQueried.getOperator());
		assertEquals(testCar.getMake(), carQueried.getMake());
		assertEquals(testCar.getModel(), carQueried.getModel());
		assertEquals(testCar.getYear(), carQueried.getYear());
		assertEquals(testCar.getNumOfSeats(), carQueried.getNumOfSeats());
		assertEquals(true, sql.deleteCar("heqiantestCar"));
		assertEquals(true, sql.deleteUser("heqiantestCar"));
	}
	
	@Test
	public void testUpdatetCar() {
		User testUser = new User("heqiantestCar", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		Car testCar = new Car("heqiantestCar", "Ford", "Escape", 2010, 5, 12.5, "ABC123");
		if(sql.getCarByOperator("heqiantestCar") == null) {
			assertEquals(true, sql.insertCar(testCar));
		}
		Car testUpdateCar = new Car("heqiantestCar", "Honda", "CR-V", 2015, 4, 10.5, "DEF456");
		sql.updateCar(testUpdateCar);
		
		Car carQueried = sql.getCarByOperator(testCar.getOperator());
		assertEquals(testUpdateCar.getOperator(), carQueried.getOperator());
		assertEquals(testUpdateCar.getMake(), carQueried.getMake());
		assertEquals(testUpdateCar.getModel(), carQueried.getModel());
		assertEquals(testUpdateCar.getYear(), carQueried.getYear());
		assertEquals(testUpdateCar.getNumOfSeats(), carQueried.getNumOfSeats());
		assertEquals(true, sql.deleteCar("heqiantestCar"));
		assertEquals(true, sql.deleteUser("heqiantestCar"));
	}
	
	@Test 
	public void testGetAllCars() {
		int numberCars = sql.getAllCars().size();
		User testUser = new User("heqiantestCar", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		Car testCar = new Car("heqiantestCar", "Ford", "Escape", 2010, 5, 12.5, "ABC123");
		if(sql.getCarByOperator("heqiantestCar") == null) {
			assertEquals(true, sql.insertCar(testCar));
			assertEquals(numberCars + 1, sql.getAllCars().size());
		}
		assertEquals(true, sql.deleteCar("heqiantestCar"));
		assertEquals(true, sql.deleteUser("heqiantestCar"));
	}
	
	//==================
	//	Trip Tests
	//==================
	
	@Test
	public void testInsertTrip() {
		User testUser = new User("heqiantestTrip", "password", "heqian@gmail.com", "514888888", "He Qian", "Wang");
		if(sql.getUserByUsername(testUser.getUsername()) == null) {
			assertEquals(true, sql.insertUser(testUser));
		}
		Car testCar = new Car("heqiantestTrip", "Ford", "Escape", 2010, 5, 12.5, "ABC123");
		if(sql.getCarByOperator("heqiantestTrip") == null) {
			assertEquals(true, sql.insertCar(testCar));
		}
		
		if(sql.getLastTripByUsername("heqiantestTrip") == null) {
			assertEquals(true, sql.insertTrip("heqiantestTrip"));
		}
		Trip testTrip = sql.getLastTripByUsername("heqiantestTrip");
		assertEquals(true, sql.deleteTrip(testTrip.getTripID(), testTrip.getOperator()));
		assertEquals(true, sql.deleteCar("heqiantestTrip"));
		assertEquals(true, sql.deleteUser("heqiantestTrip"));

	}

	//==================
	//  Driver Tests
	//==================
	@Test
	public void testInsertDeleteGetDriver() {
		User testUser10 = new User("user10", "password10", "user10@gmail.com", "1234567890", "user10FirstName", "user10LastName");
		assertEquals(true, sql.insertUser(testUser10));
		assertEquals(true, sql.insertDriver(testUser10.getUsername()));
		Driver driver = sql.getDriverByUsername(testUser10.getUsername());
		assertEquals(testUser10.getUsername(), driver.getOperator());
		assertEquals(0.0, driver.getRating(), 0.0);
		assertEquals(0, driver.getTripsCompleted());
		assertEquals(true, sql.deleteDriver(testUser10.getUsername()));
		assertEquals(true, sql.deleteUser(testUser10.getUsername()));
	}

	@Test
	public void testGetAllDrivers() {
		User testUser11 = new User("user11", "password11", "user11@gmail.com", "1234567890", "user12FirstName", "user12LastName");
		assertEquals(true, sql.insertUser(testUser11));
		assertEquals(true, sql.insertDriver(testUser11.getUsername()));
		User testUser12 = new User("user12", "password12", "user12@gmail.com", "1234567890", "user12FirstName", "user12LastName");
		assertEquals(true, sql.insertUser(testUser12));
		assertEquals(true, sql.insertDriver(testUser12.getUsername()));
		ArrayList<Driver> allDrivers = sql.getAllDrivers();
		assertEquals(2, allDrivers.size());
		assertEquals(true, sql.deleteDriver(testUser11.getUsername()));
		assertEquals(true, sql.deleteUser(testUser11.getUsername()));
		assertEquals(true, sql.deleteDriver(testUser12.getUsername()));
		assertEquals(true, sql.deleteUser(testUser12.getUsername()));
	}

	@Test
	public void testGetAllDriversWithFilters() {
		User testUser13 = new User("user13", "password13", "user13@gmail.com", "1234567890", "user13FirstName", "user13LastName");
		assertEquals(true, sql.insertUser(testUser13));
		assertEquals(true, sql.insertDriver(testUser13.getUsername()));
		assertEquals(true, sql.incrementPersonsRated(testUser13.getUsername()));
		assertEquals(true, sql.incrementTripsCompleted(testUser13.getUsername()));
		assertEquals(true, sql.addRating(testUser13.getUsername(), 3.0));
		assertEquals(1, sql.getAllDriversAboveTripsCompleted(1).size());
		assertEquals(1, sql.getAllDriversAboveRating(1.5).size());
		assertEquals(1, sql.getAllDriversAbovePersonsRated(2).size());
		assertEquals(0, sql.getAllDriversAboveTripsCompleted(2).size());
		assertEquals(0, sql.getAllDriversAboveRating(2.0).size());
		assertEquals(0, sql.getAllDriversAbovePersonsRated(3).size());
		assertEquals(true, sql.deleteDriver(testUser13.getUsername()));
		assertEquals(true, sql.deleteUser(testUser13.getUsername()));
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

		ArrayList<Itinerary> a = sql.getItineraryNearDestination(12.555, -14.352, 13.243, -14.8752, 10000, arrivalTime);
		assertEquals(true, a.size() != 0);

		a = sql.getItineraryNearDestination(12.555, -14.352, 13.200, -14.8752, 50, arrivalTime);
		assertEquals(true, a.size() == 0);

		arrivalTime = sql.convertStringToTimestamp("2018-03-20 20:00:00.000");
		a = sql.getItineraryNearDestination(12.555, -14.352, 13.243, -14.8752, 5000, arrivalTime);
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

	//=======================
	//  Authentication Tests
	//=======================
	@Test
	public void testAuthentication() {
		User testUser = new User("user15", "password15", "user15@gmail.com", "1234567890", "first", "last");
		assertEquals(true, sql.insertUser(testUser));
		assertEquals(true, sql.insertAuthentication(testUser.getUsername()));
		assertEquals(true, sql.verifyAuthentication(testUser.getUsername()));
		assertEquals(true, sql.deleteAuthentication(testUser.getUsername()));
		assertEquals(true, sql.deleteUser(testUser.getUsername()));
	}
}
