package hello.itemservice.api;

import org.omg.CORBA.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hello.itemservice.exception.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

// API 에서 오류 처리
// @ExceptionHandler 를 사용하자

@Slf4j
@RestController
public class ApiExceptionController {

	
// ExControllerAdvice 클래스에서 처리
	
	// @ExceptionHandler를 사용해서 이 컨트롤러 내의 IllegalArgumentException를 잡아서 처리해준다.(WAS로 예외가 넘어가지 않는다.)
	// + 이 컨트롤러는 @RestController 를 가지고 있어서 model, view가 아니라 그대로 반환해준다.
//	@ResponseStatus(HttpStatus.BAD_REQUEST) // MVC1 내용에 있었음
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ErrorResult illegalExHandler(IllegalArgumentException e) {
//		log.error("[exceptionHandler] ex", e);
//		return new ErrorResult("BAD", e.getMessage());
//	}
//	
//	@ExceptionHandler
//	public ResponseEntity<ErrorResult> exHandler(Exception e){ // 나머지 예외 처리
//		log.error("[exceptionHandler] ex", e);
//		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//		return new ResponseEntity<ErrorResult>(errorResult, HttpStatus.BAD_REQUEST);
//	}
//	
	@GetMapping("/api-ex/2/members/{id}")
	public MemberDto getMemberV2(@PathVariable("id") String id) {
		
		if(id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자");
		}
		
		if(id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}
		
		return new MemberDto(id, "hello " + id);
	}
	
	
	
	
	
	
	
	
	
// @ExceptionHandler 사용 전, 후
	
	@GetMapping("/api-ex/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		
		if(id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자");
		}
		
		if(id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}
		
		return new MemberDto(id, "hello " + id);
	}
	
	@GetMapping("/api-ex/response-status-ex2")
	public String responseStatusEx2() {	
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
	}
	
	@GetMapping("/api-ex/default-handler-ex")
	public String defaultException(@RequestParam Integer data) {
		return "ok";
	}
	
	// DTO
	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
	
}
