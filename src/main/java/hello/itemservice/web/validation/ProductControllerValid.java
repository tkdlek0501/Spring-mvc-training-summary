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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.product.DeliveryCode;
import hello.itemservice.domain.product.Product;
import hello.itemservice.domain.product.ProductRepository;
import hello.itemservice.domain.product.ProductType;
import hello.itemservice.web.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/validation/items")
public class ProductControllerValid {
	
	private final ProductRepository productRepository;
	private final ItemValidator itemValidator;
	
	// 본 컨트롤러 안 어떤 메서드에서도 쓸 수 있는 binder 설정
	@InitBinder
	public void init(WebDataBinder dataBinder) {
		dataBinder.addValidators(itemValidator); // validator 가져옴
	}
	
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
	
	//	@Autowired
	//	public BasicItemController(ItemRepository itemRepository) {
	//		this.itemRepository = itemRepository;
	//	}
	
	// 상품 리스트
	@GetMapping("")
	public String items(Model model) {
		List<Product> items = productRepository.findAll();
		model.addAttribute("items", items);
		//return "basic/items";
		return "validation/items";
	}
	
	// 상품 상세
	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Product item = productRepository.findById(itemId);
		model.addAttribute("item", item);
		//return "basic/item";
		return "validation/item";
	}
	
	// 상품 등록 폼
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new Product()); // 빈 객체 1개를 넣어주면 th:object / th:field 사용 가능
		
//		Map<String, String> regions = new LinkedHashMap<>(); // LinkedHashMap 을 써야 순서가 보장된다.
//		regions.put("SEOUL", "서울");
//		regions.put("BUSAN", "부산");
//		regions.put("JEJU", "제주");
//		model.addAttribute("regions", regions);
// 위 코드가 등록뿐만 아니라 수정, 상세에서도 나와야한다. -> 스프링에서는 @ModelAttribute의 기능으로 해결할 수 있다. 클래스 상단에 적용.
		
		//return "basic/addForm";
		return "validation/addForm";
	}

	
	// 상품 등록 (폼과 같은 url 이지만 다른 method)
	@PostMapping("/add")
	public String save(
			@Validated @ModelAttribute("item") Product item, // 객체를 만들어주고 set까지 해줌, 어노테이션 생략 가능
			BindingResult bindingResult, // item에 binding 된 결과가 담김 (검증 오류 처리), 바인딩 오류를 얘가 처리하게 됨: 타입 오류가 있어도 404로 안가고 컨트롤러를 타게됨
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		// 검증 로직
		// ItemValidator 클래스(Validator 상속)에 작업
		// itemValidator.validate(item, bindingResult); (object, bindingResult) // -> 본 컨트롤러 상단에 binder로 처리 + @Validated를 검증할 객체 앞에 붙여주기
		// @Validated를 사용함으로써 supports 되는지까지 수행된다 (모든 validator를 확인)
		
		// 검증에 실패하면 다시 입력 폼으로
		if(bindingResult.hasErrors()) {
			log.info("errors = {}", bindingResult);
			return "validation/addForm";
		}
		
		// 성공시
		log.info("product.open={}",item.getOpen());
		log.info("product.regions={}", item.getRegions());
		log.info("product.productType={}", item.getProductType());
		// store에 저장
		Product savedProduct = productRepository.save(item);
		// redirect 설정
		redirectAttributes.addAttribute("itemId", savedProduct.getId());
		redirectAttributes.addAttribute("status", true);
		
		model.addAttribute("item", item);
		return "redirect:/validation/items/{itemId}";
	}

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@	
	
	// BindingResult 설명
