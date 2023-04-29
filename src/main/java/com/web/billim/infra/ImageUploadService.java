package com.web.billim.infra;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
	String upload(MultipartFile image, String path) throws IOException;
}
