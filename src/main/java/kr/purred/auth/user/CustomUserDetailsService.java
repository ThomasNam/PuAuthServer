package kr.purred.auth.user;

import kr.purred.auth.models.PuMemberSv;
import kr.purred.auth.models.domains.PuMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by RED on 2016-04-25.
 */
@Service ("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
	/**
	 * 사용자 Sv
	 */
	final private PuMemberSv memberSv;

	@Override
	public UserDetails loadUserByUsername(final String username)
		throws UsernameNotFoundException
	{
		PuMember member = memberSv.getMember (username);

		if (member == null)
			throw new UsernameNotFoundException (username);

		List<GrantedAuthority> authorities = buildUserAuthority (member);

		return new User (member.getMemberID (), member.getPasswd (), member.isActive (), true, true, true, authorities);
	}

	/**
	 * 권한을 생성한다.
	 * @return 권한 리스트
	 */
	private List<GrantedAuthority> buildUserAuthority(PuMember member)
	{
		return member.getAuthorities ().stream ().map (SimpleGrantedAuthority::new).collect (Collectors.toList());
	}
}
