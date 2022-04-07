package hello.itemservice.upload.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

// form에서 받을 data

@Data
public class ItemForm {
	
	private Long itemId;
	private String itemName;
	private List<MultipartFile> imageFiles;
	private MultipartFile attachFile;
	
}
