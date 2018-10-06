package com.ecse321.team10.riderz.controller;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecse321.team10.riderz.dto.DriverDto;
import com.ecse321.team10.riderz.model.Driver;
import com.ecse321.team10.riderz.sql.MySQLJDBC;

/**
 * Controller for the Driver's API
 */
@RestController
@RequestMapping(value = "/driver")
public class DriversController {
	
	@Autowired
	private MySQLJDBC sql;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private DriverDto convertToDto(Driver driver) {
		return modelMapper.map(driver, DriverDto.class);
	}
	
	private static final Logger logger = LogManager.getLogger(RiderzController.class);

	/**
	 * Fetches a Driver from the database using the Driver's operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return A DriverDto object if an a Driver with that operator name was found 
	 * in the database. Null otherwise.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public DriverDto getDriverByUsername(@RequestParam String operator) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return null;
		}
		sql.connect();
		Driver driver = sql.getDriverByUsername(operator);
		if(driver != null) {
			sql.closeConnection();
			logger.info("Fetched driver with operator name: "+ operator + 
					" with info: " + driver.toString());
			DriverDto dto = convertToDto(driver);
			return dto;
		}
		sql.closeConnection();
		logger.info("No driver with operator name: "+ operator + " was found.");
		return null;
	}
	
//	TODO: implement filtering feature
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ArrayList<DriverDto> getDriversByFilter(@RequestParam String filter) {
//		try {
//			ArrayList<Driver> drivers;
//			sql.connect();
//			
//			if(filter.equalsIgnoreCase("rating")) {
//				//create & call jdbc method
//				drivers = (ArrayList<Driver>) null; //replace
//			}else if(filter.equalsIgnoreCase("personsRated")) {
//				drivers = (ArrayList<Driver>) null; //replace
//			}else if(filter.equalsIgnoreCase("tripsCompleted")) {
//				drivers = (ArrayList<Driver>) null; //replace
//			}
//			
//			if(!drivers.isEmpty()) {
//				sql.closeConnection();
//				logger.info("Fetched driver with filter: "+ filter);
//				ArrayList<DriverDto> driversDto = drivers.forEach(); //convert to dto
//				return driversDto;
//			}
//			sql.closeConnection();
//			logger.info("No driver with filter: "+ filter + "was found.");
//			return null; 
//		} catch (Exception e) {
//			logger.error(e.getClass().getName() + ": " + e.getMessage());
//			return null;
//		}
//	}
	
	/**
	 * Fetches all Drivers from the database.
	 * @return An ArrayList of DriverDto object(s) if an one or more Drivers were 
	 * found in the database. Null otherwise.
	 */
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ArrayList<DriverDto> getAllDrivers() {
		sql.connect();
		ArrayList<Driver> drivers = sql.getAllDrivers();
		ArrayList<DriverDto> driversDto = new ArrayList<DriverDto>();
		if(drivers != null) {
			sql.closeConnection();
			if(drivers.isEmpty()) {
				logger.info("No drivers were found.");
				return null;
			} else {
				logger.info("Fetched all drivers");
				for (Driver driver: drivers) {
					driversDto.add(convertToDto(driver));
				}
				return driversDto;
			}
		}
		sql.closeConnection();
		return null;
	}
	
	/**
	 * Fetches a Driver's rating from the database using their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return A double number representing the Driver's rating if a Driver with 
	 * that operator name was found in the database. Zero otherwise.
	 */
	@RequestMapping(value = "rating", method = RequestMethod.GET)
	public double getDriverRating(@RequestParam String operator) {
		sql.connect();
		double rating = sql.getRating(operator);
		sql.closeConnection();
		logger.info("Rating of driver with operator name: "+ operator + " is: " + rating);
		return rating;
	}
	
