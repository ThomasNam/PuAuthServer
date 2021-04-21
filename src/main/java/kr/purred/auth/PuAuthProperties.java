package kr.purred.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PuAuthProperties
{
	/**
	 * 서비스 아이디
	 */
	private String serviceID;

	/**
	 * JKS 파일
	 */
	private String jksFile;

	/**
	 * 패스워드
	 */
	private String jksPw;

	/**
	 * 별칭
	 */
	private String jksAlias;

	/**
	 * Alias Pw
	 */
	private String jksAliasPw;

	/**
	 * 글로벌 토큰
	 */
	private String globalToken;
}
