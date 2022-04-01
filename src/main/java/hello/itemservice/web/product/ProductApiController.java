package hello.itemservice.web.product;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.web.product.form.ProductSaveForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // String 을 써도 view 로 연결되지 않고 그대로 String 쓸 수 있게
@RequestMapping("/validation/api/items")
public class ProductApiController {
	
	@ResponseBody
	@PostMapping("/add")
	public Object add(@RequestBody @Validated ProductSaveForm form, BindingResult bindingResult) {
		// API 로 price = 'qqq' 이렇게 문자로 요청하면(typeMismatch) 400 Bad Request 에러가 떨어진다. (컨트롤러를 호출 못함)
		// @ResponseBody와 @ModelAttribute 의 차이가 있어서 그렇다.
		// @ModelAttribute는 각 필드별로 세밀하게 적용된다. but HttpMessageConverter 는 전체 객체 단위로 적용돼서 필드 중 하나라도 binding 실패하면 ProductSaveForm 객체를 생성할 수 있다.
		// 따라서 validator 적용 단계까지 넘어올 수 없다.
		// 예외 처리 방법이 따로 필요하다
		
		log.info("API 컨트롤러 호출");
		
		if(bindingResult.hasErrors()) {
			log.info("검증 오류 발생 error={}", bindingResult);
			return bindingResult.getAllErrors();
		}
		
		log.info("성공 로직 실행");
		return form;
	}

}
