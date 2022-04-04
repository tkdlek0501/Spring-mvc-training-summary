package hello.itemservice;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 직접 에러 페이지를 커스텀 하는 클래스
// 해당 예외가 발생하면 redirect하는 로직

// 예외 발생과 오류 페이지 요청 흐름  (서버 내부에서 작동)
// 1. 컨트롤러에서 예외 발생 -> 인터셉터 -> 서블릿 -> 필터 -> WAS
// 2. WAS에서 다시 예외 페이지 uri 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> view

// 이러면 같은 필터와 인터셉터가 2번 호출 되는 것 -> 클라이언트 요청이 아닌 서버 내부에서 호출이라면 필터와 인터셉터는 필요없다.
// -> 이를 DispatcherType 을 이용해 해결 가능; 필터는 : LoginCheckFilter/ 인터셉터는 : webConfig 에서 확인

// 여기서 이렇게 errorpage를 등록하는 게 번거롭다 -> 스프링부트에서 지원해준다 (templates/error 경로에 html 파일 두면 자동으로 인식)

// @Component -> 스프링에서 지원하는 오류페이지 사용을 위해 주석 처리
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		
		ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
		ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
				
		ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
		factory.addErrorPages(errorPage404, errorPage500, errorPageEx);		
	}

}
