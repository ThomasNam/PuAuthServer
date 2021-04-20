package kr.purred.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class PuAuthServerApplication extends SpringBootServletInitializer
{
	public static void main (String[] args)
	{
		new SpringApplicationBuilder (PuAuthServerApplication.class)
				.run (args);
	}

	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder application)
	{
		return application.sources (PuAuthServerApplication.class);
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter ()
	{
		return new StringHttpMessageConverter (StandardCharsets.UTF_8);
	}

	@Bean
	public Filter characterEncodingFilter ()
	{
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter ();
		characterEncodingFilter.setEncoding ("UTF-8");
		characterEncodingFilter.setForceEncoding (true);
		return characterEncodingFilter;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean()
	{
		FilterRegistrationBean registrationBean = new FilterRegistrationBean ();

		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter ();
		registrationBean.setFilter (characterEncodingFilter);
		characterEncodingFilter.setEncoding ("UTF-8");
		characterEncodingFilter.setForceEncoding (true);
		registrationBean.setOrder (Integer.MIN_VALUE);
		registrationBean.addUrlPatterns ("/*");
		return registrationBean;
	}

	/**
	 * CORS 필터 추가
	 * @return CORS 필터 추가
	 */
	@Bean
	public CorsFilter corsFilter()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource ();
		CorsConfiguration config = new CorsConfiguration ();
		config.setAllowCredentials (true);
		config.addAllowedOrigin ("*");
		config.addAllowedHeader ("*");
		config.addAllowedMethod ("OPTIONS");
		config.addAllowedMethod ("GET");
		config.addAllowedMethod ("POST");
		config.addAllowedMethod ("PUT");
		config.addAllowedMethod ("DELETE");
		source.registerCorsConfiguration ("/**", config);
		return new CorsFilter (source);
	}
}
