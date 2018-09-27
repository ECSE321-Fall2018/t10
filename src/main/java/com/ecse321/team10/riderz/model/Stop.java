package com.ecse321.team10.riderz.model;

import java.sql.Timestamp;

public class Stop {
	private int tripID;
	private int stopNumber;
	private String address;
	private String city;
	private String province;
	private String country;
	private Timestamp timestamp;
	private int seatsLeft;

	public Stop(int tripID, int stopNumber, String address, String city, String province,
				String country, Timestamp timestamp, int seatsLeft) {
		this.tripID = tripID;
		this.stopNumber = stopNumber;
		this.address = address;
		this.city = city;
		this.province = province;
		this.country = country;
		this.timestamp = timestamp;
		this.seatsLeft = seatsLeft;
	}

	public int getTripID() {
		return this.tripID;
	}

	public int getStopNumber() {
		return this.stopNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}

	public String getProvince() {
		return this.province;
	}

	public String getCountry() {
		return this.country;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public int getSeatsLeft() {
		return this.seatsLeft;
	}

	public String toString() {
		return "[Stop] TripID: " + this.tripID + " Stop Number: " + this.stopNumber + " Address: " +
			   this.address + " City: " + this.city + " Province: " + this.province + " Country: " +
			   this.country + " Timestamp: " + this.timestamp.toString() + " Seats Left: " + this.seatsLeft;
	}
}
