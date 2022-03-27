package hello.itemservice.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

//테스트
//특정 메서드만 테스트 하고 싶으면 메서드 위에 ctrl + f11 누르고 junit 테스트 돌리면 된다.

class ProductRepositoryTest {
	
	ProductRepository itemRepository = new ProductRepository();
	
	// 테스트마다 초기화
	@AfterEach
	void afterEach() {
		itemRepository.clearStore();
	}
	
	@Test
	void save() {
		//given : test 를 위해 필요한 data
		Product product = new Product("A", 10000, 10); // 생성자 통해서 객체 생성
		
		//when : 이 부분을 test 함
		Product savedProduct = itemRepository.save(product); // store 에 저장
		
		//then : 결과 및 검증
		Product findProduct = itemRepository.findById(product.getId()); // 해당 id로 store 에서 꺼내옴
		assertThat(findProduct).isEqualTo(savedProduct); 
	}
	
	@Test
	void findAll() {
		//given
		// 리스트 조회니까 2개 이상 만들어 봄
		Product itemA = new Product("A", 10000, 10);
		Product itemB = new Product("B", 20000, 20);
		
		itemRepository.save(itemA);
		itemRepository.save(itemB);
		
		//when
		List<Product> findAllProduct = itemRepository.findAll();
		
		//then
		assertThat(findAllProduct.size()).isEqualTo(2); // 2개가 저장돼있는지
		assertThat(findAllProduct).contains(itemA, itemB); // 그 2개가 만들어준 2개를 포함 하는지
	}
	
	@Test
	void update() {
		//given
		Product itemA = new Product("itemA", 10000, 10); // 객체 생성
		Product savedItem = itemRepository.save(itemA); // 등록
		Long itemId = savedItem.getId();
		
		//when
		Product updateParam = new Product("itemsB", 20000, 20); // 수정할 내용의 객체 생성
		itemRepository.update(itemId, updateParam); // 수정
		
		//then
		Product updatedItem = itemRepository.findById(itemId);
		assertThat(updatedItem.getProductName()).isEqualTo(updateParam.getProductName());
		assertThat(updatedItem.getPrice()).isEqualTo(updateParam.getPrice());
		assertThat(updatedItem.getQuantity()).isEqualTo(updateParam.getQuantity());
	}
}
