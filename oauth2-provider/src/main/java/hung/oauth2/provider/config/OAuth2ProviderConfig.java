package hung.oauth2.provider.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class OAuth2ProviderConfig {
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		
		@Autowired
		private TokenStore tokenStore;
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.tokenStore(this.tokenStore);
		}
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
			  .antMatcher("/oauth/userinfo")
			    .authorizeRequests()
			      .anyRequest()
			        .authenticated()
			  .and()
			    .cors();
			// @formatter:on
		}
		
	}
	
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
		
		@Autowired
		private DataSource dataSource;

		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private CorsConfigurationSource corsConfigurationSource;
		
		@Autowired
		private TokenStore tokenStore;
		
		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			CorsFilter corsFilter = new CorsFilter(this.corsConfigurationSource);
			
			// @formatter:off
			security
			  //Allow client_id & client_secret passed in form post data
			  .allowFormAuthenticationForClients();
			// @formatter:on
			
			//Add CORS to filter to allow cross-origin ajax preflight call to /oauth/token  
			security.addTokenEndpointAuthenticationFilter(corsFilter);
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			JdbcAuthorizationCodeServices jdbcAuthCodeSvc = new JdbcAuthorizationCodeServices(dataSource);
			JdbcApprovalStore jdbcApplStore = new JdbcApprovalStore(dataSource);
			//JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
			//RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
			
			// @formatter:off
			//Reference: (Grant Types) https://projects.spring.io/spring-security-oauth/docs/oauth2.html			  
			endpoints
			  //Inject the authenticationManager to switch on the password grant type
			  .authenticationManager(authenticationManager)
			  //if you inject a UserDetailsService or if one is configured globally anyway (e.g. in a GlobalAuthenticationManagerConfigurer) 
			  //then a refresh token grant will contain a check on the user details, to ensure that the account is still active
			  .userDetailsService(userDetailsService)
			  .authorizationCodeServices(jdbcAuthCodeSvc)
			  .approvalStore(jdbcApplStore)
			  .tokenStore(this.tokenStore);
			// @formatter:on
		}
		
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.jdbc(dataSource);
		}
	
	}

	@Configuration
	@Profile("redis-token")
	protected static class RedisTokenStoreConfiguration {

		@Bean		
		public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
			return new RedisTokenStore(redisConnectionFactory);
		}
	}
	
	@Configuration
	@Profile("jdbc-token")
	protected static class JdbcTokenStoreConfiguration {

		@Bean
		public TokenStore tokenStore(DataSource dataSource) {
			return new JdbcTokenStore(dataSource);
		}	
	}
	
}
