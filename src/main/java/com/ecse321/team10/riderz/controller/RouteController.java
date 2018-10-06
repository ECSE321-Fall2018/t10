package com.ecse321.team10.riderz.controller;


import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecse321.team10.riderz.dto.ItineraryDto;
import com.ecse321.team10.riderz.dto.LocationDto;
import com.ecse321.team10.riderz.dto.ReservationDto;
import com.ecse321.team10.riderz.model.Itinerary;
import com.ecse321.team10.riderz.model.Location;
import com.ecse321.team10.riderz.model.Reservation;
import com.ecse321.team10.riderz.sql.MySQLJDBC;

/**
 * Route Controller handling URLS and routing to the specified information within the DB
 * Handles all relevant information concerning itineraries
 */

@RestController
public class RouteController {
	
	@Autowired
	private MySQLJDBC sql;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private LocationDto locationConvertToDto(Location location) {
		return modelMapper.map(location, LocationDto.class);
	}
	
	private ReservationDto reservationConvertToDto(Reservation reservation) {
		return modelMapper.map(reservation, ReservationDto.class);
	}
	
	private ItineraryDto intineraryConvertToDto(Itinerary itinerary) {
		return modelMapper.map(itinerary, ItineraryDto.class);
	}
	
	/**
	 * Insert an itinerary
	 * 
	 * @param tripID             - An integer uniquely identifying a trip
	 * @param startingLongitude  - A double representing the starting longitude
	 * @param startingLatitude   - A double representing the starting latitude
	 * @param startingTime       - A String representing the starting time
	 * @param endingLongitude    - A double representing the ending longitude
	 * @param endingLatitude     - A double representing the ending latitude
	 * @param endingTime         - A String representing the ending time
	 * @param seatsLeft          - An integer representing the number of seats left
	 * @return itinerary         - The created itinerary
	 */
	@GetMapping("/insertItinerary/{tripID}/{startingLongitude}/{startingLatitude}/{startingTime}/{endingLongitude}/{endingLatitude}/{endingTime}/{seatsLeft}")
	public ItineraryDto insertItinerary(@PathVariable("tripID") int tripID,
										@PathVariable("startingLongitude") double startingLongitude,
										@PathVariable("startingLatitude") double startingLatitude,
										@PathVariable("startingTime") String startingTime,
										@PathVariable("endingLongitude") double endingLongitude,
										@PathVariable("endingLatitude") double endingLatitude,
										@PathVariable("endingTime") String endingTime,
										@PathVariable("seatsLeft") int seatsLeft) {
		
		Timestamp startingTimeStamp = stringtoTimeStamp(startingTime);
		Timestamp endingTimeStamp = stringtoTimeStamp(endingTime);
		
		Itinerary itinerary = new Itinerary(tripID, startingLongitude, startingLatitude, startingTimeStamp, endingLongitude, endingLatitude, endingTimeStamp, seatsLeft);
		sql.connect();
		if (sql.insertItinerary(itinerary)) {
			sql.closeConnection();
			return intineraryConvertToDto(itinerary);
		}
		sql.closeConnection();
		return null;
	}
	
	
	/**
	 * Update an itinerary
	 * 
	 * @param tripID             - An integer uniquely identifying a trip
	 * @param startingLongitude  - A double representing the starting longitude
	 * @param startingLatitude   - A double representing the starting latitude
	 * @param startingTime       - A String representing the starting time
	 * @param endingLongitude    - A double representing the ending longitude
	 * @param endingLatitude     - A double representing the ending latitude
	 * @param endingTime         - A String representing the ending time
	 * @param seatsLeft          - An integer representing the number of seats left
	 * @return updatedItinerary  - the updated itinerary
	 */
	@GetMapping("/updateItinerary/{tripID}/{startingLongitude}/{startingLatitude}/{startingTime}/{endingLongitude}/{endingLatitude}/{endingTime}/{seatsLeft}")
	public ItineraryDto updateItinerary(@PathVariable("tripID") int tripID,
										@PathVariable("startingLongitude") double startingLongitude,
										@PathVariable("startingLatitude") double startingLatitude,
										@PathVariable("startingTime") String startingTime,
										@PathVariable("endingLongitude") double endingLongitude,
										@PathVariable("endingLatitude") double endingLatitude,
										@PathVariable("endingTime") String endingTime,
										@PathVariable("seatsLeft") int seatsLeft) {
		
		Timestamp startingTimeStamp = stringtoTimeStamp(startingTime);
		Timestamp endingTimeStamp = stringtoTimeStamp(endingTime);
		
		Itinerary updatedItinerary = new Itinerary(tripID, startingLongitude, startingLatitude, startingTimeStamp, endingLongitude, endingLatitude, endingTimeStamp, seatsLeft);
		sql.connect();
		if (sql.updateItinerary(updatedItinerary)) {
			sql.closeConnection();
			return intineraryConvertToDto(updatedItinerary);
		}
		sql.closeConnection();
		return null;
		
	}
	
