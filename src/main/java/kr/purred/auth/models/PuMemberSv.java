package kr.purred.auth.models;

import kr.purred.auth.models.data.RegisterMemberData;
import kr.purred.auth.models.domains.PuMember;

/**
 * 회원 Sv
 */
public interface PuMemberSv
{
	/**
	 * 회원 정보를 리턴한다.
	 * @param memberID 회원 아이디
	 * @return 회원 정보
	 */
	PuMember getMember (String memberID);

	/**
	 * 회원 아이디
	 * @param memberID 회원 아이디
	 * @return 존재 여부
	 */
	boolean existMemberID (String memberID);

	/**
	 * 회원 등록
	 * @param registerMemberData 회원 등록 정보
	 * @return 회원 아이디
	 */
	String registerMember (RegisterMemberData registerMemberData);

	/**
	 * 패스워드를 변경한다.
	 * @param memberID 회원 아이디
	 * @param passwd 패스워드
	 * @param newPasswd 새로운 패스워드
	 * @return 결과
	 */
	boolean changePasswd (String memberID, String passwd, String newPasswd);

	/**
	 * 패스워드를 변경한다.
	 * @param memberID 회원 아이디
	 * @param newPasswd 새로운 패스워드
	 * @return 결과
	 */
	boolean changePasswdForAdmin (String memberID, String newPasswd);

	/**
	 * 패스워드를 체크한다.
	 * @param memberID 회원 아이디
	 * @param passwd 패스워드
	 * @return 체크 결과
	 */
	boolean checkPasswd (String memberID, String passwd);

	/**
	 * 사용자 아이디를 삭제한다.
	 * @param memberID 회원 아이디
	 */
	void deleteMember (String memberID);
}
