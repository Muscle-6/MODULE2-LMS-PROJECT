package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeWritePageResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeWriteRequest;
import com.wanted.ailienlmsprogram.admin.entity.AdminContinentNoticePost;
import com.wanted.ailienlmsprogram.admin.repository.AdminContinentNoticeCommandRepository;
import com.wanted.ailienlmsprogram.admin.repository.AdminContinentNoticeQueryRepository;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminContinentNoticeService {

    private final AdminContinentNoticeQueryRepository queryRepository;
    private final AdminContinentNoticeCommandRepository commandRepository;

    @Transactional(readOnly = true)
    public List<AdminContinentNoticeContinentResponse> getContinents() {
        return queryRepository.findContinentsForNotice();
    }

    @Transactional(readOnly = true)
    public AdminContinentNoticeWritePageResponse getWritePage(Long continentId) {
        AdminContinentNoticeWritePageResponse page = queryRepository.findWriteTarget(continentId);
        if (page == null) {
            throw new IllegalArgumentException("선택한 대륙이 존재하지 않습니다.");
        }
        return page;
    }

    public void writeNotice(Long adminMemberId, AdminContinentNoticeWriteRequest request) {
        Long continentId = request.getContinentId();

        validateContinent(continentId);
        Member admin = getValidatedAdmin(adminMemberId);

        String title = request.getTitle() == null ? "" : request.getTitle().trim();
        String content = request.getContent() == null ? "" : request.getContent().trim();

        if (title.isBlank()) {
            throw new IllegalArgumentException("공지 제목을 입력해주세요.");
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("공지 내용을 입력해주세요.");
        }

        AdminContinentNoticePost notice =
                AdminContinentNoticePost.createNotice(continentId, admin, title, content);

        commandRepository.save(notice);
    }

    private void validateContinent(Long continentId) {
        Continent continent = queryRepository.findContinent(continentId);
        if (continent == null) {
            throw new IllegalArgumentException("선택한 대륙이 존재하지 않습니다.");
        }
    }

    private Member getValidatedAdmin(Long adminMemberId) {
        Member admin = queryRepository.findMember(adminMemberId);
        if (admin == null) {
            throw new IllegalArgumentException("관리자 정보를 찾을 수 없습니다.");
        }
        if (admin.getRole() != Member.MemberRole.ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 작성할 수 있습니다.");
        }
        return admin;
    }
}