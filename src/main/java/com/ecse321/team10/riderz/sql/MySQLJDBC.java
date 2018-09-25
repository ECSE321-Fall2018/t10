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
