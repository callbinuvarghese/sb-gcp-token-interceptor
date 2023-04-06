package com.binu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@GetMapping("/login")
	public String login() {
		logger.info("====Employee Login Controller=====");
		return "Employee Login";
	}

	@PostMapping("/postbody")
	public String postBody(@RequestBody String fullName) {
		return "Hello " + fullName;
	}
}
