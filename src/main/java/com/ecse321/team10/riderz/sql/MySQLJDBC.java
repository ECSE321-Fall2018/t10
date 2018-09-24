package com.ecse321.team10.riderz.sql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.model.CarClass;


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
	
	public boolean insertCar(String operator, int make, String model, int year, int numOfSeats, double fuelEfficiency) {
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
