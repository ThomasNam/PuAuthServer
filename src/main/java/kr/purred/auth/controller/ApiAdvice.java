package kr.purred.auth.controller;

import kr.purred.auth.controller.response.ApiResult;
import kr.purred.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(value = "kr.purred.auth.controller")
@RequiredArgsConstructor
public class ApiAdvice
{
	@ExceptionHandler(AuthException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResult errorAuthException ()
	{
		return ApiResult.fail ("NO AUTH");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResult errorAuthException (IllegalArgumentException e)
	{
		return ApiResult.fail (e.getMessage ());
	}
}
