package hello.itemservice.upload;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.util.IOUtils;



@Component
public class AwsUploader {
	
private final AmazonS3 amzonS3;
	
	// 생성자(key 등록)
	public AwsUploader() {
			String accessKey = "";
			String secretKey = "";
		   	//this.amzonS3 = new AmazonS3Client(new BasicAWSCredentials(accessKey,secretKey));
			
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		   	amzonS3 = AmazonS3ClientBuilder.standard()
	                .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                .withRegion(Regions.AP_NORTHEAST_2)
	                .build();
	}
	
	// 서비스에서 호출
    public String returnSaveFileName(MultipartFile image, String folder) throws IOException {

        String saveFileName = image.getOriginalFilename();
        saveFileName = getChangedFilename(saveFileName);

        if (uploadImageToWithNameAwsS3(folder, image, saveFileName) == false) {
            System.out.println("S3에 업로드할게 없거나 이미지 업로드 오류 발생");
        }

        return folder + "/" + saveFileName;
    }

    private String getChangedFilename(String dBFileName) {
        String fileName = "";
        Date date = new Date();
        fileName += date.getTime() + "_" + dBFileName;
        return fileName;
    }
   
    
    // 업로드
 	public boolean uploadImageToWithNameAwsS3(String path, MultipartFile file, String fileName) {
 	
 		System.out.println(file.getOriginalFilename());
         String bucketName = "bucketName/" + path; // path 에 그 이미지가 들어갈 폴더 이름도 같이

         try {
         	
 		    File convFile = new File(file.getOriginalFilename());
 		    convFile.createNewFile();
 		    FileOutputStream fos = new FileOutputStream(convFile);
 		    fos.write(file.getBytes());
 		    fos.close();
 		
// 		    File result = imageResize(convFile);
 		
         	PutObjectRequest putObjectRequeset = new PutObjectRequest(bucketName, fileName, convFile);
         	putObjectRequeset.setCannedAcl(CannedAccessControlList.PublicRead);
         	
         	amzonS3.putObject(putObjectRequeset);
         	convFile.delete();
         	return true;
         	
         } catch (Exception ex) {
         	
         	ex.printStackTrace();
         	System.out.println("index : " + ex);
         	return false;
         }
 	}
 	
 	// 다운로드
 	public ResponseEntity<byte[]> fileDownloader(String file_path) throws IOException {
		   String[] filePathArr = file_path.split("/");
		   
		   String bucketName = "..." + filePathArr[0]; 
		   String objectName = filePathArr[1];
		  
		   
		   // download object
		   try {
		       S3Object s3Object = amzonS3.getObject(bucketName, objectName);
		       S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
		       
		       byte[] bytes = IOUtils.toByteArray(s3ObjectInputStream);
		       //OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
		      
		       String realFileName = URLEncoder.encode(objectName, "UTF-8").replaceAll("\\+", "%20");
		        HttpHeaders httpHeaders = new HttpHeaders();
		        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		        httpHeaders.setContentLength(bytes.length);
		        httpHeaders.setContentDispositionFormData("attachment", realFileName);
		        
		       return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
		   } catch(AmazonS3Exception e) {
			   e.printStackTrace();
		       return null;
		   }
 	}
 	// 다운로드 서비스에서 사용법
// 	@GetMapping("download-file")
//	public ResponseEntity<byte[]> downloadFile(
//			@RequestParam("filePath") String filePath,
//			) throws Exception{
//		
//		return awsUploader.fileDownloader(filePath);
//		
//	}
 	
 	
 	// 삭제
 	public boolean deleteFileInAwsByKey(String path , String fileName) {
 		
 		try
 		{
 			   String bucketName = "pref-bucket/images/herren/" + path;
 		       amzonS3.deleteObject(bucketName,fileName);
 		        return true;
 		}
 		catch(Exception ex){
 			System.out.println(ex.getMessage());
 		}
 	
         return false;
 	}
 	
 	public File imageResize(File file) {
		
		
        String imgFormat = "jpg";                             // 새 이미지 포맷. jpg, gif 등
        int newWidth = 600;                                  // 변경 할 넓이
        int newHeight = 700;                                 // 변경 할 높이
        String mainPosition = "W";                             // W:넓이중심, H:높이중심, X:설정한 수치로(비율무시)

        Image image;
        int imageWidth;
        int imageHeight;
        double ratio;
        int w;
        int h;

        try{
            // 원본 이미지 가져오기
            image = ImageIO.read(file);

            // 원본 이미지 사이즈 가져오기
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);

            if(mainPosition.equals("W")){    // 넓이기준

                ratio = (double)newWidth/(double)imageWidth;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }else if(mainPosition.equals("H")){ // 높이기준

                ratio = (double)newHeight/(double)imageHeight;
                w = (int)(imageWidth * ratio);
                h = (int)(imageHeight * ratio);

            }else{ //설정값 (비율무시)

                w = newWidth;
                h = newHeight;
            }

            // 이미지 리사이즈
            // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
            Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);

            // 새 이미지  저장하기
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();

            File resultFile = new File(file.getName());
            ImageIO.write(newImage, imgFormat, resultFile);

            return resultFile;

        }catch (Exception e){

            e.printStackTrace();
            return null;
        }

    }
}
