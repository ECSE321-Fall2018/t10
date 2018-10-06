package com.ecse321.team10.riderz.dto;

public class ReservationDto {
	private int tripID;
	private String operator;

	public ReservationDto() {
	}
	
	public ReservationDto(String operator, int tripID) {
		this.operator = operator;
		this.tripID = tripID;
	}

	public String getOperator() {
		return this.operator;
	}

	public int getTripID() {
		return this.tripID;
	}
	
	public void setTripID(int tripID) {
		this.tripID = tripID;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
