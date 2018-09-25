package com.ecse321.team10.riderz.model;

public class Driver {
	private String operator;
	private double rating;
	private int personsRated;
	private int tripsCompleted;

	public Driver(String operator, double rating, int personsRated, int tripsCompleted) {
		this.operator = operator;
		this.rating = rating;
		this.personsRated = personsRated;
		this.tripsCompleted = tripsCompleted;
	}

	public String getOperator() {
		return this.operator;
	}

	public double getRating() {
		return this.rating;
	}

	public int getPersonsRated() {
		return this.personsRated;
	}

	public int getTripsCompleted() {
		return this.tripsCompleted;
	}

	public String toString() {
		return "[Driver] Operator: " + this.operator + " Rating: " + this.rating +
			   " Persons Rated: " + this.personsRated + " Trips Completed: " +
			   this.tripsCompleted;
	}
}
