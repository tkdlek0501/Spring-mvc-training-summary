package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import hello.itemservice.domain.product.Product;

// 상품 등록 시 validation 해주는 클래스
@Component // bean으로 등록
public class ItemValidator implements Validator{
	
	// validator 지원하는지 
	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.isAssignableFrom(clazz);
		// product == clazz , product == subProduct
	}
	
	// bindingResult는 extends Errors 이고 rejectValue, reject는 Errors에도 있다
	@Override
	public void validate(Object target, Errors errors) {
		Product item = (Product) target;
		
//		if(!StringUtils.hasText(item.getProductName())) {
//			errors.rejectValue("productName", "required"); // (필드명, 코드명) / 이미 bindingResult는 @ModelAttribute의 객체를 알고 있으므로 object는 생략
			// bindingResult.addError(new FieldError("item", "productName", item.getProductName(), false, new String[] {"required.item.productName"}, null,  "상품 이름은 필수 입니다.(default)"));
			// 'item.getProductName()' 는 보존하는 값(rejectedValue)으로 쓰인다. (유저가 입력한 값을 저장해놓기 위해) + th:field 가 오류시에 이 값을 쓰게 된다
//		}
//		if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//			errors.rejectValue("price", "range", new Object[] {1000, 1000000}, null);
			//bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"}, new Object[] {1000, 1000000}, null));
//		}
//		if(item.getQuantity() == null || item.getQuantity() > 9999) {
//			errors.rejectValue("quantity", "max", new Object[] {9999}, null);
			//bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[] {"max.item.quantity"}, new Object[] {9999}, null));
//		}	
		// 특정 필드가 아닌 복합 검증
		if(item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if(resultPrice < 10000) {
				errors.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
			}
		}
		
	}
	
	
}
