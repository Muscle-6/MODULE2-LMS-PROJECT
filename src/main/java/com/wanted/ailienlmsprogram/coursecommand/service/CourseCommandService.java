package com.wanted.ailienlmsprogram.coursecommand.service;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.repository.ContinentRepository;
import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseCommandDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.user.entity.User;
import com.wanted.ailienlmsprogram.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseCommandService {

    private final CourseRepository courseRepository;
    private final ContinentRepository continentRepository;
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;

    @Transactional
    public void applyCourse(CourseCommandDTO request, Long memberId, MultipartFile thumbnailFile) throws IOException {

        Continent continent = continentRepository.getReferenceById(request.getContinentId());
        User instructor = userRepository.getReferenceById(memberId);

        String thumbnailUrl = null;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            thumbnailUrl = saveThumbnail(thumbnailFile); // 새 이미지 저장
        }


        Course course = new Course(
                null,
                continent,
                instructor,
                request.getCourseTitle(),
                request.getCourseDescription(),
                thumbnailUrl,
                request.getCoursePrice(),
                LocalDateTime.now(),
                CourseStatus.DRAFT
        );

        courseRepository.save(course);
    }



    private String saveThumbnail(MultipartFile thumbnailFile) throws IOException {

        // 1. 저장 경로 설정
        Resource resource = resourceLoader.getResource("classpath:static/images/courseThumbnail");
        // 기존 강사님의 코드에서 경로가 없으면 만드는 코드 삭제 -> 불필요한 코드 제거로 인한 최적화
        String filePath = resource.getFile().getAbsolutePath();


        // 3. 원본 파일명에서 확장자 추출
        String originFileName = thumbnailFile.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));

        // 4. UUID 로 중복되지 않는 파일명 생성
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        // 5. 파일 저장
        thumbnailFile.transferTo(new File(filePath + "/" + savedName));

        // 6. DB 에 저장할 경로 반환
        return "/images/profile/" + savedName;

    }
}
