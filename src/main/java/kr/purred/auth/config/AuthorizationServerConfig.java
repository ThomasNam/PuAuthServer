package kr.purred.auth.config;

import kr.purred.auth.PuAuthProperties;
import kr.purred.auth.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@EnableAuthorizationServer
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	ClientDetailsService clientDetailsService;

	private final PuAuthProperties authProperties;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	{
		endpoints.accessTokenConverter (jwtAccessTokenConverter ())
				.authenticationManager (userAuthenticationManagerBean ());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.withClientDetails (clientDetailsService);
	}

	@Bean
	@Primary
	public JdbcClientDetailsService jdbcClientDetailsService (
			@Qualifier("dataSource") DataSource dataSource)
	{
		return new JdbcClientDetailsService (dataSource);
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter()
	{
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

		converter.setAccessTokenConverter (accessTokenConverter ());

		KeyPair keyPair = new KeyStoreKeyFactory (new ClassPathResource (authProperties.getJksFile ()), authProperties.getJksPw ().toCharArray ())
				.getKeyPair(authProperties.getJksAlias (), authProperties.getJksAliasPw ().toCharArray());

		converter.setKeyPair (keyPair);

		return converter;
	}

	@Bean
	public AccessTokenConverter accessTokenConverter ()
	{
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter ();

		tokenConverter.setUserTokenConverter (userAuthenticationConverter ());

		return tokenConverter;
	}

	@Bean
	public TokenStore tokenStore ()
	{
		return new JwtTokenStore (jwtAccessTokenConverter ());
	}

	@Bean
	public UserAuthenticationConverter userAuthenticationConverter()
	{
		DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter ();

		defaultUserAuthenticationConverter.setUserDetailsService (customUserDetailsService);

		return defaultUserAuthenticationConverter;
	}

	@Bean (name = "authenticationManager")
	public AuthenticationManager userAuthenticationManagerBean()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

		daoAuthenticationProvider.setUserDetailsService (customUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder (passwordEncoder ());

		List<AuthenticationProvider> authenticationProviderList = new ArrayList<> ();
		authenticationProviderList.add (daoAuthenticationProvider);

		return new ProviderManager (authenticationProviderList);
	}

	@Bean
	public PasswordEncoder passwordEncoder ()
	{
		return new BCryptPasswordEncoder ();
	}
}
