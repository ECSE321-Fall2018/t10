package com.ecse321.team10.riderz.model;

/**
 * An object representing a trip entry.
 * @version 1.0
 */
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

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[Trip] TripID: " + this.tripID + " Operator: " + this.operator;
	}
}
