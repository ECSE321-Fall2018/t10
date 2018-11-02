package com.ecse321.team10.riderz.dto;

public class CarDto {
    private String operator;
    private String make;
    private String model;
    private int year;
    private int numOfSeats;
    private double fuelEfficiency;
    private String licensePlate;

    public CarDto(){

    }

    public CarDto(String operator, String make, String model, int year,
                  int numOfSeats, double fuelEfficiency, String licensePlate){
        this.operator = operator;
        this.make = make;
        this.model = model;
        this.year = year;
        this.numOfSeats = numOfSeats;
        this.fuelEfficiency = fuelEfficiency;
        this.licensePlate = licensePlate;
    }

    public String getOperator(){ return this.operator; }

    public String getMake() { return this.make; }

    public String getModel() { return this.model; }

    public int getYear() { return this.year; }

    public int getNumOfSeats() { return this.numOfSeats; }

    public double getFuelEfficiency() { return this.fuelEfficiency; }

    public String getLicensePlate() { return this.licensePlate; }

    public void setOperator(String operator) { this.operator = operator; }

    public void setMake(String make) { this.make = make; }

    public void setModel(String model) { this.model = model; }

    public void setYear(int year) { this.year = year; }

    public void setNumOfSeats(int numOfSeats) { this.numOfSeats = numOfSeats; }

    public void setFuelEfficiency(double fuelEfficiency) { this.fuelEfficiency = fuelEfficiency; }

    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

}
