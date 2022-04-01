package hello.itemservice.web.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.SessionConst;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	private final SessionManager sessionManager;
	
	// 로그인 폼 화면
	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
		return "login/loginForm";
	}
	
	// 로그인
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
		if(bindingResult.hasErrors()) {
			return "login/loginForm";
		}
		
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		
		if(loginMember == null) {
			log.info("로그인 실패");
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		// 로그인 성공 처리
		log.info("로그인 한 회원 : {}", loginMember);
		//세션이 있으면 이미 있는 세션 반환, 없으면 새로 생성
		HttpSession session = request.getSession();
		//세션에 로그인 회원 정보 보관
		session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
		
		// 직접 만든 SessionManager 이용해서 세션 생성, 쿠키 발급, 회원 data 보관
		//sessionManager.createSession(loginMember, response);
		
		return "redirect:/";
	}
	
	//	@PostMapping("/login")
//	public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
//		if(bindingResult.hasErrors()) {
//			return "login/loginForm";
//		}
//		
//		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
//		
//		if(loginMember == null) {
//			log.info("로그인 실패");
//			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//			return "login/loginForm";
//		}
//		
//		// 로그인 성공 처리
//		log.info("로그인 한 회원 : {}", loginMember);
//		
//		//TODO: 쿠키 사용
//		// 쿠키에 시간 설정 안하면 브라우저 종료시까지 유지
//		Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId())); // Long to String
//		response.addCookie(idCookie);
//		
//		return "redirect:/";
//	}
	
	// 로그아웃
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		//sessionManager.expire(request);
		HttpSession session = request.getSession(false); // default는 true, false이면 기존 세션 없을 때 새로 생성 안함
		if(session != null) { // 기존 세션이 있다면
			session.invalidate(); // 무효화
		}
		
		return "redirect:/";
	}
	
	// cookie의 만료시간을 0으로 만들어버리기
//	@PostMapping("/logout")
//	public String logout(HttpServletResponse response) {
//		expireCookie(response, "memberId");
//		return "redirect:/";
//	}

	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
}
