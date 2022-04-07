package hello.itemservice.upload.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import hello.itemservice.upload.domain.UploadFile;

// 파일 관련 util? 이라고 생각
// TODO: file 등록 관련 유틸(테스트 코드 작성하기)

@Component
public class FileStore {
	
	
	@Value("${file.dir}")
	private String fileDir;
	
	// filename 을 받아서 fullPath를 반환 해주는 메서드
	public String getFullPath(String filename) {
		return fileDir + filename;
	}
	
	// 파일 여러개 리스트 반환
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IllegalStateException, IOException{
		List<UploadFile> storeFileResult = new ArrayList<>(); // 결과
		
		for (MultipartFile multipartFile : multipartFiles) {
			if(!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}
	
	// 파일 저장 및 업로드할 파일 정보가 있는 객체 생성
	public UploadFile storeFile (MultipartFile multipartFile) throws IllegalStateException, IOException {
		if(multipartFile.isEmpty()) {
			return null;
		}
		
		String originalFilename = multipartFile.getOriginalFilename();
		
		//서버에 저장하는 파일명
		// uuid + 확장자명
		String storeFileName = createStoreFileName(originalFilename);
		// fullPath
		multipartFile.transferTo(new File(getFullPath(storeFileName))); // 경로에 파일 저장
		// 만약 AWS등 클라우드 이용해서 저장하면 이 경로만 달라지겠지
		
		return new UploadFile(originalFilename, storeFileName);
	}
	
	// 저장할 파일 이름 생성 (uuid + 확장자명)
	private String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename); // 확장자명 가져오기
		String uuid = UUID.randomUUID().toString();
		String storeFileName = uuid + "." + ext;
		return storeFileName;
	}
	
	// 확장자명 가져오기
	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf("."); // 나눈 후  마지막 인덱스 뽑아오기
		String ext = originalFilename.substring(pos + 1); // 확장자명 꺼내기
		return ext;
	}
}
