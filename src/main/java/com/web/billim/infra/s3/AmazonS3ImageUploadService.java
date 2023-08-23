package com.web.billim.infra.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.web.billim.exception.FailedImageUploadException;
import com.web.billim.infra.ImageUploadService;
import com.web.billim.infra.helper.ImageFileConvertHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3ImageUploadService implements ImageUploadService {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.url}")
	private String s3Url;

	@Override
	public String upload(MultipartFile image, String path) {
		File convertFile = new File(Objects.requireNonNull(image.getOriginalFilename()));
		try {
			if (convertFile.createNewFile()) {
				try (FileOutputStream fos = new FileOutputStream(convertFile)) {
					fos.write(image.getBytes());
				}
			}
			long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
			String fileName = path + "/" + timestamp + "_" + image.getOriginalFilename();
			String uploadUrl = this.put(convertFile, fileName);
			convertFile.delete();
			return uploadUrl;
		} catch (Exception ex) { // 에러의 전환 (회피, 회복)
			convertFile.delete();
			throw new FailedImageUploadException(ex);
		}
	}

	@Override
	public String upload(String encodedImage, String path) {
		// data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/7QAsUGhvdG9zaG9wIDMuMAA4QklNBCUA .....
		// Base64 로 인코딩된 이 이미지를 File 객체로 만들어 내야한다.
		String fileName = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_" + UUID.randomUUID();
		File convertFile = ImageFileConvertHelper.convertBase64EncodedStringToImageFile(encodedImage, fileName);

		try {
			String uploadUrl = this.put(convertFile, path + "/" + fileName);
			convertFile.delete();
			return uploadUrl;
		} catch (Exception ex) {
			convertFile.delete();
			throw new FailedImageUploadException(ex);
		}
	}

	@Override
	public void delete(String url) {
		// https://billim.s3.ap-northeast-2.amazonaws.com/product/1692121125_a_6e6c7e3417154a59ba69b1adaa8ad015.webp
		//   -> product/1692121125_a_6e6c7e3417154a59ba69b1adaa8ad015.webp
		String fileName = url.substring(s3Url.length() + 1);
		System.out.println(fileName);
		amazonS3Client.deleteObject(bucket, fileName);
	}


	private String put(File file, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

}

/*
    AWS S3 저장소에 Image 를 저장
     1. AWS S3 에 Image 를 업로드 -> S3 는 정적 파일(Image, JS, CSS, HTML ..)을 저장하는 웹하드
     2. AWS S3 에서 업로드된 파일에 접근할 수 있는 URL 을 제공해 줌
     3. DB 에 이 URL 만 저장해두고
     4. 그 URL 을 통해서 Client(사용자) 가 이미지에 접근할 수 있도록 한다.

    AWS S3 세팅
     1. AWS IAM 계정
     2. 그 계정에 S3 Full Access 권한 부여
     3. S3 Bucket 만들고
     4. S3 Bucket 에 접근할 수 있는 권한, 정책
     5. Spring 으로 넘어올거
 */
