package com.ecse321.team10.riderz.model;

public class Reservation {
	private int tripID;
	private int stopNumber;
	private String operator;

	public Reservation(int tripID, int stopNumber, String operator) {
		this.tripID = tripID;
		this.stopNumber = stopNumber;
		this.operator = operator;
	}

	public int getTripID() {
		return this.tripID;
	}

	public int stopNumber() {
		return this.stopNumber;
	}

	public String getOperator() {
		return this.operator;
	}

	public String toString() {
		return "[Reservation] TripID: " + this.tripID + " Stop Number: " + this.stopNumber + 
			   " Operator: " + this.operator;
	}
}
