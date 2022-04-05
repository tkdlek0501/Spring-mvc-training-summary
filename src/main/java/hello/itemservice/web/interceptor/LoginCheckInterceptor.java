package hello.itemservice.web.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import hello.itemservice.web.SessionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{
	
	// handler 호출 전
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		String requestURI = request.getRequestURI();
		
		log.info("인증 체크 인터셉터 실행 {}", requestURI);
		
		HttpSession session = request.getSession();
		
		if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
			log.info("미인증 사용자 요청");
			// 로그인으로 redirect
			response.sendRedirect("/login?redirectURL=" + requestURI);
			return false;
		}
		
		return true;
	}
	
	// handler 호출 후, 예외 발생 시 호출x
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//		log.info("postHandle [{}]", modelAndView);	
//	}
	
	// 완전히 실행 후, 예외 발생 시에도 호출
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//		String requestURI = request.getRequestURI();
//		String uuid = (String) request.getAttribute(LOG_ID);
//		log.info("RESPONSE [{}][{}][{}]", uuid, requestURI, handler);
//		if(ex != null) {
//			log.error("afterCompletion error", ex);
//		}
//				
//	}
	
}
