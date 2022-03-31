package hello.itemservice.web.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.product.DeliveryCode;
import hello.itemservice.domain.product.Product;
import hello.itemservice.domain.product.ProductRepository;
import hello.itemservice.domain.product.ProductType;
import hello.itemservice.web.validation.form.ProductSaveForm;
import hello.itemservice.web.validation.form.ProductUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/validation-final/items")
public class ProductControllerValidFinal {
	
	private final ProductRepository productRepository;
//	private final ItemValidator itemValidator;
	
	// 본 컨트롤러에서 이 regions는 model에 무조건 담겨있게 된다.
	@ModelAttribute("regions")
	public Map<String, String> regions() {
		Map<String, String> regions = new LinkedHashMap<>(); // LinkedHashMap 을 써야 순서가 보장된다.
		regions.put("SEOUL", "서울");
		regions.put("BUSAN", "부산");
		regions.put("JEJU", "제주");
		return regions;
	}
	
	// ENUM 으로 만든 것도 넣어둘 수 있다.
	@ModelAttribute("productTypes")
	public ProductType[] productTypes() {
		return ProductType.values();
	}
	
	// 객체에 넣어주기
	// 이렇게 객체를 생성해 쓰기 보다는 미리 static으로 생성해두고 재사용하는 것이 효율적 or ENUM
	@ModelAttribute("deliveryCodes")
	public List<DeliveryCode> deliveryCodes() {
		List<DeliveryCode> deliveryCodes = new ArrayList<>();
		deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
		deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
		deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
		return deliveryCodes;
	}
	
	// 본 컨트롤러 안 어떤 메서드에서도 쓸 수 있는 binder 설정
//	@InitBinder
//	public void init(WebDataBinder dataBinder) {
//		dataBinder.addValidators(itemValidator); // validator 가져옴
//	}
	
	
	
	// 상품 리스트
	@GetMapping("")
	public String items(Model model) {
		List<Product> items = productRepository.findAll();
		model.addAttribute("items", items);
		//return "basic/items";
		return "validation2/items";
	}
	
	// 상품 상세
	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Product item = productRepository.findById(itemId);
		model.addAttribute("item", item);
		//return "basic/item";
		return "validation2/item";
	}
	
	// 상품 등록 폼
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new Product()); // 빈 객체 1개를 넣어주면 th:object / th:field 사용 가능
		return "validation2/addForm";
	}

	
	// 상품 등록 (폼과 같은 url 이지만 다른 method)
	@PostMapping("/add")
	public String save(
			@Validated @ModelAttribute("item") ProductSaveForm form, // 객체를 만들어주고 set까지 해줌, @ModelAttribute 어노테이션 생략 가능
			BindingResult bindingResult, // item에 binding 된 결과가 담김 (검증 오류 처리), 바인딩 오류를 얘가 처리하게 됨: 타입 오류가 있어도 404로 안가고 컨트롤러를 타게됨
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		// @Validated 가 Product 클래스에서 설정한 Bean Validation 어노테이션을 인식해서 validation 기능을 수행한다(지금은 field에만 적용)
		// 결론 : 이렇게 field 단위는 간단히 어노테이션을 이용해 구현하고, object 이상의 단위는 validator를 이용하거나 따로 메서드를 만들어 사용하자!
		// 검증시 하나의 object 를 초과하는 경우도 있어서 메서드 따로 만들어 사용하는게 낫겠다.
		// 등록시와 수정시에 필드 값 조건이 다르다면 @Valiated 의 group 기능을 사용하면 된다. Product 객체에서 설정 후 컨트롤러에서 사용 
		
		// + @RequestParam 의 경우에는 validation 을 어떻게 해줘야 할까? @Validated 붙이면 되나? 안되지 왜냐면 어떤 객체인지 모르니까 그냥 변수(타입)으로만 받으니까
		
		// 특정 필드가 아닌 복합 검증
		if(form.getPrice() != null && form.getQuantity() != null) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if(resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
			}
		}
		
		// 검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors = {}", bindingResult);
			return "validation2/addForm";
		}
		
		// 성공시
		Product item = new Product();
		item.setProductName(form.getProductName());
		item.setPrice(form.getPrice());
		item.setQuantity(form.getQuantity());
		item.setDeliveryCode(form.getDeliveryCode());
		item.setOpen(form.getOpen());
		item.setProductType(form.getProductType());
		item.setRegions(form.getRegions());
		
		log.info("product.open={}",item.getOpen());
		log.info("product.regions={}", item.getRegions());
		log.info("product.productType={}", item.getProductType());
		
		
		
		// store에 저장
		Product savedProduct = productRepository.save(item);
		// redirect 설정
		redirectAttributes.addAttribute("itemId", savedProduct.getId());
		redirectAttributes.addAttribute("status", true);
		
		model.addAttribute("item", item);
		return "redirect:/validation-final/items/{itemId}";
	}

	// 상품 수정 폼
	@GetMapping("{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Product item = productRepository.findById(itemId);
		model.addAttribute("item", item);
		//return "basic/editForm";
		return "validation2/editForm";
	}
	
	// 상품 수정
	@PostMapping("{itemId}/edit")
	public String edit(
			@PathVariable Long itemId, 
			@Validated @ModelAttribute("item") ProductUpdateForm form,
			BindingResult bindingResult
			) {
		
		if(form.getPrice() != null && form.getQuantity() != null) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if(resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
			}
		}
		
		if(bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation2/editForm";
		}
		
		Product item = new Product();
		item.setDeliveryCode(form.getDeliveryCode());
		item.setOpen(form.getOpen());
		item.setPrice(form.getPrice());
		item.setProductName(form.getProductName());
		item.setProductType(form.getProductType());
		item.setQuantity(form.getQuantity());
		item.setRegions(form.getRegions());
		
		
		productRepository.update(itemId, item);
		return "redirect:/validation-final/items/{itemId}";
	}
	
	// test 용 데이터 추가 (store 에 아무것도 없으니까 몇개 생성 해주기)
	@PostConstruct
	public void init() {
		productRepository.save(new Product("A", 10000, 10));
		productRepository.save(new Product("B", 20000, 20));
	}
}
