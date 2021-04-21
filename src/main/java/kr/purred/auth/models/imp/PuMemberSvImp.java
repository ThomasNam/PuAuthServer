package kr.purred.auth.models.imp;

import kr.purred.auth.lib.StrLib;
import kr.purred.auth.lib.Util;
import kr.purred.auth.models.PuMemberSv;
import kr.purred.auth.models.data.RegisterMemberData;
import kr.purred.auth.models.domains.PuMember;
import kr.purred.auth.models.imp.repository.PuMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PuMemberSvImp implements PuMemberSv
{
	private final PuMemberRepository repository;

	@Override
	public PuMember getMember (String memberID)
	{
		return repository.findOne (memberID);
	}

	@Override
	public boolean existMemberID (String memberID)
	{
		return repository.exists (memberID);
	}

	@Override
	@Transactional
	public String registerMember (RegisterMemberData registerMemberData)
	{
		if (registerMemberData == null)
			throw new IllegalArgumentException ();

		// TODO 추후 밸리데이터로 이동시킨다.
		if (StrLib.isEmptyStr (registerMemberData.getMemberID ()))
			throw new IllegalArgumentException ();

		if (StrLib.isEmptyStr (registerMemberData.getPasswd ()))
			throw new IllegalArgumentException ();

		PuMember member = new PuMember ();

		Util.myCopyProperties (registerMemberData, member);

		member.setPasswd (BCrypt.hashpw (registerMemberData.getPasswd (), BCrypt.gensalt ()));
		member.setActive (true);
		member.getAuthorities ().add ("ROLE_USER");

		if (StrLib.isExistStr (registerMemberData.getMemberType ()))
			Arrays.stream (registerMemberData.getMemberType ().split (",")).forEach (member.getAuthorities ()::add);

		member = repository.save (member);

		return member.getMemberID ();
	}

	@Override
	@Transactional
	public boolean changePasswd (String memberID, String passwd, String newPasswd)
	{
		PuMember member = repository.findOne (memberID);

		if (member == null)
			return false;

		if  (BCrypt.checkpw (passwd, member.getPasswd ()))
		{
			member.setPasswd (BCrypt.hashpw (newPasswd, BCrypt.gensalt ()));

			return true;
		}
		else
			return false;
	}

	@Override
	@Transactional
	public boolean changePasswdForAdmin (String memberID, String newPasswd)
	{
		PuMember member = repository.findOne (memberID);

		if (member == null)
			return false;

		member.setPasswd (BCrypt.hashpw (newPasswd, BCrypt.gensalt ()));

		return true;
	}

	@Override
	@Transactional
	public boolean checkPasswd (String memberID, String passwd)
	{
		PuMember member = repository.findOne (memberID);

		return member != null && BCrypt.checkpw (passwd, member.getPasswd ());
	}

	@Override
	@Transactional
	public void deleteMember (String memberID)
	{
		if (repository.exists (memberID))
			repository.delete (memberID);
	}
}
