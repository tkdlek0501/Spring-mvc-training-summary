package hello.itemservice.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

// 에러 발생시 에러페이지 보여줄 컨트롤러 

@Slf4j
@Controller
public class ErrorPageController {
	
	// RequestDispatcher 상수
	public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
	
	@RequestMapping("/error-page/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 404");
		return "errorPage/404";
	}
	
	@RequestMapping("/error-page/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 500");
		return "errorPage/500";
	}
	
	// api 응답시 produces 설정으로 mediaType 지정 가능; 해당 미디어 타입이면 이 컨트롤러가 호출됨
	// 요청시에 accept 설정에 application/json 으로 하면 이 컨트롤러가 우선이 됨
	@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,Object>> errorPage500Api(
			HttpServletRequest request, HttpServletResponse response
			){
		log.info("API errorPage 500");
		
		Map<String, Object> result = new HashMap<>();
		Exception ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		result.put("status", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		result.put("message", ex.getMessage());
		
		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
	}
	
	
	
	
}
