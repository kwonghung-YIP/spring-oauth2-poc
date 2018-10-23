package hung.oauth2.provider.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@RestController
public class UserInfoController {

    @RequestMapping("/oauth/userinfo")    
    public Principal user(Principal principal) {
   		return principal;
    }
    
}
