package com.wanted.ailienlmsprogram.user.service;

import com.wanted.ailienlmsprogram.user.dto.UserFindDTO;
import com.wanted.ailienlmsprogram.user.dto.UserEditDTO;
import com.wanted.ailienlmsprogram.user.entity.User;
import com.wanted.ailienlmsprogram.user.repository.UserRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;
    private final ModelMapper modelMapper;

    public UserFindDTO findUserById(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다."));

        return modelMapper.map(user, UserFindDTO.class);
    }

    @Transactional
    public void editUserProfile(Long memberId, UserEditDTO request, MultipartFile profileImageFile) throws IOException {

        User user = userRepository.findById(memberId)
                                  .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다."));

        // 이미지 처리
        String profileImageUrl;
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            profileImageUrl = saveProfileImage(profileImageFile); // 새 이미지 저장
        } else {
            profileImageUrl = user.getProfileImageUrl(); // 기존 URL 유지
        }

        user.editUserInfo(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                profileImageUrl,
                request.getIntroduction()
        );

        userRepository.save(user);
    }

    private String saveProfileImage(MultipartFile file) throws IOException {

        // 1. 저장 경로 설정
        Resource resource = resourceLoader.getResource("classpath:static/images/profile");
        String filePath = new File("src/main/resources/static/images/profile").getAbsolutePath();



        // 3. 원본 파일명에서 확장자 추출
        String originFileName = file.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf("."));

        // 4. UUID 로 중복되지 않는 파일명 생성
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        // 5. 파일 저장
        file.transferTo(new File(filePath + "/" + savedName));

        // 6. DB 에 저장할 경로 반환
        return "/images/profile/" + savedName;
    }
}