	/**
	 * Delete an itinerary
	 * 
	 * @param tripID     - An integer uniquely identifying a trip
	 * @return message   - A string to indicate if the itinerary was deleted successfully or not.
	 */
	@GetMapping("/deleteItinerary/{tripID}")
	public String deleteItinerary(@PathVariable("tripID") int tripID) {
		
		int deleteTripID = tripID;
		sql.connect();
		if (sql.deleteItinerary(deleteTripID)) {
			sql.closeConnection();
			return String.format("Itinerary %s was deleted.", deleteTripID);
		}
		sql.closeConnection();
		return "Itinerary does not exist.";	
	}
	
	/**
	 * Obtain the Itinerary information base on the tripID
	 * 
	 * @param tripID     - An integer uniquely identifying a trip
	 * @return itinerary - The Itinerary object if found, null otherwise.
	 */
	@GetMapping("/getItineraryByTripID/{tripID}")
	public ItineraryDto getItineraryByTripID(@PathVariable("tripID") int tripID) {
		Itinerary itinerary = null;
		
		sql.connect();
		itinerary = sql.getItineraryByTripID(tripID);
		if (itinerary != null) {
			sql.closeConnection();
			return intineraryConvertToDto(itinerary);
		}
		sql.closeConnection();
		return null;
	}
	
	/**
	 * Obtains all the Itineraries fitting search criteria based on a spherical distance
	 * algorithm. Low search radius is recommended for accurate results.
	 * 
	 * @param endingLongitude	-	A double representing destination longitude.
	 * @param endingLatitude	-	A double representing destination latitude.
	 * @param maximumDistance	-	A double representing maximum search radius in meters. 
	 * @param arrivalTime       -   A String representing preferred arrival time
	 * @return itineraryList    -   A List of Itinerary representing the itineraries found base on the criteria. Return Null if none was found.
	 */
	@GetMapping("/getItineraryNearDestination/{endingLongitude}/{endingLatitude}/{maximumDistance}/{arrivalTime}")
	public List<ItineraryDto> getItineraryNearDestination(	@PathVariable("endingLongitude") double endingLongitude,
															@PathVariable("endingLatitude") double endingLatitude,
															@PathVariable("maximumDistance") double maximumDistance,
															@PathVariable("arrivalTime") String arrivalTime){
		
		Timestamp arrivalTimeStamp = stringtoTimeStamp(arrivalTime);
		
		sql.connect();		
		List<ItineraryDto> itineraryList = new ArrayList<ItineraryDto>();
		for(Itinerary itinerary : sql.getItineraryNearDestination(endingLongitude, endingLatitude, maximumDistance, arrivalTimeStamp))
			itineraryList.add(intineraryConvertToDto(itinerary));
		sql.closeConnection();
		return itineraryList;
	}
	
