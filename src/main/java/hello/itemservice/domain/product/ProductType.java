package hello.itemservice.domain.product;

// 상품 종류 : 상수, 변하지 않음 -> enum 사용 (조회, 열거용)
public enum ProductType {
	
	BOOK("도서"), FOOD("음식"), ETC("기타"); // .name() 으로 꺼내올 수 있다.
	
	private final String description; // 설명을 위해서
	
	ProductType(String description){
		this.description = description;
	}
	
	// 타임리프에서 꺼내올 때는 getter를 기준으로 꺼내오기 때문에 getter를 만들어 줘야 한다.
	public String getDescription() {
		return description;
	}
	
	
	
}
