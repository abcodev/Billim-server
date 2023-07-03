package com.web.billim.infra;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
	String upload(MultipartFile image, String path);
	String upload(String encodedBase64Image, String path);
}
