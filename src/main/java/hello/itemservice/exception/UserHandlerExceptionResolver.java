//package hello.itemservice.exception;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class UserHandlerExceptionResolver implements HandlerExceptionResolver{
//	
//	private final ObjectMapper objectMapper = new ObjectMapper(); 
//	
//	@Override
//	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
//			Exception ex) {
//		
//		try {
//			
//			if(ex instanceof UserException) { // 예외가 UserException 타입이면 
//				log.info("UserException resolver to 400");
//				String acceptHeader = request.getHeader("accept");
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 에러로 만들어줌
//				
//				if("application/json".contentEquals(acceptHeader)) { // accept의 미디어타입이 application/json 이면
//					Map<String,Object> errorResult = new HashMap<>();
//					errorResult.put("ex", ex.getClass());
//					errorResult.put("message", ex.getMessage());
//					
//					String result = objectMapper.writeValueAsString(errorResult); // Map을 String으로 변환
//					
//					response.setContentType("application/json");
//					response.setCharacterEncoding("utf-8");
//					response.getWriter().write(result);
//					return new ModelAndView();
//				}else {
//					// ex. TEXT/HTML
//					return new ModelAndView("error/500");
//				}
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		return null;
//	}
//	
//}
