package hung.oauth2.provider.web;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserInfoController {

    @RequestMapping("/oauth/userinfo")    
    public Object user(Principal principal) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	log.info(""+auth);
    	log.info(""+auth.getPrincipal());
    	
   		return auth.getPrincipal();
    }
    
}
