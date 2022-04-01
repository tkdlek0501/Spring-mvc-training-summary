package hello.itemservice.web.product.form;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import hello.itemservice.domain.product.ProductType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductUpdateForm {
	
	@NotNull
	private Long id;
	
	@NotBlank
	private String productName;
	
	@NotNull
	@Range(min = 1000, max = 1000000)
	private Integer price;
	
	// 수정할 때 수량은 조건이 없음
	private Integer quantity;
	
	private Boolean open; // 판매 여부
	private List<String> regions; // 등록 지역
	private ProductType productType; // 상픔 종류; ENUM
	private String deliveryCode; // 배송 방식
}
