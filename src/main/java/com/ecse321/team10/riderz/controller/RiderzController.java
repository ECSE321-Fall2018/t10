package com.ecse321.team10.riderz.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecse321.team10.riderz.sql.MySQLJDBC;
/**
 * Index controller of the application
 *
 */
@RestController
public class RiderzController {
	private static final Logger logger = LogManager.getLogger(RiderzController.class);
	
	@RequestMapping("/")
	public String mainPage() {
		logger.info("An info message");
		return "Index page, web frontend is to be added later.\n";
	}
	
}
