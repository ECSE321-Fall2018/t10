package com.ecse321.team10.riderz.controller;

import com.ecse321.team10.riderz.dto.CarDto;
import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.sql.MySQLJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/car")

/**
 * Controller for the Car API
 */
@RestController
public class CarController {

	@Autowired
	private MySQLJDBC sql;

	@Autowired
	private ModelMapper modelMapper;

	private CarDto convertToDto(Car car) { return modelMapper.map(car, CarDto.class); }

	private static final Logger logger = LogManager.getLogger(RiderzController.class);

	/**
	 * Creates the car information for a Driver
	 * @param operator		 -		A String representing the Driver's operator name
	 * @param make			 -		A String representing the make of the Car
	 * @param model			 -		An integer representing the year of the Car
	 * @param year			 -		An integer representing the year of the Car
	 * @param numberOfSeats  -		An integer representing the number of seats in the Car
	 * @param fuelEfficiency -		A double representing the Car's gas consumption
	 * @param licensePlate	 -		A String representing the license plate associated to the Car
	 * @return CarDto Object if the Car was successfully created in the database. Otherwise Null
	 */
	@PostMapping("")
	public CarDto addCar(@RequestParam String operator,
						 @RequestParam String make,
						 @RequestParam String model,
						 @RequestParam int year,
						 @RequestParam int numberOfSeats,
						 @RequestParam double fuelEfficiency,
						 @RequestParam String licensePlate) {
		if (areValidCarParameters(make, model, licensePlate)){
			Car car = new Car(operator, make, model, year, numberOfSeats, fuelEfficiency, licensePlate);
			sql.connect();
			if(sql.insertCar(car)) {
				sql.closeConnection();
				logger.info("Successfully added a new car for operator" + operator);
				return convertToDto(car);
			}
			logger.info("There an incorrect parameter");
			sql.closeConnection();
			return null;
		}
		else{
			return null;
		}
	}

	/**
	 * Deletes a Car from the database using the Driver's operating name
	 * @param operator		 -		A String representing the Driver's operator name
	 * @return True if the Car associated with the requested operator name was successfully deleted.
	 * Otherwise False
	 */
	@DeleteMapping("")
	public boolean deleteCar(@RequestParam String operator) {
		sql.connect();
		if (operator != null){
			sql.deleteCar(operator);
			sql.closeConnection();
			logger.info("Successfully deleted the car owned by: " + operator);
			return true;
		}
		else{
			sql.closeConnection();
			logger.info("There was no operator");
			return false;
		}
	}

	/**
	 * Updates the Driver's current car information
	 * @param operator		 -		A String representing the Driver's operator name
	 * @param make			 -		A String representing the make of the Car
	 * @param model			 -		A String representing the model of the Car
	 * @param year			 -		An integer representing the year of the Car
	 * @param numberOfSeats	 -		An integer representing the number of seats in the Car
	 * @param fuelEfficiency -		A double representing the Car's gas consumption
	 * @param licensePlate	 -		A String representing the license plate associated to the Car
	 * @return CarDto Object if the Car information associated with a Driver, linked via operator name,
	 * is present in the database is successfully updated. Otherwise Null.
	 */
	@PutMapping("")
	public CarDto updateCar(@RequestParam String operator,
						 	@RequestParam String make,
						 	@RequestParam String model,
						 	@RequestParam int year,
						 	@RequestParam int numberOfSeats,
						 	@RequestParam double fuelEfficiency,
						 	@RequestParam String licensePlate) {

		if (areValidCarParameters(make, model, licensePlate)){
			Car car = new Car(operator, make, model, year, numberOfSeats, fuelEfficiency, licensePlate);
			sql.connect();
			if(sql.updateCar(car)) {
				sql.closeConnection();
				logger.info("Updated the car information which belongs to: " + operator);
				return convertToDto(car);
			}
			sql.closeConnection();
			logger.info("There was an incorrect parameter");
			return null;
		}
		return null;
	}

	/**
	 * Fetches a Car from the database via the Driver's operator name
	 * @param operator 		 -		A String representing the Driver's operator name
	 * @return A CarDto object if a Driver linked to the operator name is present in the database.
	 * Otherwise return Null.
	 */
	@GetMapping("")
	public CarDto getCar(@RequestParam String operator) {
		sql.connect();
		if (operator != null){
			Car car = sql.getCarByOperator(operator);
			sql.closeConnection();
			logger.info("Successfully retrieved the car of operator: " + operator);
			return convertToDto(car);
		}
		sql.closeConnection();
		logger.info("There was no operator value");
		return null;
	}

	/**
	 * Fetches all the Cars from the database
	 * @return An ArrayList of CarDto object(s) if there are cars are found in the
	 * database. Otherwise returns Null
	 */
	@GetMapping("/all")
	public List<CarDto> getAllCar() {
		sql.connect();
		List<CarDto> cars = new ArrayList<>();
		for(Car car : sql.getAllCars())
			cars.add(convertToDto(car));
		sql.closeConnection();
		logger.info("Successfully got a list of all the cars");
		logger.info(cars);
		return cars;
	}

	/**
	 * Validates that the input only contains letters from the alphabet
	 * @param word		 -		The String which needs to be processed for validation
	 * @return True if the word contains only letters in the alphabet. False otherwise
	 */
	private static Boolean isOnlyAlphabetic(String word){
		Pattern special = Pattern.compile("[^A-Za-z ]");
		Matcher hasSpecial = special.matcher(word);
		return !hasSpecial.find();
	}

	/**
	 * Validates that the input is alphanumeric
	 * @param word		 -		The String which needs to be processed for validation
	 * @return True if the word is alphanumeric. False otherwise
	 */
	private static Boolean isAlphaNumeric(String word){
		Pattern special = Pattern.compile("[^A-Za-z0-9 ]");
		Matcher hasSpecial = special.matcher(word);
		return !hasSpecial.find();
	}

	/**
	 * Validates the parameters of the Car
	 * @param make			 -		A String representing the make of the Car
	 * @param model			 -		A String representing the model of the Car
	 * @param licensePlate	 -		A String representing the license plate associated to the Car
	 * @return True if the parameters follow the proper structure. Otherwise False.
	 */
	private static Boolean areValidCarParameters(String make, String model, String licensePlate){
		if(!isOnlyAlphabetic(make)){
			logger.info("Your make " + make + " must contain only letters in the alphabet");
			return false;
		}

		if (!isAlphaNumeric(model)){
			logger.info(("Your car model " + model + " must not contain special characters"));
			return false;
		}

		if (!isAlphaNumeric(licensePlate)){
			logger.info("Your license plate " + licensePlate + " must not contain special characters");
			return false;
		}
		return true;
	}
}