package com.ecse321.team10.riderz.dto;

import java.sql.Timestamp;

public class ItineraryDto {
	private int tripID;
	private double startingLongitude;
	private double startingLatitude;
	private Timestamp startingTime;
	private double endingLongitude;
	private double endingLatitude;
	private Timestamp endingTime;
	private int seatsLeft;

	public ItineraryDto (int tripID, double startingLongitude, double startingLatitude,
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

}
