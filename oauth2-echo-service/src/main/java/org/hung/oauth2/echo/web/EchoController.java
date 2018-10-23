package org.hung.oauth2.echo.web;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

	@GetMapping("/echo")
	public String echo(@RequestParam String msg) {
		return "I got your message [" + msg + "]";
	}
	
	@GetMapping("/greeting")
	public Principal greeting(@AuthenticationPrincipal Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return principal;
	}	

}
