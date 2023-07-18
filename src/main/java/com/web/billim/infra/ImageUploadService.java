package com.web.billim.infra;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
	String upload(MultipartFile image, String path);
	void delete(String url);
}
