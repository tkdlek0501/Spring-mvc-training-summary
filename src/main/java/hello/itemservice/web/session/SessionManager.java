package hello.itemservice.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class SessionManager {
	
	public static final String SESSION_COOKIE_NAME = "mySessionId";
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
	
	// 세션 생성
	public void createSession(Object value, HttpServletResponse response) {
		
		// 임의의 세션 id 생성 및 세션에 저장
		String sessionId = UUID.randomUUID().toString();
		sessionStore.put(sessionId, value);
		
		// 쿠키 생성
		Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		response.addCookie(mySessionCookie);
	}
	
	// 세션 조회 
	public Object getSession(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie == null) {
			return null;
		}
		return sessionStore.get(sessionCookie.getValue());
	}
	
	// 세션 만료 
	public void expire(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie != null) {
			sessionStore.remove(sessionCookie.getValue());
		}
	}
	
	//TODO: stream 사용법
	// 쿠키 찾기
	public Cookie findCookie(HttpServletRequest request, String cookieName) {
		if(request.getCookies() == null) {
			return null;
		}
		return Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals(cookieName))
			.findAny()
			.orElse(null);
	}
	
	
}
