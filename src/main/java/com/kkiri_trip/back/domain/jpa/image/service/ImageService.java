package com.kkiri_trip.back.domain.jpa.image.service;

import com.kkiri_trip.back.domain.jpa.image.dto.Request.ImageRequestDto;
import com.kkiri_trip.back.domain.jpa.image.dto.Response.ImageResponseDto;
import com.kkiri_trip.back.domain.jpa.image.dto.Response.ImageUrlResponse;
import com.kkiri_trip.back.global.error.errorcode.FileErrorCode;
import com.kkiri_trip.back.global.error.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

import static com.kkiri_trip.back.global.config.web.AWSConfig.IMAGE_MAXIMUM_LENGTH;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public ImageResponseDto generateUploadPresignedUrl(ImageRequestDto imageRequestDto){
        if(IMAGE_MAXIMUM_LENGTH < imageRequestDto.getContentLength()){
            throw new FileException(FileErrorCode.EXCEED_SIZE_LIMIT);
        }

        if (!imageRequestDto.getContentType().startsWith("image")){
            throw new FileException(FileErrorCode.NOT_MATCHED_TYPE); // "image/~~" 이것만 가능
        }

        String key = "upload/" + UUID.randomUUID() + "_" + imageRequestDto.getFileName();

        // 어떤 파일을, 어디에, 어떤 타입으로 업로드할지 정의하는 기본 정보
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(imageRequestDto.getContentType())
                .build();

        // Presigned URL을 생성할 때의 유효 시간 및 실제 요청 정보를 포함하는 객체
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15)) // Presigned URL이 유효한 시간(15분)
                .putObjectRequest(putObjectRequest)
                .build();

        // AWS SDK v2로 실제 업로드 가능한 URL 생성
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

        return ImageResponseDto.builder()
                .presignedUrl(presignedPutObjectRequest.url().toExternalForm())
                .key(key)
                .build();
    }

    public ImageUrlResponse getImageUrl(String key) {
        try{
            String url = s3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(bucket)
                            .key(key))
                    .toExternalForm();
            return new ImageUrlResponse(url);
        }catch (SdkException e){
            throw new FileException(FileErrorCode.INVALID_KEYWORD);
        }
    }
}
