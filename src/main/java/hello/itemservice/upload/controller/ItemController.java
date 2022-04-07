package hello.itemservice.upload.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import hello.itemservice.upload.domain.Item;
import hello.itemservice.upload.domain.UploadFile;
import hello.itemservice.upload.file.FileStore;
import hello.itemservice.upload.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 상품 컨트롤러 (파일 포함 등록)

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemRepository itemRepository;
	private final FileStore fileStore;
	
	@GetMapping("/items/new")
	public String newItem(@ModelAttribute ItemForm form) {
		return "upload/item-form";
	}
	
	// 파일 저장
	@PostMapping("/items/new")
	public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
		// 파일 하나 저장 및 객체 생성
		UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
		// 파일 여러개 저장 및 객체 리스트 생성
		List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
		
		// 데이터베이스에 저장
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setAttachFile(attachFile);
		item.setImageFiles(storeImageFiles);
		itemRepository.save(item);
		
		redirectAttributes.addAttribute("itemId", item.getId());
		return "redirect:/items/{itemId}";
	}
	
	// 아이템 조회
	@GetMapping("/items/{id}")
	public String items(@PathVariable Long id, Model model) {
		Item item = itemRepository.findById(id);
		
		log.info("first storeFileName : {}", item.getImageFiles().get(0).getStoreFileName());
		
		model.addAttribute("item", item);
		return "upload/item-view";
	}
	
	// TODO: 파일 보기 및 다운로드
	// 실무에서는 AWS or 클라우드에 저장해놓고 그 경로를 보여주면 됐지만(ex. aws/basket/파일명), 로컬에 저장 시에는 따로 컨트롤 필요
	// 근데 굳이 컨트롤러로 만들 필요가 있을까? 그냥 타임리프에서 src에 경로만 추가해줘도 될 것 같다. 어차피 /images 라는 경로를 붙여주기 때문에..
	// 쓸만한 경우: 파일 보기 또는 접근을 제어하기 위해
	
	// 파일 img 태그 내 src
	// storeFileName 으로 fullPath를 찾아서 반환; 경로에 있는 파일을 찾아서 갖고오는 것!
	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
		log.info("filename={}", filename);
		return new UrlResource("file:" + fileStore.getFullPath(filename));
	}
	
	// 파일 다운로드
	@GetMapping("/attach/{itemId}")
	public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException{
		
		// 해당 파일에 접근이 가능한 유저인지 로직을 추가해도 됨
		
		Item item = itemRepository.findById(itemId);
		String storeFileName = item.getAttachFile().getStoreFileName(); // 저장된 파일 이름
		String uploadFileName = item.getAttachFile().getUploadFileName(); // 저장할 때 원래 파일 이름
		
		UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName)); // 전체 경로
		
		log.info("uploadFileName={}", uploadFileName);
		
		// 한글, 특수문자 깨짐 방지
		String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
		// 다운로드 받기 위해 헤더에 들어갈 정보
		String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}
	
}
