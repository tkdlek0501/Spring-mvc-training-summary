package hello.itemservice.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.PatternMatchUtils;

import hello.itemservice.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

// 로그인이 필요한 페이지는 로그인 정보가 없으면 이동(요청)을 막아야 된다.
// 스프링 시큐리티를 이용해도 이러한 과정(필터)들이 있기 때문에 스프링 시큐리티 다시 공부하면서 이 부분을 참고하면 좋을 것 같음

@Slf4j
public class LoginCheckFilter implements Filter{
	
	// 필터로 거르지 않을 URL 설정
	private static final String[] whiteList = {"/",  "/members/add", "/login", "/logout", "/css/*"};
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		try {
			log.info("인층 체크 필터 시작{}", requestURI);
			
			if(isLoginCheckPath(requestURI)) { // 로그인이 필요한 URI 인지
				log.info("인증 체크 로직 실행 {}", requestURI);
				HttpSession session = httpRequest.getSession(false);
				if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
					log.info("미인증 사용자 요청 {}", requestURI);
					// 로그인으로 redirect
					httpResponse.sendRedirect("/login?redirectURL=" + requestURI); // requestURI를 넘겨서 로그인을 하면 원래 보던 페이지로 이동되게 
					return; // 더 이상 진행 X
				}
			}
			
			chain.doFilter(request, response);
			
		} catch (Exception e) {
			throw e; // 예외 로깅 가능 but, 톰캣까지 예외를 보내줘야 함
		} finally {
			log.info("인증 체크 필터 종료 {}", requestURI);
		}
	}
	
	// 화이트 리스트의 경우 인증 체크 X
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
	}

}


// init과 destroy 메서드는 인터페이스 내에서 default 키워드가 붙어있어 구현체가 없어도 된다.
// 여기의 filter는 WebConfig 에 bean 등록을 해줘야 실행된다!


