package com.wanted.ailienlmsprogram.coursecommand.service;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.repository.ContinentRepository;
import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseApplyDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.user.entity.User;
import com.wanted.ailienlmsprogram.user.repository.UserRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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


        Course course = Course.create(continent, instructor, request.getCourseTitle(),
                request.getCourseDescription(), thumbnailUrl, request.getCoursePrice());

        courseRepository.save(course);
    }



    private String saveThumbnail(MultipartFile thumbnailFile) throws IOException {

        // 1. 저장 경로 설정
        String filePath = new File("src/main/resources/static/images/courseThumbnail").getAbsolutePath();




        // 3. 원본 파일명에서 확장자 추출
        String originFileName = thumbnailFile.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));

        // 4. UUID 로 중복되지 않는 파일명 생성
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        // 5. 파일 저장
        thumbnailFile.transferTo(new File(filePath + "/" + savedName));

        // 6. DB 에 저장할 경로 반환
        return "/images/courseThumbnail/" + savedName;

    }

    // 강좌 상세 조회
    public CourseDetailResponseDTO courseDetail(Long courseId) {

        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강좌입니다."));

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

        //List<Course> courses = courseRepository.findByInstructor_MemberIdAndCourseStatus(memberId, courseStatus);

        // fetch join 적용 메서드
        List<Course> courses = courseRepository.findMyCoursesWithInstructor(memberId, courseStatus);



        return courses.stream()
                .map(course -> {
                    CourseFindDTO dto = modelMapper.map(course, CourseFindDTO.class);
                    dto.setContinentId(course.getContinent().getContinentId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void editMyCourse(MultipartFile thumbnailFile, Long courseId, Long memberId, CourseApplyDTO request) throws IOException {

        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강좌입니다."));

        // 본인 소유 강좌인지 확인
        if (!course.getInstructor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인의 강좌만 수정할 수 있습니다.");
        }

        Continent continent = continentRepository.getReferenceById(request.getContinentId());

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
                continent

        );
    }

    public CourseFindDTO findCourseById(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강좌입니다."));

        CourseFindDTO dto = modelMapper.map(course, CourseFindDTO.class);
        dto.setContinentId(course.getContinent().getContinentId());
        return dto;
    }

}
