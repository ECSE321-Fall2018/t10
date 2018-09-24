package com.ecse321.team10.riderz.model;

public class Car {
	private int carID;
	private String operator;
	private String make;
	private String model;
	private int year;
	private int numOfSeats;
	private double fuelEfficiency;
	
	public Car(int carID, String operator, String make, String model, int year, int numOfSeats,
			double fuelEfficiency) {
		this.carID = carID;
		this.operator = operator;
		this.make = make;
		this.model = model;
		this.year = year;
		this.numOfSeats = numOfSeats;
		this.fuelEfficiency = fuelEfficiency;
	}
	
	public int getCarID() {
		return this.carID;
	}
	public String getOperator() {
		return this.operator;
	}
	public String getMake() {
		return this.make;
	}
	public String getModel() {
		return this.model;
	}
	public int getYear() {
		return this.year;
	}
	public int getNumOfSeats() {
		return this.numOfSeats;
	}
	public double getfuelEfficiency() {
		return this.fuelEfficiency;
	}
}
