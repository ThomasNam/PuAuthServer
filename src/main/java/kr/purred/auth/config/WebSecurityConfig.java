package kr.purred.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	@Qualifier (value = "customUserDetailsService")
	private UserDetailsService customUserDetailsService;

	private static PasswordEncoder encoder;

	@Qualifier ("dataSource")
	@Autowired DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder ()
	{
		if(encoder == null)
			encoder = new BCryptPasswordEncoder ();

		return encoder;
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

	@Override
	protected AuthenticationManager authenticationManager()
	{
		return userAuthenticationManagerBean ();
	}

	@Override
	public void configure (WebSecurity web) throws Exception
	{
		super.configure (web);

		web.ignoring ().antMatchers (HttpMethod.OPTIONS);
	}

	@Override
	protected void configure (HttpSecurity http) throws Exception
	{
		http
			.csrf ().disable ()
			.authorizeRequests ()
				.antMatchers ("/", "/mapi/**").permitAll ()
				.anyRequest ().authenticated ()
			.and ()
				.headers ().frameOptions ().disable ()
			.and ()
				.httpBasic ()
		;
	}
}
