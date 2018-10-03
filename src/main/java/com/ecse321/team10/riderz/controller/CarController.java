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

@RestController
public class CarController {

	@Autowired
	private MySQLJDBC sql;

	@Autowired
	private ModelMapper modelMapper;

	private CarDto convertToDto(Car car) { return modelMapper.map(car, CarDto.class); }

	private static final Logger logger = LogManager.getLogger(RiderzController.class);

	@PostMapping("/insertCar/{operator}")
	public CarDto addCar(@PathVariable("operator") String operator,
						 @RequestParam String make,
						 @RequestParam String model,
						 @RequestParam int year,
						 @RequestParam int numberOfSeats,
						 @RequestParam double fuelEfficiency,
						 @RequestParam String licensePlate) {
		Car car = new Car(operator, make, model, year, numberOfSeats, fuelEfficiency, licensePlate);
		sql.connect();
		if(sql.insertCar(car)) {
			sql.closeConnection();
			return convertToDto(car);
		}
		sql.closeConnection();
		return null;
	}

	@DeleteMapping("/deleteCar/{operator}")
	public CarDto deleteCar(@PathVariable("operator") String operator) {
		sql.connect();
		sql.deleteCar(operator);
		sql.closeConnection();
		return null;
	}

	@PutMapping("/updateCar/{operator}")
	public CarDto updateCar(@PathVariable("operator") String operator,
						 	@RequestParam String make,
						 	@RequestParam String model,
						 	@RequestParam int year,
						 	@RequestParam int numberOfSeats,
						 	@RequestParam double fuelEfficiency,
						 	@RequestParam String licensePlate) {
		Car car = new Car(operator, make, model, year, numberOfSeats, fuelEfficiency, licensePlate);
		sql.connect();
		if(sql.updateCar(car)) {
			sql.closeConnection();
			return convertToDto(car);
		}
		sql.closeConnection();
		return null;
	}

	@GetMapping("/getCar/{operator}")
	public CarDto getCar(@PathVariable("operator") String operator) {
		sql.connect();
		Car car = sql.getCarByOperator(operator);
		sql.closeConnection();
		return convertToDto(car);
	}

	@GetMapping("/getAllCars")
	public List<CarDto> getAllCar() {
		sql.connect();
		List<CarDto> cars = new ArrayList<CarDto>();
		for(Car car : sql.getAllCars())
			cars.add(convertToDto(car));
		sql.closeConnection();
		return cars;
	}
}