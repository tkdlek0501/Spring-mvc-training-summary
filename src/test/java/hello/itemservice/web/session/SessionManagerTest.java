package hello.itemservice.web.session;

import javax.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.itemservice.domain.member.Member;

class SessionManagerTest {

	SessionManager sessionManager = new SessionManager();
	
	@Test
	void sessionTest(){
		
		// 세션 생성
		MockHttpServletResponse response = new MockHttpServletResponse(); // response와 request를 직접 받을 수 없으니 스프링에서 제공하는 것을 사용
		Member member = new Member();
		sessionManager.createSession(member, response);
		
		// 요청에 응답 쿠키 저장 
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(response.getCookies());
		
		// 세션 조회
		Object result = sessionManager.getSession(request);
		Assertions.assertThat(result).isEqualTo(member);
		
		// 세션 만료
		sessionManager.expire(request);
		Object expired = sessionManager.getSession(request);
		Assertions.assertThat(expired).isNull();
	}	
}
