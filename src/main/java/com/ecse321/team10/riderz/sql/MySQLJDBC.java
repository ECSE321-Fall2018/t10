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
	        
	        String sqlCars = 		"CREATE TABLE IF NOT EXISTS cars " 				+
		        					" (carId 		INT PRIMARY KEY		NOT NULL," 	+
		        					"  seats 		INT 				NOT NULL," 	+
		        					"  model 		VARCHAR(50) 		NOT NULL," 	+
		        					"  year 		INT 				NOT NULL," 	+
		        					"  gasRate 		double 				NOT NULL," 	+
		        					"  carClass 	VARCHAR(50) 		NOT NULL";
	        
	        String sqlDrivers = 	"CREATE TABLE IF NOT EXISTS users " 			+
									" (UserId 		INT PRIMARY KEY		NOT NULL," 	+
									"  name 		VARCHAR(50)			NOT NULL," 	+
									"  email 		VARCHAR(355) 		NOT NULL," 	+
									"  password 	VARCHAR(20) 		NOT NULL," 	+
									"  ratings 		double 				NOT NULL," 	+
									"  car		 	INT			 		NOT NULL,"	+
									"  destination 	VARCHAR(255)		NOT NULL,"	+
									"  endStop 		VARCHAR(255)		NOT NULL";
	        
	        String sqlPassengers = 	"CREATE TABLE IF NOT EXISTS users " 			+
	        						" (UserId 		INT PRIMARY KEY		NOT NULL," 	+
	        						"  name 		VARCHAR(50)			NOT NULL," 	+
	        						"  email 		VARCHAR(355) 		NOT NULL," 	+
	        						"  password 	VARCHAR(20) 		NOT NULL," 	+
	        						"  ratings 		double 				NOT NULL," 	+
	        						"  price		double			 			,"	+
	        						"  route 		INT					NOT NULL";

	        
	        String sqlRatings = 	"CREATE TABLE IF NOT EXISTS ratings " 			+
									" (UserId 		INT PRIMARY KEY		NOT NULL," 	+
									"  userRate 	VARCHAR(50)			NOT NULL," 	+
									"  ratings 		double 				NOT NULL";
	        
	        stmt.executeUpdate(sqlCars);
	        stmt.executeUpdate(sqlDrivers);
	        stmt.executeUpdate(sqlPassengers);
	        stmt.executeUpdate(sqlRatings);
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
	
	public boolean insertCar(int carId, int seats, String model, int year, int gasRate, CarClass carClass) {
		String insertCar = String.format("INSERT INTO cars (carId, seats, model, year, gasRate, carClass) "
				+ "VALUES (%d, %s, %d, %d, %s)", carId, seats, model, year, gasRate, carClass);
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
	
	public boolean deleteCar(int carId) {
		String deleteCar = String.format("DELETE FROM cars WHERE carId = %d ", carId);
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
