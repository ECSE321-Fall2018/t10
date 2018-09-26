package com.ecse321.team10.riderz.model;

public class Trip {
	private int tripID;
	private String operator;
	public Trip(int tripID, String operator) {
		this.tripID = tripID;
		this.operator = operator;
	}

	public int getTripID() {
		return this.tripID;
	}

	public String getOperator() {
		return this.operator;
	}

	public String toString() {
		return "[Trip] TripID: " + this.tripID + " Operator: " + this.operator;
	}
}
