package hello.itemservice.upload.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import hello.itemservice.upload.domain.Item;

// 상품 저장소 - 원래는 인터페이스만들어서 구현체 구현

@Repository
public class ItemRepository {
	
	private final Map<Long, Item> store = new HashMap<>();
	private long sequence = 0L;
	
	public Item save(Item item) {
		item.setId(++sequence);
		store.put(item.getId(), item);
		return item;
	}
	
	public Item findById(Long id) {
		return store.get(id);
	}
}
