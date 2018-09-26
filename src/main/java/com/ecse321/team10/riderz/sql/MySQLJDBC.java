package com.ecse321.team10.riderz.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.model.Driver;
import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.User;

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
	public boolean insertUser(String username, String password, String email,
							  String phone, String firstName, String lastName) {
		String insertUser = "INSERT INTO users (username, password, email, phone, " +
							"firstName, lastName) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertUser);
			ps.setString(1, username);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			ps.setString(2, DatatypeConverter.printHexBinary(hash));
			ps.setString(3, email);
			ps.setString(4, phone);
			ps.setString(5, firstName);
			ps.setString(6, lastName);
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

	public boolean deleteUser(String username) {
		String deleteUser = "DELETE FROM users WHERE username = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteUser);
			ps.setString(1, username);
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

	public User getUserByUsername(String username) {
		String getUser = "SELECT * FROM users WHERE username = ?;";
		User user = null;
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getUser);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"),
								rs.getString("email"), rs.getString("phone"),
								rs.getString("firstName"), rs.getString("lastName"));
			}
			ps.close();
			return user;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public ArrayList<User> getAllUsers() {
		ArrayList<User> userList = new ArrayList<User>();
		try {
			ResultSet rs = c.createStatement().executeQuery("SELECT * FROM users;");
			while (rs.next()) {
				userList.add(new User(rs.getString("username"), rs.getString("password"),
						rs.getString("email"), rs.getString("phone"),
						rs.getString("firstName"), rs.getString("lastName")));
			}
			rs.close();
			return userList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public boolean verifyLogin(String username, String password) {
		String verifyLogin = "SELECT username FROM users WHERE username = ? AND password = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(verifyLogin);
			ps.setString(1, username);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			ps.setString(2, DatatypeConverter.printHexBinary(hash));
			ResultSet rs = ps.executeQuery();
			rs.last();
			if (rs.getRow() == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public String getEmail(String username) {
		String getEmail = "SELECT email FROM users WHERE username = ?;";
		PreparedStatement ps = null;
		String email = null;
		try {
			ps = c.prepareStatement(getEmail);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				email = rs.getString("email");
			}
			ps.close();
			return email;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public boolean setEmail(String username, String email) {
		String setEmail = "UPDATE users SET email = ? WHERE username = ?;";
	   	PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(setEmail);
			ps.setString(1, email);
			ps.setString(2, username);
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

	public boolean setPassword(String username, String password) {
		String setPassword = "UPDATE users SET password = ? WHERE username = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(setPassword);

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			ps.setString(1, DatatypeConverter.printHexBinary(hash));
			ps.setString(2, username);
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

	//=======================
	// DRIVER API
	//=======================
	public boolean insertDriver(String operator) {
		String insertDriver = "INSERT INTO driver (operator, rating, personsRated, " +
							  "tripsCompleted) VALUES (?, 0.0, 0, 0);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertDriver);
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

	public boolean deleteDriver(String operator) {
		String deleteDriver = "DELETE FROM driver WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteDriver);
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

	public Driver getDriverByUsername(String operator) {
		String getDriver = "SELECT * FROM driver WHERE operator = ?;";
		Driver driver = null;
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getDriver);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				driver = new Driver(rs.getString("operator"), rs.getDouble("rating"),
									rs.getInt("personsRated"), rs.getInt("tripsCompleted"));
			}
			ps.close();
			return driver;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public ArrayList<Driver> getAllDrivers() {
		ArrayList<Driver> driverList = new ArrayList<Driver>();
		try {
			ResultSet rs = c.createStatement().executeQuery("SELECT * FROM driver;");
			while (rs.next()) {
				driverList.add(new Driver(rs.getString("operator"), rs.getDouble("rating"),
										  rs.getInt("personsRated"), rs.getInt("tripsCompleted")));
			}
			rs.close();
			return driverList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public double getRating(String operator) {
		String getRating = "SELECT rating FROM driver WHERE operator = ?;";
		PreparedStatement ps = null;
		double rating = 0.0;
		try {
			ps = c.prepareStatement(getRating);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rating = rs.getDouble("rating");
			}
			ps.close();
			return rating;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return 0.0;
		}
	}

	public boolean addRating(String operator, double rating) {
		String addRating = "UPDATE driver SET rating = ? WHERE operator = ?;";
		double oldRating = getRating(operator);
		int personsRated = getPersonsRated(operator);
		PreparedStatement ps = null;
		try {
			double newRating = ((personsRated * oldRating) + rating) / (personsRated + 1);
			ps = c.prepareStatement(addRating);
			ps.setDouble(1, newRating);
			ps.setString(2, operator);
			if (ps.executeUpdate() == 1 && 
				incrementPersonsRated(operator)) {

				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public int getPersonsRated(String operator) {
		String getPersonsRated = "SELECT personsRated FROM driver WHERE operator = ?;";
		PreparedStatement ps = null;
		int personsRated = 0;
		try {
			ps = c.prepareStatement(getPersonsRated);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				personsRated = rs.getInt("personsRated");
			}
			ps.close();
			return personsRated;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	public boolean incrementPersonsRated(String operator) {
		String incrementPersonsRated = "UPDATE driver SET personsRated = personsRated + 1 " +
									   "WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(incrementPersonsRated);
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

	public int getTripsCompleted(String operator) {
		String getTripsCompleted = "SELECT tripsCompleted FROM driver WHERE operator = ?;";
		PreparedStatement ps = null;
		int tripsCompleted = 0;
		try {
			ps = c.prepareStatement(getTripsCompleted);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				tripsCompleted = rs.getInt("tripsCompleted");
			}
			ps.close();
			return tripsCompleted;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	public boolean incrementTripsCompleted(String operator) {
		String incrementTripsCompleted = "UPDATE driver SET tripsCompleted = tripsCompleted + 1 " +
									   	 "WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(incrementTripsCompleted);
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
}
