package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeFormRequest;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeListResponse;
import com.wanted.ailienlmsprogram.admin.repository.AdminContinentNoticeQueryRepository;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import com.wanted.ailienlmsprogram.global.filtering.BadWordCheck;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminContinentNoticeService {

    private final AdminContinentNoticeQueryRepository adminContinentNoticeQueryRepository;

    @Transactional(readOnly = true)
    public List<AdminContinentNoticeContinentResponse> getContinents() {
        return adminContinentNoticeQueryRepository.findContinents();
    }

    @Transactional(readOnly = true)
    public AdminContinentNoticeContinentResponse getContinent(Long continentId) {
        validateContinent(continentId);
        return adminContinentNoticeQueryRepository.findContinent(continentId);
    }

    @Transactional(readOnly = true)
    public List<AdminContinentNoticeListResponse> getNoticePosts(Long continentId) {
        validateContinent(continentId);
        return adminContinentNoticeQueryRepository.findNoticePostsByContinent(continentId);
    }

    @Transactional(readOnly = true)
    public AdminContinentNoticeDetailResponse getNoticeDetail(Long continentId, Long postId) {
        validateContinent(continentId);

        AdminContinentNoticeDetailResponse detail =
                adminContinentNoticeQueryRepository.findNoticeDetail(continentId, postId);

        if (detail == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "해당 대륙의 공지글을 찾을 수 없습니다.");
        }

        return detail;
    }


    @Transactional
    @BadWordCheck
    public void createNotice(AdminContinentNoticeFormRequest request, Long adminMemberId) {
        validateContinent(request.getContinentId());
        Member adminMember = getAdminMember(adminMemberId);

        CommunityPost post = CommunityPost.create(
                request.getContinentId(),
                request.getPostTitle().trim(),
                request.getPostContent().trim(),
                adminMember,
                true
        );

        adminContinentNoticeQueryRepository.save(post);
    }

    @Transactional
    @BadWordCheck
    public void updateNotice(AdminContinentNoticeFormRequest request) {
        CommunityPost post = getTargetNotice(request.getContinentId(), request.getPostId());
        post.updateContent(request.getPostTitle().trim(), request.getPostContent().trim());
    }

    @Transactional
    public void deleteNotice(Long continentId, Long postId) {
        CommunityPost post = getTargetNotice(continentId, postId);
        post.delete();
    }

    private CommunityPost getTargetNotice(Long continentId, Long postId) {
        if (postId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "공지글 정보가 없습니다.");
        }

        CommunityPost post = adminContinentNoticeQueryRepository.findPost(postId);

        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "공지글이 존재하지 않습니다.");
        }

        if (!Objects.equals(post.getContinentId(), continentId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "선택한 대륙의 공지글이 아닙니다.");
        }

        if (post.isPostIsDeleted()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 삭제된 공지글입니다.");
        }

        if (!post.isPostIsNotice()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "관리자 공지글만 수정/삭제할 수 있습니다.");
        }

        return post;
    }

    private Member getAdminMember(Long adminMemberId) {
        Member member = adminContinentNoticeQueryRepository.findMember(adminMemberId);

        if (member == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "관리자 계정 정보를 찾을 수 없습니다.");
        }

        return member;
    }

    private void validateContinent(Long continentId) {
        if (continentId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "대륙 정보가 없습니다.");
        }

        if (adminContinentNoticeQueryRepository.findContinentEntity(continentId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "대륙이 존재하지 않습니다.");
        }
    }
}