//	@PostMapping("/add")
//	public String save(
//			@ModelAttribute("item") Product item, // 객체를 만들어주고 set까지 해줌, 어노테이션 생략 가능
//			BindingResult bindingResult, // item에 binding 된 결과가 담김 (검증 오류 처리), 바인딩 오류를 얘가 처리하게 됨: 타입 오류가 있어도 404로 안가고 컨트롤러를 타게됨
//			Model model,
//			RedirectAttributes redirectAttributes
//			) {
//		
//		// 검증 오류 결과
//		Map<String, String> errors = new HashMap<>();
//		
//		// 검증 로직
//		if(!StringUtils.hasText(item.getProductName())) {
//			//errors.put("itemName", "상품 이름은 필수로 입력해주셔야 합니다."); - basic html에 쓰임
//			bindingResult.addError(new FieldError("item", "productName", "상품 이름은 필수 입니다."));
//			// object 이름은 @ModelAttribute를 따라가고 field 이름은 그 객체의 필드 이름과 맞춰줘야 한다.
//		}
//		if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//			//errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
//			bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
//		}
//		if(item.getQuantity() == null || item.getQuantity() > 9999) {
//			//errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
//			bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
//		}	
//		// 특정 필드가 아닌 복합 검증
//		if(item.getPrice() != null && item.getQuantity() != null) {
//			int resultPrice = item.getPrice() * item.getQuantity();
//			if(resultPrice < 10000) {
//				//errors.put("globalError", "가격 x 수량의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice);
//				bindingResult.addError(new ObjectError("item", "가격 x 수량의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
//			}
//		}
//		
//		// 검증에 실패하면 다시 입력 폼으로
//		//if (!errors.isEmpty()) {
//		if(bindingResult.hasErrors()) {
//			//log.info("errors = {} ", errors);
//			//model.addAttribute("errors", errors);
//			// return "basic/addForm";
//			log.info("errors = {}", bindingResult);
//			return "validation/addForm";
//		}
//		
//		// 성공시
//		log.info("product.open={}",item.getOpen());
//		// HTML 에서 체크 박스를 선택하지 않고 form 전송하면 open 이라는 필드 자체가 서버로 전송되지 않음; null이 되어 문제...
//		// -> hidden input을 넣어 해결 => th:filed로 간단히 해결
//		log.info("product.regions={}", item.getRegions());
//		log.info("product.productType={}", item.getProductType());
//		Product savedProduct = productRepository.save(item);
//		redirectAttributes.addAttribute("itemId", savedProduct.getId());
//		redirectAttributes.addAttribute("status", true);
//		
//		model.addAttribute("item", item);
//		//return "redirect:/basic/items/" + item.getId(); // PRG 
//		//return "redirect:/basic/items/{itemId}"; // redirectAttributes에 넣은 itemId 값을 사용 가능
//		return "redirect:/validation/items/{itemId}";
//		// 나머지 설정한 값들은 쿼리 파라미터 형식으로 들어간다. ?status=true
//	}
	
	
//		@PostMapping("/add")
//		public String save(
//				@RequestParam("itemName") String itemName,
//				@RequestParam("price") Integer price,
//				@RequestParam("quantity") Integer quantity,
//				Model model
//				) {
//			Item item = new Item();
//			item.setItemName(itemName);
//			item.setPrice(price);
//			item.setQuantity(quantity);
//			
//			itemRepository.save(item);
//			
//			model.addAttribute("item", item);
//			return "basic/item";
//		}
//	@PostMapping("/add")
//	public String save(
//			@ModelAttribute Item item, 
//			Model model
//			) {
//		itemRepository.save(item);
//		
//		//model.addAttribute("item", item); // 자동 추가, 생략 가능
//		return "basic/item";
//	}
	
	// 상품 수정 폼
	@GetMapping("{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Product item = productRepository.findById(itemId);
		model.addAttribute("item", item);
		//return "basic/editForm";
		return "validation/editForm";
	}
	
	// 상품 수정
	@PostMapping("{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Product item) {
		productRepository.update(itemId, item);
		//return "redirect:/basic/items/{itemId}"; // redirect 설정은 이렇게 할 수 있음
		return "redirect:/validation/items/{itemId}";
	}
	
	// test 용 데이터 추가 (store 에 아무것도 없으니까 몇개 생성 해주기)
	@PostConstruct
	public void init() {
		productRepository.save(new Product("A", 10000, 10));
		productRepository.save(new Product("B", 20000, 20));
	}
}
