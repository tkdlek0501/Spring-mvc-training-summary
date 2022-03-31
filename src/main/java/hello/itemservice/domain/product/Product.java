package hello.itemservice.domain.product;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Data // 롬복에 있는 @Getter @Setter 어노테이션 뿐만 아니라 여러 어노테이션을 한번에 가져옴 -> 위험할 수 있음, 아래와 같이 하나씩 꺼내는 방법이 좋다. -> DTO 처럼 data 가공이 없는 경우에는 상관없음
@Getter
@Setter
@ToString
//@Alias()
//@ScriptAssert(lang = "javascript", script = "_this.price + _this.quantity >= 10000", message="가격 x 수량의 값은 10000원이 넘어야 합니다.")
public class Product {
	
	@NotNull(groups = UpdateCheck.class) // 수정시에는 필수
	private Long id;
	
	// field 단위 : validate 라이브러리에서 지원하는 validate 관련 어노테이션을 필드에 붙임
	// object 단위 : @ScriptAssert() 를 사용하면 되긴 하는데 제약 조건이 많다. -> 어노테이션이 아닌 자바 코드로 작성하는게 유연하다.
	// 결론 : field 에러는 어노테이션으로 object 에러는 직접 자바 코드로 작성하기
	
	@NotBlank(message = "빈 값일 수 없습니다.", groups = {SaveCheck.class, UpdateCheck.class}) // 빈값 or 공백 미허용
	private String productName;
	
	// int 는 null 이 들어가지 못한다. Integer는 null 대입 가능 -> 가격이나 수량이 null 이 될 수 있냐 없냐로 판단
	@NotNull(message = "공백일 수 없습니다.", groups = {SaveCheck.class, UpdateCheck.class}) // null 미허용
	@Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class}) // 범위 안의 값 허용
	private Integer price;
	
	@NotNull(message = "공백일 수 없습니다.", groups = {SaveCheck.class, UpdateCheck.class})
	@Max(value = 9999, groups = SaveCheck.class) // 등록시에는 최대값까지 허용   but 수정시에는 상한선이 없음
	private Integer quantity;
	
	private Boolean open; // 판매 여부
	private List<String> regions; // 등록 지역
	private ProductType productType; // 상픔 종류; ENUM
	private String deliveryCode; // 배송 방식
	
	// 기본 생성자
	public Product() {
	}
	
	// id를 제외한 생성자
	public Product(String productName, Integer price, Integer quantity) {
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
	}
}
