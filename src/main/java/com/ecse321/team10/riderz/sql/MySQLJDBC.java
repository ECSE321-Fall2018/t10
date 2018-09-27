package com.ecse321.team10.riderz.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ecse321.team10.riderz.model.Driver;
import com.ecse321.team10.riderz.model.Car;
import com.ecse321.team10.riderz.model.User;
import com.ecse321.team10.riderz.model.Trip;
import com.ecse321.team10.riderz.model.Iterary;
import com.ecse321.team10.riderz.model.Location;
import com.ecse321.team10.riderz.model.Reservation;

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

	public String getPhone(String username) {
		String getPhone = "SELECT phone FROM users WHERE username = ?;";
		PreparedStatement ps = null;
		String phone = null;
		try {
			ps = c.prepareStatement(getPhone);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				phone = rs.getString("phone");
			}
			ps.close();
			return phone;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public boolean setPhone(String username, String phone) {
		String setPhone = "UPDATE users SET phone = ? WHERE username = ?;";
	   	PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(setPhone);
			ps.setString(1, phone);
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

	//=======================
	// TRIP API
	//=======================
	public boolean insertTrip(String operator) {
		String insertTrip = "INSERT INTO trip(operator) VALUES (?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertTrip);
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

	public boolean deleteTrip(int tripID, String operator) {
		String deleteTrip = "DELETE FROM trip WHERE tripID = ? AND operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteTrip);
			ps.setInt(1, tripID);
			ps.setString(2, operator);
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

	public boolean deleteAllTrips(String operator) {
		String deleteAllTrips = "DELETE FROM trip WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteAllTrips);
			ps.setString(1, operator);
			if (ps.executeUpdate() != 0) {
				ps.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public ArrayList<Trip> getTripsByUsername(String operator) {
		ArrayList<Trip> tripList = new ArrayList<Trip>();
		String getTripsByUsername = "SELECT * FROM trip WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getTripsByUsername);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tripList.add(new Trip(rs.getInt("tripID"), rs.getString("operator")));
			}
			rs.close();
			return tripList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public Trip getLastTripByUsername(String operator) {
		String getLastTripByUsername = "SELECT * FROM trip WHERE operator = ? ORDER BY " +
									   "tripID DESC LIMIT 1;";
		PreparedStatement ps = null;
		Trip trip = null;
		try {
			ps = c.prepareStatement(getLastTripByUsername);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				trip = new Trip(rs.getInt("tripID"), rs.getString("operator"));
			}
			ps.close();
			return trip;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public ArrayList<Trip> getAllTrips() {
		ArrayList<Trip> tripList = new ArrayList<Trip>();
		try {
			ResultSet rs = c.createStatement().executeQuery("SELECT * FROM trip;");
			while (rs.next()) {
				tripList.add(new Trip(rs.getInt("tripID"), rs.getString("operator")));
			}
			rs.close();
			return tripList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	//=======================
	// Iterary API
	//=======================
	public boolean insertIterary(Iterary iterary) {
		String insertIterary = "INSERT INTO iterary(tripID, startingLongitude, " +
							   "startingLatitude, startingTime, endingLongitude, " +
							   "endingLatitude, endingTime, seatsLeft) " +
							   "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertIterary);
			ps.setInt(1, iterary.getTripID());
			ps.setDouble(2, iterary.getStartingLongitude());
			ps.setDouble(3, iterary.getStartingLatitude());
			ps.setTimestamp(4, iterary.getStartingTime());
			ps.setDouble(5, iterary.getEndingLongitude());
			ps.setDouble(6, iterary.getEndingLatitude());
			ps.setTimestamp(7, iterary.getEndingTime());
			ps.setInt(8, iterary.getSeatsLeft());
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

	public boolean updateIterary(Iterary iterary) {
		String updateIterary = "UPDATE iterary SET startingLongitude = ?, " +
							   "startingLatitude = ?, startingTime = ?, " +
							   "endingLongitude = ?, endingLatitude = ?, " +
							   "endingTime = ?, seatsLeft = ? WHERE tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(updateIterary);
			ps.setDouble(1, iterary.getStartingLongitude());
			ps.setDouble(2, iterary.getStartingLatitude());
			ps.setTimestamp(3, iterary.getStartingTime());
			ps.setDouble(4, iterary.getEndingLongitude());
			ps.setDouble(5, iterary.getEndingLatitude());
			ps.setTimestamp(6, iterary.getEndingTime());
			ps.setInt(7, iterary.getSeatsLeft());
			ps.setInt(8, iterary.getTripID());
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

	public boolean deleteIterary(int tripID) {
		String deleteStop = "DELETE FROM iterary WHERE tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteStop);
			ps.setInt(1, tripID);
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

	public Iterary getIteraryByTripID(int tripID) {
		Iterary iterary = null;
		String getIteraryByTripID = "SELECT * FROM iterary WHERE tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getIteraryByTripID);
			ps.setInt(1, tripID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				iterary = new Iterary(rs.getInt("tripID"),
									  rs.getDouble("startingLongitude"),
									  rs.getDouble("startingLatitude"),
									  rs.getTimestamp("startingTime"),
									  rs.getDouble("endingLongitude"),
									  rs.getDouble("endingLatitude"),
									  rs.getTimestamp("endingTime"),
									  rs.getInt("seatsLeft"));
			}
			rs.close();
			return iterary;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public ArrayList<Iterary> getIteraryNearDestination(double endingLongitude,
					double endingLatitude, double maximumDistance, Timestamp arrivalTime) {
		ArrayList<Iterary> iteraryList = new ArrayList<Iterary>();
		double radiusOfEarth = 6371000.0;
		String getIteraryNearDestination = "SELECT * FROM iterary WHERE SQRT(" +
						   "POWER((2 * PI() * ? * (ABS(endingLongitude - ?) / 360.0)), 2) + " +
						   "POWER((2 * PI() * ? * (ABS(endingLatitude - ?) / 360.0)), 2)) <= ? " +
						   "AND endingTime > NOW() AND endingTime <= ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getIteraryNearDestination);
			ps.setDouble(1, radiusOfEarth);
			ps.setDouble(2, endingLongitude);
			ps.setDouble(3, radiusOfEarth);
			ps.setDouble(4, endingLatitude);
			ps.setDouble(5, maximumDistance);
			ps.setTimestamp(6, arrivalTime);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				iteraryList.add(new Iterary(rs.getInt("tripID"),
											rs.getDouble("startingLongitude"),
										    rs.getDouble("startingLatitude"),
											rs.getTimestamp("startingTime"),
											rs.getDouble("endingLongitude"),
											rs.getDouble("endingLatitude"),
											rs.getTimestamp("endingTime"),
											rs.getInt("seatsLeft")));
			}
			ps.close();
			return iteraryList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public boolean incrementSeatsLeft(int tripID) {
		String incrementSeatsLeft = "UPDATE iterary SET seatsLeft = seatsLeft + 1 " +
									"WHERE tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(incrementSeatsLeft);
			ps.setInt(1, tripID);
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

	public boolean decrementSeatsLeft(int tripID) {
		String decrementSeatsLeft = "UPDATE iterary SET seatsLeft = seatsLeft - 1 " +
									"WHERE tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(decrementSeatsLeft);
			ps.setInt(1, tripID);
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
	// LOCATION API
	//=======================
	public boolean insertLocation(Location location) {
		String insertLocation = "INSERT INTO location (operator, longitude, latitude) VALUES (?, ?, ?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertLocation);
			ps.setString(1, location.getOperator());
			ps.setDouble(2, location.getLongitude());
			ps.setDouble(3, location.getLatitude());
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

	public ArrayList<Location> getLocationNear(double longitude, double latitude, double maximumDistance) {
		ArrayList<Location> locationList = new ArrayList<Location>();
		String getLocationNear = "SELECT * FROM location WHERE SQRT(" +
						   		 "POWER((2 * PI() * ? * (ABS(longitude - ?) / 360.0)), 2) + " +
						   		 "POWER((2 * PI() * ? * (ABS(latitude - ?) / 360.0)), 2)) <= ?;";
		PreparedStatement ps = null;
		double radiusOfEarth = 6371000;
		try {
			ps = c.prepareStatement(getLocationNear);
			ps.setDouble(1, radiusOfEarth);
			ps.setDouble(2, longitude);
			ps.setDouble(3, radiusOfEarth);
			ps.setDouble(4, latitude);
			ps.setDouble(5, maximumDistance);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				locationList.add(new Location(rs.getString("operator"), rs.getDouble("longitude"),
											  rs.getDouble("latitude")));
			}
			rs.close();
			return locationList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public Location getLocationByUsername(String operator) {
		String getLocationByUsername = "SELECT * FROM location WHERE operator = ?;";
		Location location = null;
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(getLocationByUsername);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				location = new Location(rs.getString("operator"), rs.getDouble("longitude"),
										rs.getDouble("latitude"));
			}
			rs.close();
			return location;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

	public boolean updateLocation(Location location) {
		String updateLocation = "UPDATE location SET longitude = ? AND latitude = ? WHERE operator = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(updateLocation);
			ps.setDouble(1, location.getLongitude());
			ps.setDouble(2, location.getLatitude());
			ps.setString(3, location.getOperator());
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
	// RESERVATION API
	//=======================
	public boolean insertReservation(Reservation reservation) {
		String insertReservation = "INSERT INTO reservation (operator, tripID) VALUES (?, ?);";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(insertReservation);
			ps.setString(1, reservation.getOperator());
			ps.setInt(2, reservation.getTripID());
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

	public boolean deleteReservation(Reservation reservation) {
		String deleteReservation = "DELETE FROM reservation WHERE operator = ? AND tripID = ?;";
		PreparedStatement ps = null;
		try {
			ps = c.prepareStatement(deleteReservation);
			ps.setString(1, reservation.getOperator());
			ps.setInt(2, reservation.getTripID());
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

	public ArrayList<Reservation> getReservationByUsername(String operator) {
		String getReservationByUsername = "SELECT * FROM reservation WHERE operator = ?;";
		PreparedStatement ps = null;
		ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
		try {
			ps = c.prepareStatement(getReservationByUsername);
			ps.setString(1, operator);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reservationList.add(new Reservation(rs.getString("operator"), rs.getInt("tripID")));
			}
			rs.close();
			return reservationList;
		} catch (Exception e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}
