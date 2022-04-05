package hello.itemservice.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.itemservice.exception.ErrorResult;
import lombok.extern.slf4j.Slf4j;

// 이 컨트롤러에서 모든 컨트롤러의 API 예외 처리를 해줌

@Slf4j
@RestControllerAdvice // (@ControllerAdvice + @ResponseBody)
// @RestControllerAdvice(annotation = RestController.class) // @RestController가 붙는 컨트롤러만 적용. 이 부분을 특정 패키지 or 컨트롤러로 설정해도 됨
// 이 방법으로 어느 컨트롤러에 적용할지 나눠서 사용하면 된다.

// 오류는 어떤 경우에 어떻게 발생시켜야 하는지? -> 클라이언트가 잘못된 argument 보냈을 때는 500이 아닌 400 에러로 처리, ...

public class ExControllerAdvice {
	
		// @ExceptionHandler를 사용해서 이 컨트롤러 내의 IllegalArgumentException를 잡아서 처리해준다.(WAS로 예외가 넘어가지 않는다.)
		// + 이 컨트롤러는 @RestController 를 가지고 있어서 model, view가 아니라 그대로 반환해준다.
		@ResponseStatus(HttpStatus.BAD_REQUEST) // MVC1 내용에 있었음
		@ExceptionHandler(IllegalArgumentException.class)
		public ErrorResult illegalExHandler(IllegalArgumentException e) {
			log.error("[exceptionHandler] ex", e);
			return new ErrorResult("BAD", e.getMessage());
		}
		
		@ExceptionHandler
		public ResponseEntity<ErrorResult> exHandler(Exception e){ // 나머지 예외 처리
			log.error("[exceptionHandler] ex", e);
			ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
			return new ResponseEntity<ErrorResult>(errorResult, HttpStatus.BAD_REQUEST);
		}
		
	
}
