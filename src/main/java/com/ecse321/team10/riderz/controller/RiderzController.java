package com.ecse321.team10.riderz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiderzController {

	@RequestMapping("/")
	public String mainPage() {
		return "Index page, web frontend is to be added later.\n";
	}
}
