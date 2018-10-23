package org.hung.oauth2.plainclient.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallerController {

	@Autowired
	private OAuth2RestTemplate greetingService;
	
	@GetMapping("/caller")
	public String caller() {
		String message = greetingService.getForObject("http://localhost:9090/greeting", String.class);
		return message;
	}
}
