package com.ecse321.team10.riderz.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.model.Car;

public class MySQLJDBC {
	private static final Logger logger = LogManager.getLogger(MySQLJDBC.class);
	
	private static Connection c;
	
	//=======================
	// CONNECTIONS
	//=======================
	
	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//this holds dummy values for now
	        c = DriverManager.getConnection("jdbc:mysql://localhost/feedback?user=sqluser&password=sqluserpw");
	        logger.info("Connection to the database has been established.");
	        Statement stmt = c.createStatement();
	        
	        String sqlCars = 		"CREATE TABLE IF NOT EXISTS car " 					+
		        					" (carID 			INT PRIMARY KEY		NOT NULL," 		+
		        					"  operator 		varchar(40) 		PRIMARY KEY NOT NULL," 	+
		        					"  make 			varchar(45) 		NOT NULL," 		+
		        					"  model 			varchar(45) 		NOT NULL," 		+
		        					"  year 			year(4) 			NOT NULL," 		+
		        					"  numOfSeats 		int(11) 			NOT NULL," 		+
		        					"  fuelEfficiency 	float	 			NOT NULL)";
	        
	        String sqlDrivers = 	"CREATE TABLE IF NOT EXISTS driver " 			+
									" (operator 		varchar(40) 		PRIMARY KEY		NOT NULL," 	+
									"  rating 			float				NOT NULL," 	+
									"  personsRate 		int(11)		 		NOT NULL," 	+
									"  tripsCompleted 	int(11)		 		NOT NULL)" ;
							        
	        String sqlStop = 		"CREATE TABLE IF NOT EXISTS stop " 			+
	        						" (tripID 		int(11) PRIMARY KEY		NOT NULL," 	+
	        						"  stopNumber 	int(11)				NOT NULL," 	+
	        						"  address 		varchar(255) 		NOT NULL," 	+
	        						"  city		 	varchar(45) 		NOT NULL," 	+
	        						"  province 	varchar(45) 		NOT NULL," 	+
	        						"  country		varchar(45)			NOT NULL,"	+
	        						"  time 		datetime			NOT NULL)";

	        
	        String sqlTrip = 		"CREATE TABLE IF NOT EXISTS trip " 			+
									" (tripID 		INT(11) PRIMARY KEY	NOT NULL," 	+
									"  operator 	varchar(40)			NOT NULL)" ;
	        
	        String sqlUsers = 		"CREATE TABLE IF NOT EXISTS users " 			+
									" (username 	varchar(40) PRIMARY KEY		NOT NULL," 	+
									"  password 	varchar(64)			NOT NULL," 	+
									"  email 		varchar(384) 		NOT NULL," 	+
									"  phone	 	varchar(15) 		NOT NULL," 	+
									"  firstName 	varchar(90) 		NOT NULL," 	+
									"  lastName		varchar(90)			NOT NULL)";
	        
	        stmt.executeUpdate(sqlCars);
	        stmt.executeUpdate(sqlDrivers);
	        stmt.executeUpdate(sqlStop);
	        stmt.executeUpdate(sqlTrip);
	        stmt.executeUpdate(sqlUsers);
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
	
	//***Note concerning the by ID and by operator, this is so that we have the choice to perform operations on
	//a car based on its unique driver or its unique ID. We need only one of the types.
	
	public boolean insertCar(String operator, int make, String model, int year, int numOfSeats, 
			double fuelEfficiency) {
		String insertCar = String.format("INSERT INTO car (operator, make, model, year, numOfSeats, fuelEfficiency) "
				+ "VALUES (%s, %s, %s, %d, %d, %f)", operator, make, model, year, numOfSeats, fuelEfficiency);
		try {
			if(c.createStatement().executeUpdate(insertCar) <= 0)
				return false;
			return true;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public boolean deleteCarByID(int carID) {
		String deleteCar = String.format("DELETE FROM car WHERE carID = %d ", carID);
		try {
			if(c.createStatement().executeUpdate(deleteCar) <= 0)
				return false;
			return true;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public boolean updateCarByID(int carID, String operator, int make, String model, int year, int numOfSeats, 
			double fuelEfficiency) {
		String updateCar = String.format( "UPDATE car SET operator = '%s', make = '%s', model = '%s', year = '%d', "
				+ "numOfSeats = '%d', fuelEfficiency = '%f' WHERE carID = '%d';", operator, make, model, 
				year, numOfSeats, fuelEfficiency, carID);
		try {
			if(c.createStatement().executeUpdate(updateCar) <= 0)
				return false;
			return true;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	public boolean updateCarByOperator(String operator, int make, String model, int year, int numOfSeats, 
			double fuelEfficiency) {
		String updateCar = String.format( "UPDATE car make = '%s', model = '%s', year = '%d', "
				+ "numOfSeats = '%d', fuelEfficiency = '%f' WHERE operator = '%d';", make, model, 
				year, numOfSeats, fuelEfficiency, operator);
		try {
			if(c.createStatement().executeUpdate(updateCar) <= 0)
				return false;
			return true;
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
            while (rs.next())
                carList.add(new Car(rs.getInt("carID"), rs.getString("operator"), rs.getString("make"), 
                		rs.getString("model"), rs.getInt("year"), rs.getInt("numOfSeats"), 
                		rs.getDouble("fuelEfficiency")));
            rs.close();
            return carList;
        } catch (Exception e) {
        	logger.error(e.getClass().getName() + ": " + e.getMessage());
        	return null;
        }
        
    } 
	
	public Car getCarByID(int carID) {
        Car car = null;
        try {
            ResultSet rs = c.createStatement().executeQuery(String.format("SELECT * FROM car WHERE carID = '%d';", 
            		carID));
            while (rs.next())
                car = new Car(rs.getInt("carID"), rs.getString("operator"), rs.getString("make"), 
                		rs.getString("model"), rs.getInt("year"), rs.getInt("numOfSeats"), 
                		rs.getDouble("fuelEfficiency"));
            rs.close();
            return car;
        } catch (Exception e) {
        	logger.error(e.getClass().getName() + ": " + e.getMessage());
        	return null;
        }
    }
	
	public Car getCarByOperator(String operator) {
        Car car = null;
        try {
            ResultSet rs = c.createStatement().executeQuery(String.format("SELECT * FROM car WHERE operator = '%s';", 
            		operator));
            while (rs.next())
                car = new Car(rs.getInt("carID"), rs.getString("operator"), rs.getString("make"), 
                		rs.getString("model"), rs.getInt("year"), rs.getInt("numOfSeats"), 
                		rs.getDouble("fuelEfficiency"));
            rs.close();
            return car;
        } catch (Exception e) {
        	logger.error(e.getClass().getName() + ": " + e.getMessage());
        	return null;
        }
    }
	
	public boolean deleteCarByOpeartor(int operator) {
		String deleteCar = String.format("DELETE FROM car WHERE operator = %d ", operator);
		try {
			if(c.createStatement().executeUpdate(deleteCar) <= 0)
				return false;
			return true;
		}
		catch(Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
}
