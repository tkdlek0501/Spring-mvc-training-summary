package hello.itemservice.upload.domain;

import java.util.List;

import lombok.Data;

@Data
public class Item {
	
	private Long id;
	private String itemName;
	private UploadFile attachFile;
	private List<UploadFile> imageFiles; // 하나의 상품에 여러개 이미지 가능
	
}
