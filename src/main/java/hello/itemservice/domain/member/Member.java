package hello.itemservice.domain.member;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {
	
	private Long id;
	
	@NotBlank
	private String loginId; // 로그인 ID
	@NotBlank
	private String name; // 사용자 이름
	@NotBlank
	private String password; // 비번
	
}
