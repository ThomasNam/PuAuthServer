package kr.purred.auth.controller.mapi;

import kr.purred.auth.PuAuthProperties;
import kr.purred.auth.controller.response.ApiResult;
import kr.purred.auth.exception.AuthException;
import kr.purred.auth.lib.StrLib;
import kr.purred.auth.models.PuMemberSv;
import kr.purred.auth.models.data.RegisterMemberData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mapi/member")
public class PuMemberController
{
	/**
	 * 회원 Sv
	 */
	private final PuMemberSv puMemberSv;

	/**
	 * 프로퍼티
	 */
	private final PuAuthProperties authProperties;

	@PostMapping("/")
	public ApiResult postMember (RegisterMemberData memberData, @RequestHeader("globalToken") String globalToken) throws AuthException
	{
		// TODO 추후 인터셉터로 이동 시키자
		if (StrLib.isEmptyStr (globalToken) || !globalToken.equals (authProperties.getGlobalToken ()))
			throw new AuthException ();

		puMemberSv.registerMember (memberData);

		return ApiResult.success ();
	}
}
