package hello.itemservice.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;

// 배송 코드
// FAST : 빠른 배송
// NORMAL : 일반 배송
// SLOW : 느린 배송
@Data
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
public class DeliveryCode {
	
	private String code; // 실제 코드
	private String displayName; // 고객에게 보여주는 값
	
}
