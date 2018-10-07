package com.ecse321.team10.riderz.controller;

import com.ecse321.team10.riderz.dto.TripDto;
import com.ecse321.team10.riderz.model.Trip;
import com.ecse321.team10.riderz.sql.MySQLJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("trip")

public class TripController {

    @Autowired
    private MySQLJDBC sql;

    @Autowired
    private ModelMapper modelMapper;

    private TripDto convertToDto(Trip trip) { return modelMapper.map(trip, TripDto.class); }

    private static final Logger logger = LogManager.getLogger(RiderzController.class);

    /**
     * Fetches all the trips associated with the User
     * @param operator 		 -		A String representing the User's operator name
     * @return A list of TripDto object(s) if the user exist and has at least one Trip.
     * Otherwise Null
     */
    @GetMapping("/username")
    public List<TripDto> getTripByUsername(@RequestParam String operator) {
        sql.connect();
        if (operator != null){
            List<TripDto> trips = new ArrayList<>();
            for(Trip trip : sql.getTripsByUsername(operator))
                trips.add(convertToDto(trip));
            sql.closeConnection();
            logger.info("Successfully got a list of all the trips associated with " + operator);
            logger.info(trips);
            return trips;
        }
        sql.closeConnection();
        logger.info("There was no operator value");
        return null;
    }

    /**
     * Fetches the last trip of a user via their operator name
     * @param operator		 -		A String representing the User's operator name
     * @return A TripDto object if the user exists and has at least one trip.
     * Otherwise returns Null
     */
    @GetMapping("/last")
    public TripDto getLastTripsByUsername(@RequestParam String operator) {
        sql.connect();
        if (operator != null){
            Trip trip = sql.getLastTripByUsername(operator);
            sql.closeConnection();
            logger.info("Successfully retrieved the list of trips: " + trip.toString());
            return convertToDto(trip);
        }
        sql.closeConnection();
        logger.info("There was no operator value");
        return null;
    }

    /**
     * Fetches all the Trips in the database
     * @return A list of TripDto object(s) if there are Trips in the database.
     * Otherwise Null
     */
    @GetMapping("/all")
    public List<TripDto> getAllTrips() {
        sql.connect();
        List<TripDto> trips = new ArrayList<>();
        for(Trip trip : sql.getAllTrips())
            trips.add(convertToDto(trip));
        sql.closeConnection();
        logger.info("Successfully got a list of all the trips");
        logger.info(trips);
        return trips;
    }

    @RequestMapping(value = "insertTrip", method = RequestMethod.PUT)
    public boolean insertTrip( @RequestParam String operator) {
        if (sql.connect()) {
            boolean r = sql.insertTrip(operator);
            sql.closeConnection();
            return r;
        }
        logger.info("Failed to establish communication with SQL database");
        return false;
    }

    @RequestMapping(value = "deleteTrip", method = RequestMethod.PUT)
    public boolean deleteTrip( @RequestParam int tripID,
                               @RequestParam String operator) {
        if (sql.connect()) {
            boolean r = sql.deleteTrip(tripID, operator);
            sql.closeConnection();
            return r;
        }
        logger.info("Failed to establish communication with SQL database");
        return false;
    }

    @RequestMapping(value = "deleteAllTrips", method = RequestMethod.PUT)
    public boolean deleteAllTrips( @RequestParam String operator) {
        if (sql.connect()) {
            boolean r = sql.deleteAllTrips(operator);
            sql.closeConnection();
            return r;
        }
        logger.info("Failed to establish communication with SQL database");
        return false;
    }

}