	/**
	 * Increment by 1 the number of seats left in the itinerary given by the tripID
	 * 
	 * @param tripID   - An integer uniquely identifying a trip
	 * @return message - A string indicating if the number of seats was incremented successfully for the tripID
	 */
	@GetMapping("/incrementSeatsLeft/{tripID}")
	public String incrementSeatsLeft(@PathVariable("tripID")int tripID) {
		
		sql.connect();
		if (sql.incrementSeatsLeft(tripID)) {
			sql.closeConnection();
			return String.format("The number of seats left in the itinerary %s was incremented.", tripID);
		}
		sql.closeConnection();
		return String.format("Itinerary %s does not exist.", tripID);
	}
	
	/**
	 * Decrements by 1 the number of seats left in the itinerary given by the tripID
	 * 
	 * @param tripID - An integer uniquely identifying a trip
	 * @return message - A string indicating if the number of seats was incremented successfully for the tripID
	 */
	@GetMapping("/decrementSeatsLeft/{tripID}")
	public String decrementSeatsLeft(@PathVariable("tripID")int tripID) {
		
		sql.connect();
		if (sql.decrementSeatsLeft(tripID)) {
			sql.closeConnection();
			return String.format("The number of seats left in the itinerary %s was decremented.", tripID);
		}
		sql.closeConnection();
		return String.format("Itinerary %s does not exist.", tripID);
	}
	
	/**
	 * Insert a Location object into the database. Note the operator must already exist.
	 * 
	 * Location object: operator, longitude, latitude
	 * @param operator  - A String representing the name of the driver
	 * @param longitude - A double representing the longitude
	 * @param latitude  - A double representing the latitude
	 * @return message  - A String representing if the location was successfully inserted or not.
	 */
	@GetMapping("/insertLocation/{operator}/{longitude}/{latitude}")
	public String insertLocation(@PathVariable("operator") String operator,
								 @PathVariable("longitude") double longitude,
								 @PathVariable("latitude") double latitude) {
		
		Location location = new Location(operator, longitude, latitude);
		sql.connect();
		if (sql.insertLocation(location)) {
			sql.closeConnection();
			return String.format("Location of %s has been inserted into the database.", location.getOperator());
		}
		sql.closeConnection();
		return String.format("Location of %s could not be inserted into the database.", location.getOperator());
	}
	
	/**
	 * Fetches entries from the database fitting search criteria based on a spherical distance
	 * algorithm. Recommended to have a low maximum search radius to obtain more accurate results.
	 * 
	 * @param endingLongitude	-	A double representing current User's longitude.
	 * @param endingLatitude	-	A double representing current User's latitude.
	 * @param maximumDistance	-	A double representing maximum search radius in meters.
	 * @return locationList     -   A List of Location objects matching the search criteria. Null if an error occurred.
	 */
	@GetMapping("/getLocationNear/{longitude}/{latitude}/{maximumDistance}")
	public List<LocationDto> getLocationNear(@PathVariable("longitude") double longitude,
											 @PathVariable("latitude") double latittude,
											 @PathVariable("maximumDistance") double maximumDistance){
		sql.connect();		
		List<LocationDto> locationList = new ArrayList<LocationDto>();
		for(Location location : sql.getLocationNear(longitude, latittude, maximumDistance))
			locationList.add(locationConvertToDto(location));
		sql.closeConnection();
		return locationList;
	}
		
	/**
	 * Fetches the User's location from the database.
	 * @param operator	-	A String representing an User's userName.
	 * @return A Location object if an entry was found. Null otherwise.
	 */
	@GetMapping("/getLocationByUsername/{operator}")
	public Location getLocationByUsername(@PathVariable("operator") String operator) {
		
		sql.connect();
		Location location = sql.getLocationByUsername(operator);
		if (location != null) {
			sql.closeConnection();
			return location;
		}
		sql.closeConnection();
		return null;
	}
	
