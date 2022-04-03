package hello.itemservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.argumentResolver.Login;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final MemberRepository memberRepository;
	private final SessionManager sessionManager;
	
//	@GetMapping("/")
//	public String home() {
//		return "home";
//	}
	
	// ArgumentResolver 를 통해 login한 유저 가지고 오기 (어노테이션 직접 만듦) -> 스프링 시큐리티의 Authentication?
	@GetMapping("/")
	public String homeLogin(@Login Member loginMember, Model model) {
		// 로그인
		// 안했으면
		if(loginMember == null) {
			return "home";
		}
		
		// 했으면
		model.addAttribute("member", loginMember);
		return "loginHome";
	}
	
	// spring 이용한 세션 처리 (@SessionAttribute)
	// 세션 생성은 안하고 세션을 찾아올 때 쓰는 어노테이션
//	@GetMapping("/")
//	public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
//		// 로그인
//		// 안했으면
//		if(loginMember == null) {
//			return "home";
//		}
//		
//		// 했으면
//		model.addAttribute("member", loginMember);
//		return "loginHome";
//	}
	// 특정 세션 단위로 시간 설정
	// session.setMaxInactiveInterval(1800);
	// 최근 세션 접근 시간
	// session.getLastAccessedTime();
	
	
//	@GetMapping("/")
//	public String homeLogin(HttpServletRequest request, Model model) {
//		
//		HttpSession session = request.getSession(false);
//		if(session == null) {
//			return "home";
//		}
//		
//		//Member member = (Member)sessionManager.getSession(request);
//		Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//		
//		// 로그인
//		// 안했으면
//		if(loginMember == null) {
//			return "home";
//		}
//		
////		if(member == null) {
////			return "home";
////		}
//		
//		// 했으면
//		model.addAttribute("member", loginMember);
//		return "loginHome";
//	}
	
	//TODO: 쿠키 사용법
	// 그러나 중요한 data로 쿠키를 생성하면 안되고 추정 불가능한 값으로 넘겨야한다  
	// cookie 는 HttpServletRequest를 통해 꺼낼 수도 있지만, 스프링에서 제공하는 @CookieValue 어노테이션을 이용해 꺼낼 수 있다.
//	@GetMapping("/")
//	public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
//		
//		if(memberId == null) {
//			return "home";
//		}
//		
//		// 로그인
//		Member loginMember = memberRepository.findById(memberId);
//		// 하지 않았으면
//		if(loginMember == null) {
//			return "home";
//		}
//		
//		// 했으면
//		model.addAttribute("member", loginMember);
//		return "loginHome";
//	}
	
}
