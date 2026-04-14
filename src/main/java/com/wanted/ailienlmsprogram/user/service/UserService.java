package com.wanted.ailienlmsprogram.user.service;

import com.wanted.ailienlmsprogram.user.dto.UserDTO;
import com.wanted.ailienlmsprogram.user.entity.User;
import com.wanted.ailienlmsprogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO findUserById(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        UserDTO dto = new UserDTO();
        dto.setLoginId(user.getLoginId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setRole(user.getRole());
        dto.setRank(user.getRank());
        dto.setIntroduction(user.getIntroduction());
        return dto;
    }
}