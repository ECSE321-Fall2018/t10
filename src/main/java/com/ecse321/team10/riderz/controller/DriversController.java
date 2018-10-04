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
	
	@RequestMapping(value = "rating", method = RequestMethod.GET)
	public double getDriverRating(@RequestParam String operator) {
		sql.connect();
		double rating = sql.getRating(operator);
		sql.closeConnection();
		logger.info("Rating of driver with operator name: "+ operator + " is: " + rating);
		return rating;
	}
	
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
	
	@RequestMapping(value = "rating/frequency", method = RequestMethod.GET)
	public int getDriverRatingFrequency(@RequestParam String operator) {
		sql.connect();
		int personsRated = sql.getPersonsRated(operator);
		sql.closeConnection();
		logger.info("Driver "+ operator + " has been rated by "+ personsRated + " people.");
		return personsRated;
	}
	
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
	
	@RequestMapping(value = "trip", method = RequestMethod.GET)
	public int getDriverTripsCompleted(@RequestParam String operator) {
		sql.connect();
		int completedTrips = sql.getTripsCompleted(operator);
		sql.closeConnection();
		logger.info("Driver "+ operator + " completed " + completedTrips + " trips.");
		return completedTrips;
	}
	
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
	
	@RequestMapping(value = "validate/name", method = RequestMethod.GET)
	public boolean validate(@RequestParam String operator) {
		return isAlphanumeric(operator);
	}
	
	private boolean isAlphanumeric(String input) {
		Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~/^]");
	    Matcher hasSpecial = special.matcher(input);
	    logger.info("input to validate: " + input);
		return !hasSpecial.find();
	}
}
