package com.ecse321.team10.riderz.dto;

public class TripDto {

    private int tripID;
    private String operator;

    public TripDto(){

    }
    public TripDto(int tripID, String operator){
        this.tripID = tripID;
        this.operator = operator;
    }

    public int getTripID() { return tripID; }

    public String getOperator() { return operator; }

    public void setTripID(int tripID) { this.tripID = tripID; }

    public void setOperator(String operator) { this.operator = operator; }

}