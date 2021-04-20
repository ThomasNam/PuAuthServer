package kr.purred.auth;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by RED on 2015-06-09.
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler
{
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj)
	{
		throwable.printStackTrace ();

		System.out.println("Exception message - " + throwable.getMessage());

		System.out.println("Method name - " + method.getName());

		for (Object param : obj)
			System.out.println("Parameter value - " + param);
	}
}