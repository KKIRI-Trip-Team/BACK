package com.kkiri_trip.back.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kkiri_trip.back.infra.S3Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatFileService {

    private final S3Service s3Service;

    // 이미지가 유효한지 검증
    public boolean isValidImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }

    // 이미지 크기가 적절한지 검증
    public boolean isFileSizeValid(MultipartFile file) {
        return file.getSize() <= 5 * 1024 * 1024;
    }

    // 파일 이름이 유효한지 검증
    public boolean isValidFileName(String fileName) {
        return fileName != null && !fileName.trim().isEmpty();
    }

    // 이미지 업로드 후 URL 반환
    public String uploadImageAndGetUrl(MultipartFile file) {
        try {
            return s3Service.uploadImage(file);  // S3에 업로드 후 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    // 썸네일 이미지 URL 생성
    public String getThumbnailUrl(String imageUrl) {
        return imageUrl.replace("/images/", "/thumbnails/");
    }
}