	/**
	 * Updates a location entry within the database.
	 * @param operator	-	A String representing the name of the operator.
	 * @param longitude -   A String representing the longitude.
	 * @param latitude  -   A String representing the latitude.
	 * @return message  -   A String representing if the location was successfully updated or not.
	 */
	@GetMapping("/updateLocation/{operator}/{longitude}/{latitude}")
	public String updateLocation(@PathVariable("operator") String operator,
								 @PathVariable("longitude") double longitude,
								 @PathVariable("latitude") double latitude) {
		
		Location location = new Location(operator, longitude, latitude);
		sql.connect();
		if (sql.updateLocation(location)) {
			sql.closeConnection();
			return String.format("Location of %s has been updated.", operator);
		}
		sql.closeConnection();
		return String.format("Location of %s does not exist", operator);
	}
	
	/**
	 * Deletes the location of an user from the database.
	 * 
	 * @param operator  -	A String representing the name of the operator.
	 * @return message  -   A String representing if the location was successfully deleted or not.
	 */
	@GetMapping("/deleteLocation/{operator}")
	public String deleteLocation(@PathVariable("operator") String operator) {
		
		sql.connect();
		if (sql.deleteLocation(operator)) {
			sql.closeConnection();
			return String.format("Location of %s has been deleted.", operator);
		}
		sql.closeConnection();
		return String.format("Location of %s does not exist", operator);
	}
	
	/**
	 * Inserts a Reservation object into the database.The operator and the tripId must exist
	 * @param opeartor	-	A String representing the name of the operator.
	 * @param tripID    -   An integer uniquely identifying a trip.
	 * @return message  -   A String representing if the Reservation was successfully inserted or not.
	 */
	@GetMapping("/insertReservation/{operator}/{tripID}")
	public String insertReservation(@PathVariable("operator") String operator,
									@PathVariable("tripID") int tripID) {
		sql.connect();
		Reservation reservation = new Reservation(operator, tripID);
		if (sql.insertReservation(reservation)) {
			sql.closeConnection();
			return "Reservation of " + operator + " has been inserted for the tripID: " + tripID;
		}
		sql.closeConnection();
		return "Reservation of " + operator + " is invalid for the tripID: " + tripID;
	}
	
	/**
	 * Deletes an entry from the database.
	 * 
	 * @param operator  - A String representing the name of the operator.
	 * @param tripID    - An integer uniquely identifying a trip.
	 * @return message  - A String representing if the Reservation was successfully deleted or not.
	 */
	@GetMapping("/deleteReservation/{operator}/{tripID}")
	public String deleteReservation(@PathVariable("operator") String operator,
									@PathVariable("tripID") int tripID) {
		sql.connect();
		Reservation reservation = new Reservation(operator, tripID);
		if (sql.deleteReservation(reservation)) {
			sql.closeConnection();
			return "Reservation of " + operator + " has been deleted for the tripID: " + tripID;
		}
		sql.closeConnection();
		return "Reservation of " + operator + " is invalid for the tripID: " + tripID;
	}
	
	/**
	 * Fetches from the database all reservations for a specific User.
	 * 
	 * @param operator         - A String representing the name of the operator.
	 * @return reservationList - A List of reservation for a specific user
	 */
	@GetMapping("/getReservationByUsername/{operator}")
	public List<ReservationDto> getReservationByUsername(@PathVariable("operator") String operator) {
		
		sql.connect();		
		List<ReservationDto> reservationList = new ArrayList<ReservationDto>();
		for(Reservation reservation : sql.getReservationByUsername(operator))
			reservationList.add(reservationConvertToDto(reservation));
		sql.closeConnection();
		return reservationList;
	}

	/**
	 * Helper Method: convert a string to a timeStamp
	 * 
	 * @param timeString - A time represented in a string
	 * @return timeStamp - A time represented by a timeStamp
	 */
	private Timestamp stringtoTimeStamp (String timeString) {
		try {
		    SimpleDateFormat dateLayout = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		    Date date = dateLayout.parse(timeString);
		    Timestamp timeStamp = new java.sql.Timestamp(date.getTime());
		    return timeStamp;
		} catch(Exception e) {
			return null;
		}
	}			
}
