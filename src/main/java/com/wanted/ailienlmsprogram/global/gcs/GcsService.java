package com.wanted.ailienlmsprogram.global.gcs;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folder, Long memberId, Long courseId) throws IOException {

        // 1. 원본 파일명에서 확장자 추출
        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 2. {memberId}/{courseId}/lecture/UUID.확장자 형태로 경로 생성
        String savedName = memberId + "/" + courseId + "/" + folder + "/" 
                         + UUID.randomUUID().toString().replace("-", "") + ext;

        // 3. GCS에 업로드할 BlobInfo 생성 (버킷명 + 파일경로 + 컨텐츠 타입)
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, savedName)
                .setContentType(file.getContentType())
                .build();

        // 4. 실제 GCS 버킷에 파일 업로드
        storage.create(blobInfo, file.getBytes());

        // 5. 공개 접근 URL 반환 (DB에 저장할 값)
        return "https://storage.googleapis.com/" + bucketName + "/" + savedName;
    }
}