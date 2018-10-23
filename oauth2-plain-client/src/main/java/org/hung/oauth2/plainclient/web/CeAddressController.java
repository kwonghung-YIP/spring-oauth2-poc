package org.hung.oauth2.plainclient.web;

import org.hung.oauth2.pojo.CeAddress;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CeAddressController {

	@CrossOrigin({"*"})
	@GetMapping("/{ceref}/address")
	//@PreAuthorize("permitAll")
	public CeAddress getAddress(@PathVariable String ceref) {
		CeAddress address = new CeAddress();
		address.setCeref(ceref);
		address.setLine1("from Server line#1");
		address.setLine2("from Server line#2");
		return address;
	}
}
