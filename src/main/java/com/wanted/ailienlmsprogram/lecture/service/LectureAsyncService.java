package com.wanted.ailienlmsprogram.lecture.service;

import com.wanted.ailienlmsprogram.global.gcs.GcsService;
import com.wanted.ailienlmsprogram.lecture.dao.LectureRepository;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureAsyncService {

    private final GcsService gcsService;
    private final LectureRepository lectureRepository;

    /**
     * 강의 삭제 시 사용
     * GCS 파일 삭제 (동기 처리 — DB 삭제와 함께 처리되어야 하므로 @Async 없음)
     */
    public void deleteVideoFile(String videoUrl) {
        if (videoUrl != null) {
            gcsService.deleteFile(videoUrl);
            log.info("[GCS] 강의 영상 삭제 완료 - url: {}", videoUrl);
        }
    }

    /**
     * 강의 등록 시 사용
     * GCS 업로드 후 videoUrl DB 업데이트
     *
     * @Async : 이 메서드는 호출 즉시 별도 스레드에서 실행됩니다.
     *          호출한 쪽(LectureService)은 이 메서드의 완료를 기다리지 않습니다.
     */
    @Async
    @Transactional
    public void uploadAndUpdateVideoUrl(byte[] fileBytes, String contentType, String originalFileName,
                                        String folder, Long memberId, Long courseId, Long lectureId) {
        try {
            log.info("[ASYNC] 강의 영상 GCS 업로드 시작 - lectureId: {}", lectureId);

            // 1. GCS 업로드 (백그라운드에서 처리)
            String videoUrl = gcsService.uploadFile(fileBytes, contentType, originalFileName,
                    folder, memberId, courseId);

            // 2. 업로드 완료 후 videoUrl DB 업데이트
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

            lecture.updateVideoUrl(videoUrl);

            log.info("[ASYNC] 강의 영상 GCS 업로드 완료 - lectureId: {}, url: {}", lectureId, videoUrl);

        } catch (Exception e) {
            log.error("[ASYNC] 강의 영상 GCS 업로드 실패 - lectureId: {}, error: {}", lectureId, e.getMessage());
        }
    }

    /**
     * 강의 수정 시 사용
     * 기존 GCS 파일 삭제 → 새 파일 업로드 → videoUrl DB 업데이트
     */
    @Async
    @Transactional
    public void deleteAndUploadAndUpdateVideoUrl(byte[] fileBytes, String contentType, String originalFileName,
                                                  String folder, Long memberId, Long lectureId,
                                                  String oldVideoUrl) {
        try {
            log.info("[ASYNC] 강의 영상 수정 GCS 처리 시작 - lectureId: {}", lectureId);

            // 1. 기존 GCS 파일 삭제
            if (oldVideoUrl != null) {
                gcsService.deleteFile(oldVideoUrl);
                log.info("[ASYNC] 기존 영상 GCS 삭제 완료 - lectureId: {}", lectureId);
            }

            // 2. 새 파일 GCS 업로드
            //    수정은 lectureId 기준으로 경로 생성 (applyLecture와 동일하게 맞춤)
            String videoUrl = gcsService.uploadFile(fileBytes, contentType, originalFileName,
                    folder, memberId, lectureId);

            // 3. videoUrl DB 업데이트
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

            lecture.updateVideoUrl(videoUrl);

            log.info("[ASYNC] 강의 영상 수정 GCS 처리 완료 - lectureId: {}, url: {}", lectureId, videoUrl);

        } catch (Exception e) {
            log.error("[ASYNC] 강의 영상 수정 GCS 처리 실패 - lectureId: {}, error: {}", lectureId, e.getMessage());
        }
    }
}
