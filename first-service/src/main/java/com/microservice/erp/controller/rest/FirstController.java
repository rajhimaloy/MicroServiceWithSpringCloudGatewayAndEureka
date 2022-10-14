package com.microservice.erp.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RestController
@RequestMapping("/v1")
public class FirstController {

	private static final String CONTROLLER_INSTANCE_ID = UUID.randomUUID().toString();

	@GetMapping("/message")
	public String test() {
		return String.format("%s : Hello JavaInUse Called in First Service", CONTROLLER_INSTANCE_ID);
	}

	@GetMapping("/delayed/message")
	public String testDelay() throws InterruptedException {
		Thread.sleep(5000);
		return String.format("%s : Hello JavaInUse Called First Service in 5Sec Delay", CONTROLLER_INSTANCE_ID);
	}

	@GetMapping("/rateLimit/message")
	public String testRateLimit() throws InterruptedException {
		return "Hello JavaInUse Called First Service to test RateLimit!";
	}

	@GetMapping("/error/{args}")
	public String errorMessage(@PathVariable("args") Integer args){
		return String.format("%s : Argument provided properly", CONTROLLER_INSTANCE_ID);
	}

	@GetMapping("/error/throw/{throw}")
	public String errorException(@PathVariable("throw") boolean shouldThrow){
		if (shouldThrow) throw new RuntimeException(String.format("%s : Thrown from /error/...", CONTROLLER_INSTANCE_ID));
		return String.format("%s : No message has been thrown", CONTROLLER_INSTANCE_ID);
	}

	@GetMapping("/errorFallback")
	public String errorFallback() {
		return String.format("%s : Unfortunately api not reachable at this moment!", CONTROLLER_INSTANCE_ID);
	}

}
