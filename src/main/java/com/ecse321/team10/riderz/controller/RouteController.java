package com.ecse321.team10.riderz.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecse321.team10.riderz.dto.ItineraryDto;
import com.ecse321.team10.riderz.model.Itinerary;
// Add 3 imports: itinerarydto, locationdto, reservationdto
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
	/*
	private LocationDto locationConvertToDto(Location location) {
		return modelMapper.map(location, LocationDto.class);
	}

	private ReservationDto reservationConvertToDto(Reservation reservation) {
		return modelMapper.map(reservation, ReservationDto.class);
	}
	 */
	private ItineraryDto intineraryConvertToDto(Itinerary itinerary) {
		return modelMapper.map(itinerary, ItineraryDto.class);
	}
	
	private static final Logger logger = LogManager.getLogger(RiderzController.class);
	
	//For Testing purpose:
	//localhost:8088/insertItinerary/36/15.33534/12.44412/2019-01-01 01:00:00.000/40.33245/34.33214/2019-01-01 01:30:00.000/3
	
	// Insert an itinerary
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
	
	//For Testing purpose:
	//localhost:8088/updateItinerary/36/25.1111/26.3114/2019-01-01 02:00:00.000/40.33245/34.33214/2019-01-01 02:30:05.000/2
	
	//Update an itinerary
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
	
	//For Testing purpose:
	//localhost:8088/deleteItinerary/36
	
	//Delete an itinerary
	@GetMapping("/deleteItinerary/{tripID}")
	public boolean deleteItinerary(@PathVariable("tripID") int tripID) {
		
		int deleteTripID = tripID;
		sql.connect();
		if (sql.deleteItinerary(deleteTripID)) {
			sql.closeConnection();
			return true;
		}
		sql.closeConnection();
		return false;
	
	
	}
	
	
	//--- Helper Method ---
	private Timestamp stringtoTimeStamp (String timeString) {
		try {
		    SimpleDateFormat dateLayout = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		    Date date = dateLayout.parse(timeString);
		    Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		    return timestamp;
		} catch(Exception e) {
			return null;
		}
	}			
}
