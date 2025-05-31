package com.kkiri_trip.back.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    // 이미지 업로드 (원본)
    public String uploadImage(MultipartFile file) throws IOException {
        String key = "images/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (S3Exception e) {
            throw new RuntimeException("S3에 업로드하는데 실패했습니다.", e);
        }
        return getImageUrl(key);
    }

    // 썸네일 생성 후 업로드
    public String generateThumbnail(MultipartFile file) throws IOException {
        String thumbnailKey = "images/thumbnails/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(thumbnailKey)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (S3Exception e) {
            throw new RuntimeException("S3에 썸네일 업로드하는데 실패했습니다.", e);
        }
        return getImageUrl(thumbnailKey);
    }

    // S3에서 이미지 URL 가져오기
    public String getImageUrl(String key) {
        // bucketName을 URL에서 제거하고 key만 사용
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + key;
    }
}
