package hello.itemservice.web.basic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//강의 핵심 : 
// @RequiredArgsConstructor 사용해서 의존관계 주입
// * @RequestMapping 을 클래스 레벨에서 사용해서 공통 uri 처리
// * 타임리프에서 url 표현식은 @ 사용해야 함 이때 변수 랜더링 필요 + 리터럴 대체 : || 
// * @PathVariable과 @ModelAttribute 사용
// * 상품 수정 등에서 완료 후 redirect는 'return "redirect:/basic/items/{itemId}";' 으로
// * HTTP API 설계 방법 : 요즘에는 파라미터를 넘기기보다 식별자 사용이 트랜드
// * PRG 방법; 등록 폼에서 새로고침 누르면 똑같은 행위 반복에 대해서 방지 하기 위해 + RedirectAttributes 사용 
// -> 근데 실무에서는 response 이용해서 js 코드 만들어서 alert 창 띄우는 형식으로 했음

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/basic/items")
public class ProductController {
	
	private final ProductRepository productRepository; 
	
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
		return "basic/items";
	}
	
	// 상품 상세
	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Product item = productRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/item";
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
		
		return "basic/addForm";
	}
	// 요구사항 추가
	// 판매 여부(체크박스), 등록 지역(체크박스 다중), 상품 종류(라디오버튼), 배송방식(selectbox)
	
	// 상품 등록 (폼과 같은 url 이지만 다른 method)
	@PostMapping("/add")
	public String save(
			@ModelAttribute("item") Product item, // 객체를 만들어주고 set까지 해줌, 어노테이션 생략 가능
			Model model,
			RedirectAttributes redirectAttributes
			) {
		log.info("product.open={}",item.getOpen());
		// HTML 에서 체크 박스를 선택하지 않고 form 전송하면 open 이라는 필드 자체가 서버로 전송되지 않음; null이 되어 문제...
		// -> hidden input을 넣어 해결 => th:filed로 간단히 해결
		log.info("product.regions={}", item.getRegions());
		log.info("product.productType={}", item.getProductType());
		Product savedProduct = productRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedProduct.getId());
		redirectAttributes.addAttribute("status", true);
		
		model.addAttribute("item", item);
		//return "redirect:/basic/items/" + item.getId(); // PRG 
		return "redirect:/basic/items/{itemId}"; // redirectAttributes에 넣은 itemId 값을 사용 가능
		// 나머지 설정한 값들은 쿼리 파라미터 형식으로 들어간다. ?status=true
	}
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
		return "basic/editForm";
	}
	
	// 상품 수정
	@PostMapping("{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Product item) {
		productRepository.update(itemId, item);
		return "redirect:/basic/items/{itemId}"; // redirect 설정은 이렇게 할 수 있음
	}
	
	// test 용 데이터 추가 (store 에 아무것도 없으니까 몇개 생성 해주기)
	@PostConstruct
	public void init() {
		productRepository.save(new Product("A", 10000, 10));
		productRepository.save(new Product("B", 20000, 20));
	}
}
