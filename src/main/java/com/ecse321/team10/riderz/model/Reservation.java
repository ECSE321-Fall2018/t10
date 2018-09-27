package com.ecse321.team10.riderz.model;

public class Reservation {
	private int tripID;
	private String operator;

	public Reservation(String operator, int tripID) {
		this.operator = operator;
		this.tripID = tripID;
	}

	public String getOperator() {
		return this.operator;
	}

	public int getTripID() {
		return this.tripID;
	}

	public String toString() {
		return "[Reservation] Operator: " + this.operator + " Trip ID: " + this.tripID;
	}
}
