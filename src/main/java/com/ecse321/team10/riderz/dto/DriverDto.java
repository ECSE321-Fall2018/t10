package com.ecse321.team10.riderz.dto;

public class DriverDto {
	private String operator;
	private double rating;
	private int personsRated;
	private int tripsCompleted;
	
	public DriverDto() {
	}
	
	public DriverDto(String operator, double rating, int personsRated, int tripsCompleted) {
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
	
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public void setPersonsRated(int personsRated) {
		this.personsRated = personsRated;
	}
	
	public void setTripsCompleted(int tripsCompleted) {
		this.tripsCompleted = tripsCompleted;
	}
}
