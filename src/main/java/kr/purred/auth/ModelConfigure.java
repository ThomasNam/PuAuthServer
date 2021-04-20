package kr.purred.auth;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@RequiredArgsConstructor
public class ModelConfigure implements AsyncConfigurer
{
	/**
	 * 데이터 소스
	 */
	private final DataSource dataSource;

	/**
	 * 트랜잭션 매니져
	 * @param entityManagerFactory
	 * @return
	 */
	@Bean (name = "transactionManager")
	public PlatformTransactionManager jpaTransactionManager (EntityManagerFactory entityManagerFactory)
	{
		JpaTransactionManager tm = new JpaTransactionManager ();
		tm.setEntityManagerFactory (entityManagerFactory);
		tm.setDataSource (dataSource);

		return tm;
	}

	@Bean
	public SessionFactory getSessionFactory(HibernateEntityManagerFactory hemf)
	{
		return hemf.getSessionFactory ();
	}

	/**
	 * @return 로컬 벨리데이터를 리턴한다.
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean ()
	{
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean ();

		validatorFactoryBean.setProviderClass (HibernateValidator.class);
		// validatorFactoryBean.setValidationMessageSource (messageSource);

		return validatorFactoryBean;
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor ()
	{
		MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor ();

		postProcessor.setValidator (localValidatorFactoryBean ());

		return postProcessor;
	}

	@Override
	public Executor getAsyncExecutor ()
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor ();

		executor.setCorePoolSize (7);
		executor.setMaxPoolSize (342);
		executor.setQueueCapacity (11);
		executor.setThreadNamePrefix ("MyExecutor-");
		executor.initialize ();

		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler ()
	{
		return new CustomAsyncExceptionHandler ();
	}

	@Bean
	public RestTemplate restTemplate ()
	{
		return new RestTemplate ();
	}
}
