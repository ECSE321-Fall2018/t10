package com.ecse321.team10.riderz.model;

/**
 * An object representing a reservation entry.
 * @version 1.0
 */
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

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[Reservation] Operator: " + this.operator + " Trip ID: " + this.tripID;
	}
}
