package hello.itemservice.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

// Exception 이 생겼을 때 서블릿에서 처리해주는 모습 확인을 위한 컨트롤러 (여기서 일부러 예외 발생시킴)
// application.properties 에서 스프링부트 기본 예외 페이지 꺼둠 -> 직접 설정함

@Slf4j
@Controller
public class ServletExController {
	
	@GetMapping("/error-ex")
	public void errorEx() {
		throw new RuntimeException("예외 발생!"); // 500 으로 처리
	}
	
	// response의 sendError 이용 
	@GetMapping("/error-404")
	public void error404(HttpServletResponse response) throws IOException {
		response.sendError(404, "404 오류!");
	}
	
	@GetMapping("/error-500")
	public void error5004(HttpServletResponse response) throws IOException {
		response.sendError(500);
	}
	
	@GetMapping("/error-400")
	public void error400(HttpServletResponse response) throws IOException {
		response.sendError(400, "404 오류!");
	}
}
