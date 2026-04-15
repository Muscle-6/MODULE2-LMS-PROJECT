package com.wanted.ailienlmsprogram.coursecommand.service;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.repository.ContinentRepository;
import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseCommandDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseApplyDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseEditDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.user.entity.User;
import com.wanted.ailienlmsprogram.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseCommandService {

    private final CourseRepository courseRepository;
    private final ContinentRepository continentRepository;
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;
    private final ModelMapper modelMapper;


    @Transactional
    public void applyCourse(CourseApplyDTO request, Long memberId, MultipartFile thumbnailFile) throws IOException {

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

    // 강좌 상세 조회
    public CourseDetailResponseDTO courseDetail(Long courseId) {

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(IllegalArgumentException::new);

        CourseDetailResponseDTO course = modelMapper.map(foundCourse, CourseDetailResponseDTO.class);

        if (foundCourse.getInstructor() != null) {
            course.setInstructorName(foundCourse.getInstructor().getName());
        }


        // 테스트 로직입니다
        course.setCourseDescription("지구를 정복하기 위한 최고의 자바 강의! 일루미나티도 수강 중입니다.");

        return course;
    }

    // 대륙 ID로 모든 강좌 찾기
    // 대륙 입장 시 뜨는 강좌 목록
    public List<CourseDetailResponseDTO> courseListByContinent(Long continentId) {

        // 대륙 ID로 모든 강좌 찾아옴
        List<Course> foundCourses = courseRepository.findByContinent_ContinentId(continentId);

        return foundCourses.stream()
                .map(course -> modelMapper.map(course, CourseDetailResponseDTO.class))
                .collect(Collectors.toList());

    }

    public List<CourseFindDTO> findMyCourses(Long memberId, CourseStatus courseStatus) {

        List<Course> courses = courseRepository.findByInstructor_MemberIdAndCourseStatus(memberId,courseStatus);
        List<CourseFindDTO> result = new ArrayList<>();

        for (Course course : courses) {
            CourseFindDTO courseDTO = new CourseFindDTO();
            courseDTO.setCourseId(course.getCourseId());
            courseDTO.setCourseTitle(course.getCourseTitle());
            courseDTO.setCourseDescription(course.getCourseDescription());
            courseDTO.setContinent(course.getContinent().getContinentId());
            courseDTO.setCoursePrice(course.getCoursePrice());
            courseDTO.setCourseThumbnailUrl(course.getCourseThumbnailUrl());
            courseDTO.setCourseStatus(course.getCourseStatus());
            courseDTO.setCreatedAt(course.getCreatedAt());
            result.add(courseDTO);
        }

        return result;
    }

    @Transactional
    public CourseEditDTO editMyCourse(MultipartFile thumbnailFile, Long courseId, CourseApplyDTO request) throws IOException {

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(RuntimeException::new);

        String editThumbnailUrl;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            editThumbnailUrl = saveThumbnail(thumbnailFile); // 새 이미지 저장
        } else {
            editThumbnailUrl = course.getCourseThumbnailUrl(); // 기존 URL 유지
        }

        course.editCourseInfo(
                request.getCourseTitle(),
                request.getCourseDescription(),
                editThumbnailUrl,
                request.getCoursePrice(),
                request.getContinentId()

        );

        return null;
    }
}
