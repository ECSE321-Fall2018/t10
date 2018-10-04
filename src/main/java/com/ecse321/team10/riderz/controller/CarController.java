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
		make = alphabetFilter(make);
		model = specialCharacterFilter(model);
		licensePlate = specialCharacterFilter(licensePlate);
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
	public void deleteCar(@RequestParam String operator) {
		sql.connect();
		if (operator != null){
			sql.deleteCar(operator);
			sql.closeConnection();
			logger.info("Successfully deleted the car owned by: " + operator);
		}else{
			sql.closeConnection();
			logger.info("There was no operator");
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
		make = alphabetFilter(make);
		model = specialCharacterFilter(model);
		licensePlate = specialCharacterFilter(licensePlate);
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

	private static String alphabetFilter(String word){
		return word.replaceAll("[^A-Za-z ]", "");
	}

	private static String specialCharacterFilter(String word){
		return word.replaceAll("[^A-Za-z0-9 ]", "");
	}
}