package com.ecse321.team10.riderz.sql;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.Trip;
import com.ecse321.team10.riderz.model.User;
/**
 * Test Class for the MySQLJDBC class
 * 
 */
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
}