	/**
	 * Updates a Driver's rating with a new rating using their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @param rating 	- 	A double representing the Driver's new rating.
	 * @return True if a Driver with the inputed operator name was found and
	 * its rating was successfully updated. False otherwise.
	 */
	@RequestMapping(value = "rating", method = RequestMethod.PUT)
	public boolean updateDriverRating(@RequestParam String operator, @RequestParam double rating) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return false;
		}
		sql.connect();
		boolean addedRating = sql.addRating(operator, rating);
		if(addedRating) {
			sql.closeConnection();
			logger.info("Updated driver" + operator + " successfully with rating of " + rating);
			return true;
		}
		sql.closeConnection();
		logger.info("Failed to update driver "+ operator + " with rating of "+ rating + ".");
		return false;
	}
	
	/**
	 * Fetches the number of people who have rated a specific Driver using 
	 * their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return An integer number representing the number of people who have 
	 * rated the Driver with the inputed operator name. Zero otherwise.
	 */
	@RequestMapping(value = "rating/frequency", method = RequestMethod.GET)
	public int getDriverRatingFrequency(@RequestParam String operator) {
		sql.connect();
		int personsRated = sql.getPersonsRated(operator);
		sql.closeConnection();
		logger.info("Driver "+ operator + " has been rated by "+ personsRated + " people.");
		return personsRated;
	}
	
	/**
	 * Increments the number of people who have rated a specific Driver using 
	 * their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return True if the number of people who have rated the Driver with the inputed
	 * operator name has been successfully incremented. False otherwise.
	 */
	@RequestMapping(value = "rating/frequency", method = RequestMethod.PUT)
	public boolean incrementDriverRatingFrequency(@RequestParam String operator) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return false;
		}
		sql.connect();
		boolean incrementPersonsRated = sql.incrementPersonsRated(operator);
		if(incrementPersonsRated) {
			sql.closeConnection();
			logger.info("Incremented successfully the number of people who have rated driver " + operator + ".");
			return true;
		}
		sql.closeConnection();
		logger.info("Failed to increment the number of people who have rated driver " + operator + ".");
		return false;
	}
	
	/**
	 * Fetches a Driver's number of completed trips from the database 
	 * using their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return An integer number representing the Driver's number of completed trips
	 * if a Driver with that operator name was found in the database. Zero otherwise.
	 */
	@RequestMapping(value = "trip", method = RequestMethod.GET)
	public int getDriverTripsCompleted(@RequestParam String operator) {
		sql.connect();
		int completedTrips = sql.getTripsCompleted(operator);
		sql.closeConnection();
		logger.info("Driver "+ operator + " completed " + completedTrips + " trips.");
		return completedTrips;
	}
	
	/**
	 * Increments a Driver's number of completed trips using their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return True if a Driver with that operator name was found in the database
	 * and if their number of complete trips has been successfully incremented. 
	 * False otherwise.
	 */
	@RequestMapping(value = "trip", method = RequestMethod.PUT)
	public boolean incrementDriverTripsCompleted(@RequestParam String operator) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return false;
		}
		sql.connect();
		boolean incrementTripsCompleted = sql.incrementTripsCompleted(operator);
		if(incrementTripsCompleted) {
			sql.closeConnection();
			logger.info("Incremented successfully the number of trips driver " + operator + " has completed.");
			return true;
		}
		sql.closeConnection();
		logger.info("Failed to increment the number of trips that driver " + operator + " has completed.");
		return false;
	}
	
	/**
	 * Creates a new Driver from an existing User. The User's username becomes 
	 * the Driver's operator name.
	 * @param operator 	- 	A String representing the User's username or the new
	 * Driver's operator name.
	 * @return True if a new Driver with the inputed operator name has been successfully 
	 * created from an existing user of that username. False otherwise.
	 */
	@RequestMapping(value = "new", method = RequestMethod.PUT)
	public boolean newDriver(@RequestParam String operator) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return false;
		}
		sql.connect();
		boolean insertedDriver = sql.insertDriver(operator);
		if(insertedDriver) {
			sql.closeConnection();
			logger.info("Created driver " + operator + " successfully.");
			return true;
		}
		sql.closeConnection();
		logger.info("Failed to create new driver of operator name " + operator + ".");
		return false;
	}
	
	/**
	 * Deletes a Driver from the database using their operator name.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return True if a Driver of the inputed operator name has been 
	 * successfully deleted from the database. False otherwise.
	 */
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	public boolean deleteDriver(@RequestParam String operator) {
		if(!isAlphanumeric(operator)) {
			logger.info("Operator name: "+ operator + " is not alphanumeric.");
			return false;
		}
		sql.connect();
		boolean deletedDriver = sql.deleteDriver(operator);
		if(deletedDriver) {
			sql.closeConnection();
			logger.info("Deleted driver " + operator + " successfully.");
			return true;
		}
		sql.closeConnection();
		logger.info("Failed to delete driver of operator name " + operator + ".");
		return false;
	} 
	
	/**
	 * Validates if an input is valid for an operator name. Operator names
	 * can only be alphanumeric and can contain a dash character.
	 * @param operator 	- 	A String representing the Driver's operator name.
	 * @return True if the input name is valid. False otherwise.
	 */
	@RequestMapping(value = "validate/name", method = RequestMethod.GET)
	public boolean validate(@RequestParam String operator) {
		return isAlphanumeric(operator);
	}
	
	/**
	 * Validates if an input if it is alphanumeric or contains a dash.
	 * @param input 	- 	The String to be validated
	 * @return True if the input String is valid. False otherwise.
	 */
	private boolean isAlphanumeric(String input) {
		Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~/^]");
	    Matcher hasSpecial = special.matcher(input);
	    logger.info("input to validate: " + input);
		return !hasSpecial.find();
	}
}
