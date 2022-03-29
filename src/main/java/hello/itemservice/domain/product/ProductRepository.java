package hello.itemservice.domain.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

//사실 Respository 패키지도 나누는게 좋지만 작은 규모의 프로젝트라면 도메인과 같이 넣어도 된다. 
@Repository
public class ProductRepository {
	
	// id가 Long 타입이므로 id로 부터 가져올 수 있게 Map<Long, Item> 타입 사용
	// 실제로는 HashMap 을 사용하면 안된다.(동시에 여러 접근이 가능한 멀티 쓰레드 환경에서는 안됨!! synchronized 키워드가 존재하지 않음.)
	// -> ConcurrentHashMap<>() 을 사용해야 됨
	private static final Map<Long, Product> store = new HashMap<>(); // static
	// id로 들어갈 값 (auto increased 되는 PK 라고 생각)
	private static Long sequence = 0L; // static
	
	// 저장 기능
	public Product save(Product product) {
		product.setId(++sequence);
		store.put(product.getId(), product);
		return product;
	}
	
	// 해당 id의 상품 조회
	public Product findById(Long id) {
		return store.get(id);
	}
	
	// 상품 목록 조회
	public List<Product> findAll() {
		return new ArrayList<>(store.values());
	}
	
	// 상품 수정
		// 사실 update 용 객체를 사용해야함. 수정시에 id는 필요없는데 Item 객체를 사용하고 있다.
		// -> UpdateItemParam 객체를 생성해서 itemName, price, quantity 멤버 변수만 쓰는게 맞음. 
		// 일종의 딜레마가 생길 수 있다. DTO를 하나 더 만드는 일이 귀찮더라도 중복과 명확성 중 하나를 골라야 한다면 명확한게 더 좋다. (유지보수 측면에서 훨씬)
	public void update(Long itemId, Product updateParam) {
		Product findProduct = findById(itemId);
		findProduct.setProductName(updateParam.getProductName());
		findProduct.setPrice(updateParam.getPrice());
		findProduct.setQuantity(updateParam.getQuantity());
		findProduct.setOpen(updateParam.getOpen());
		findProduct.setRegions(updateParam.getRegions());
		findProduct.setProductType(updateParam.getProductType());
		findProduct.setDeliveryCode(updateParam.getDeliveryCode());
	}
	
	// test용 store 비우기 (전체 삭제)
	public void clearStore() {
		store.clear();
	}
}
