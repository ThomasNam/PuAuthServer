package kr.purred.auth.models.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PU_MEMBER")
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PuMember
{
	@Id
	@Column (name = "memberID")
	private String memberID;

	/**
	 * 패스워드
	 */
	@JsonIgnore
	@Column(name = "passwd")
	private String passwd;


	/**
	 * 회원 타입
	 */
	@Column(name="memberType")
	private String memberType;

	/**
	 * 활성화 여부
	 */
	@Column(name="active")
	private boolean active;

	/**
	 * 사용자 권한
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@CollectionTable(name = "PU_MEMBER_AUTHORITIES", joinColumns = @JoinColumn (name = "memberID"))
	@Column(name = "authority")
	private Set<String> authorities = new HashSet<> ();
}
