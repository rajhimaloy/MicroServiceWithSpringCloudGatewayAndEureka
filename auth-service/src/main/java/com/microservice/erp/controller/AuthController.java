package com.microservice.erp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RestController
@RequestMapping("/v1")
public class AuthController {

	@GetMapping("/validateToken")
	public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
		if (token == null || token.isEmpty() || token.length() <= 10)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access: Length is less than 11");
		return ResponseEntity.ok("Hello JavaInUse Called in Auth Service: get::validateToken");
	}

	@PostMapping("/validateToken")
	public ResponseEntity<String> validateTokenV2(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		if (token == null || token.isEmpty() || token.length() <= 10)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access: Length is less than 11");
		return ResponseEntity.ok("Hello JavaInUse Called in Auth Service: post::validateToken");
	}
}
