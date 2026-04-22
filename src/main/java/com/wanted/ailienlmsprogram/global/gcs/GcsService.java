package com.wanted.ailienlmsprogram.global.gcs;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    // 비동기 업로드용 - byte[]로 직접 받아서 GCS에 업로드
    public String uploadFile(byte[] fileBytes, String contentType, String originalFileName,
                             String folder, Long memberId, Long courseId) {

        // 1. 원본 파일명에서 확장자 추출
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 2. {memberId}/{courseId}/lecture/UUID.확장자 형태로 경로 생성
        String savedName = memberId + "/" + courseId + "/" + folder + "/"
                         + UUID.randomUUID().toString().replace("-", "") + ext;

        // 3. GCS에 업로드할 BlobInfo 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, savedName)
                .setContentType(contentType)
                .build();

        // 4. byte[]로 직접 GCS 업로드
        storage.create(blobInfo, fileBytes);

        // 5. 공개 접근 URL 반환
        return "https://storage.googleapis.com/" + bucketName + "/" + savedName;
    }

    public void deleteFile(String fileUrl) {
        // URL에서 blobName 추출
        // "https://storage.googleapis.com/{bucketName}/{blobName}" 형태에서
        // {bucketName}/ 이후 문자열만 잘라냄
        String blobName = fileUrl.replace(
                "https://storage.googleapis.com/" + bucketName + "/", ""
        );
        storage.delete(bucketName, blobName);
    }
}