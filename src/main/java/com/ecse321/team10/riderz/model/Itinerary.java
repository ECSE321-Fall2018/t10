package com.ecse321.team10.riderz.model;

import java.sql.Timestamp;

/**
 * An object representing an itinerary entry.
 * @version 1.0
 */
public class Itinerary {
	private int tripID;
	private double startingLongitude;
	private double startingLatitude;
	private Timestamp startingTime;
	private double endingLongitude;
	private double endingLatitude;
	private Timestamp endingTime;
	private int seatsLeft;

	public Itinerary (int tripID, double startingLongitude, double startingLatitude,
				   	Timestamp startingTime, double endingLongitude,
					double endingLatitude, Timestamp endingTime, int seatsLeft) {
		this.tripID = tripID;
		this.startingLongitude = startingLongitude;
		this.startingLatitude = startingLatitude;
		this.startingTime = startingTime;
		this.endingLongitude = endingLongitude;
		this.endingLatitude = endingLatitude;
		this.endingTime = endingTime;
		this.seatsLeft = seatsLeft;
	}

	public int getTripID() {
		return this.tripID;
	}

	public double getStartingLongitude() {
		return this.startingLongitude;
	}

	public double getStartingLatitude() {
		return this.startingLatitude;
	}

	public Timestamp getStartingTime() {
		return this.startingTime;
	}

	public double getEndingLongitude() {
		return this.endingLongitude;
	}

	public double getEndingLatitude() {
		return this.endingLatitude;
	}

	public Timestamp getEndingTime() {
		return this.endingTime;
	}

	public int getSeatsLeft() {
		return this.seatsLeft;
	}

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[Itinerary] TripID: " + this.tripID + " Starting Longitude: " +
			   this.startingLongitude + " Starting Latitude: " + this.startingLatitude +
			   " Starting Time: " + this.startingTime.toString() + 
			   " Ending Longitude: " + this.endingLongitude + " Ending Latitude: " +
			   this.endingLatitude + " Ending Time: " + this.endingTime.toString() + 
			   " Seats Left: " + this.seatsLeft;
	}
}
