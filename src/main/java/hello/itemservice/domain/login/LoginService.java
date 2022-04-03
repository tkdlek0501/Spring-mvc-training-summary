package hello.itemservice.domain.login;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final MemberRepository memberRepository;
	
	// @return null 이면 로그인 실패
	public Member login(String loginId, String password) {
//		Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//		Member member = findMemberOptional.get();
//		if(member.getPassword().equals(password)) {
//			return member;
//		}else {
//			return null;
//		}
		
//		log.info("로그인 입력값 확인 : {}", password);
		
		// TODO: optional 꼭 연습하기!
		// 위 코드와 동일
		Optional<Member> findMember =  memberRepository.findByLoginId(loginId);
		log.info("findMember : {}", findMember); 
		return	findMember.filter(m -> m.getPassword().equals(password))
				.orElse(null);
	}
}
