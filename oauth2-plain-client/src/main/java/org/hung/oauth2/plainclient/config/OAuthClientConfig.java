package org.hung.oauth2.plainclient.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//@Profile("OAuth2")
@Configuration
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class OAuthClientConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2ProtectedResourceDetails defaultResource;
	
	@Autowired
	private OAuth2ClientContext context;
	
	@Bean
	public OAuth2RestTemplate template() {
		OAuth2RestTemplate template = new OAuth2RestTemplate(defaultResource,context);
		return template;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		  .authorizeRequests()
		    .anyRequest()
		      .authenticated()
		  .and()
		    .cors();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		//source.registerCorsConfiguration("/login", configuration);
		source.registerCorsConfiguration("/**", configuration);
		return source;		
	}
}
