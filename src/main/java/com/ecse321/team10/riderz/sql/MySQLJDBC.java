package com.ecse321.team10.riderz.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;

import java.security.MessageDigest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.model.Car;

public class MySQLJDBC {
	private static final Logger logger = LogManager.getLogger(MySQLJDBC.class);
	
	private static final String connection = "jdbc:mysql://35.237.200.65:3306/dev";
	private static final String username = "dev";
	private static final String password = "ecse321LoGiNdEv";

	private static Connection c;
	
	//=======================
	// CONNECTIONS
	//=======================
	
	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//this holds dummy values for now
	        c = DriverManager.getConnection(connection, username, password);
	        logger.info("Connection to the database has been established.");
	        Statement stmt = c.createStatement();
	        return true;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean closeConnection() {
		try {
			if (c != null) {
                c.close();
                System.out.println("Connection to the database has been closed.");
                return true;
            }
			return false;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	//=======================
	// CARS API
	//=======================

	public boolean insertCar(String operator, String make, String model, int year,
							 int numOfSeats, double fuelEfficiency, String licensePlate) {
		String insertCar = "INSERT INTO car (operator, make, model, year, numOfSeats, " +
						   "fuelEfficiency, licensePlate) VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertCar);
			ps.setString(1, operator);
			ps.setString(2, make);
			ps.setString(3, model);
			ps.setInt(4, year);
			ps.setInt(5, numOfSeats);
			ps.setDouble(6, fuelEfficiency);
			ps.setString(7, licensePlate);
			if (ps.executeUpdate() == 1) {
				ps.close();
				return true;
			}
			return false;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public boolean deleteCar(String operator) {
		String deleteCar = "DELETE FROM car WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteCar);
			ps.setString(1, operator);
			if (ps.executeUpdate() == 1) {
				ps.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public boolean updateCar(String operator, String make, String model, int year, 
							 int numOfSeats, double fuelEfficiency, String licensePlate) {
		String updateCar = "UPDATE car SET make = ?, model = ?, year = ?, numOfSeats = ?, " +
					   	   "fuelEfficiency = ?, licensePlate = ? WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(updateCar);
			ps.setString(1, make);
			ps.setString(2, model);
			ps.setInt(3, year);
			ps.setInt(4, numOfSeats);
			ps.setDouble(5, fuelEfficiency);
			ps.setString(6, licensePlate);
			ps.setString(7, operator);
			if (ps.executeUpdate() == 1) {
				ps.close();
				return true;
			}
			return false;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public ArrayList<Car> getAllCars() {
        ArrayList<Car> carList = new ArrayList<Car>();
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM car;");
            while (rs.next()) {
                carList.add(new Car(rs.getString("operator"), rs.getString("make"), 
                		rs.getString("model"), rs.getInt("year"), rs.getInt("numOfSeats"), 
                		rs.getDouble("fuelEfficiency"), rs.getString("licensePlate")));
			}
            rs.close();
            return carList;
        } catch (Exception e) {
        	logger.error(e.getClass().getName() + ": " + e.getMessage());
        	return null;
        }
        
    } 
	
	public Car getCarByOperator(String operator) {
		String query = "SELECT * FROM car WHERE operator = ?;";
        Car car = null;
		PreparedStatement ps = null;
        try {
			ps = c.prepareStatement(query);
			ps.setString(1, operator);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                car = new Car(rs.getString("operator"), rs.getString("make"), 
                			  rs.getString("model"), rs.getInt("year"), 
							  rs.getInt("numOfSeats"), rs.getDouble("fuelEfficiency"), 
							  rs.getString("licensePlate"));
			}
            ps.close();
            return car;
        } catch (Exception e) {
        	logger.error(e.getClass().getName() + ": " + e.getMessage());
        	return null;
        }
    }

	//=======================
	// USERS API
	//=======================
	public boolean createUser(String username, String password, String email,
							  String phone, String firstName, String lastName) {
		return false;
	}

	public boolean deleteUser(String username) {
		return false;
	}

	public boolean verifyLogin(String username, String password) {
		return false;
	}

	public String getEmail(String username) {
		return null;
	}

	public boolean setEmail(String username, String email) {
		return false;
	}

	private String getPassword(String username) {
		return null;
	}

	public boolean setPassword(String username, String password) {
		return false;
	}

	//=======================
	// DRIVER API
	//=======================
	public boolean createDriver(String operator) {
		return false;
	}

	public boolean deleteDriver() {
		return false;
	}

	public double getRating() {
		return 0.0;
	}

	public boolean addRating(String operator, double rating) {
		return false;
	}

	public boolean clearRating(String operator) {
		return false;
	}

	public int getTripCompleted() {
		return 0;
	}

	public boolean incrementTripCompleted() {
		return false;
	}
}
