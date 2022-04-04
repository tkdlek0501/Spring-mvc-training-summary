package hello.itemservice.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

// 에러 발생시 에러페이지 보여줄 컨트롤러 

@Slf4j
@Controller
public class ErrorPageController {
	
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
}
