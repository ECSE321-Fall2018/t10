package com.ecse321.team10.riderz.model;

/**
 * An object representing a car entry.
 * @version 1.0
 */
public class Car {
	private String operator;
	private String make;
	private String model;
	private int year;
	private int numOfSeats;
	private double fuelEfficiency;
	private String licensePlate;
	
	public Car(String operator, String make, String model, int year, int numOfSeats,
			   double fuelEfficiency, String licensePlate) {
		this.operator = operator;
		this.make = make;
		this.model = model;
		this.year = year;
		this.numOfSeats = numOfSeats;
		this.fuelEfficiency = fuelEfficiency;
		this.licensePlate = licensePlate;
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

	public double getFuelEfficiency() {
		return this.fuelEfficiency;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[Car] Operator: " + this.operator + " Make: " + this.make +
			   " Model: " + this.model + " Year: " + this.year +
			   " Number Of Seats: " + this.numOfSeats + " Fuel Efficiency: " +
			   this.fuelEfficiency + " License Plate: " + this.licensePlate;
	}
}
