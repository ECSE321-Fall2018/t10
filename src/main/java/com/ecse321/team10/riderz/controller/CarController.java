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

@RestController
public class CarController {

	@Autowired
	private MySQLJDBC sql;

	@Autowired
	private ModelMapper modelMapper;

	private CarDto convertToDto(Car car) { return modelMapper.map(car, CarDto.class); }

	private static final Logger logger = LogManager.getLogger(RiderzController.class);

	@PostMapping("")
	public CarDto addCar(@RequestParam String operator,
						 @RequestParam String make,
						 @RequestParam String model,
						 @RequestParam int year,
						 @RequestParam int numberOfSeats,
						 @RequestParam double fuelEfficiency,
						 @RequestParam String licensePlate) {

		if(!isOnlyAlphabetic(make)){
			logger.info("Your make " + make + " must contain only letters in the alphabet");
			return null;
		}

		if (!isAlphaNumeric(model)){
			logger.info(("Your car model " + model + " must not contain special characters"));
			return null;
		}

		if (!isAlphaNumeric(licensePlate)){
			logger.info("Your license plate " + licensePlate + " must not contain special characters");
			return null;
		}

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

	@PutMapping("")
	public CarDto updateCar(@RequestParam String operator,
						 	@RequestParam String make,
						 	@RequestParam String model,
						 	@RequestParam int year,
						 	@RequestParam int numberOfSeats,
						 	@RequestParam double fuelEfficiency,
						 	@RequestParam String licensePlate) {

		if(!isOnlyAlphabetic(make)){
			logger.info("Your make " + make + " must contain only letters in the alphabet");
			return null;
		}

		if (!isAlphaNumeric(model)){
			logger.info(("Your car model " + model + " must not contain special characters"));
			return null;
		}

		if (!isAlphaNumeric(licensePlate)){
			logger.info("Your license plate " + licensePlate + " must not contain special characters");
			return null;
		}

		Car car = new Car(operator, make, model, year, numberOfSeats, fuelEfficiency, licensePlate);
		sql.connect();
		if(sql.updateCar(car)) {
			sql.closeConnection();
			logger.info("Update the car information which belongs to: " + operator);
			return convertToDto(car);
		}
		sql.closeConnection();
		logger.info("There was an incorrect parameter");
		return null;
	}

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

	@GetMapping("/all")
	public List<CarDto> getAllCar() {
		sql.connect();
		List<CarDto> cars = new ArrayList<CarDto>();
		for(Car car : sql.getAllCars())
			cars.add(convertToDto(car));
		sql.closeConnection();
		logger.info("Successfully got a list of all the cars");
		return cars;
	}

	private static Boolean isOnlyAlphabetic(String word){
		Pattern special = Pattern.compile("[^A-Za-z ]");
		Matcher hasSpecial = special.matcher(word);
		return !hasSpecial.find();
	}

	private static Boolean isAlphaNumeric(String word){
		Pattern special = Pattern.compile("[^A-Za-z0-9 ]");
		Matcher hasSpecial = special.matcher(word);
		return !hasSpecial.find();
	}
}