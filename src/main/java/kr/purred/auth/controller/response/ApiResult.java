package kr.purred.auth.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ApiResult
{
	/**
	 * 성공 여부
	 */
	private final boolean success;

	/**
	 * 메시지
	 */
	private final String message;

	public static ApiResult success ()
	{
		return new ApiResult (true, "");
	}

	public static ApiResult fail (String message)
	{
		return new ApiResult (false, message);
	}
}
