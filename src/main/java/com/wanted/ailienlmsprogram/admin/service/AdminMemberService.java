package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminMemberListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminMemberSearchCondition;
import com.wanted.ailienlmsprogram.admin.repository.AdminMemberQueryRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberService {

    private final AdminMemberQueryRepository adminMemberQueryRepository;

    public List<AdminMemberListResponse> getMembers(AdminMemberSearchCondition condition) {
        return adminMemberQueryRepository.findMembers(condition);
    }

    public void toggleMemberStatus(Long memberId) {
        Member member = getTarget(memberId);
        validateAdminTarget(member);

        if (member.getAccountStatus() == Member.AccountStatus.ACTIVE) {
            member.setAccountStatus(Member.AccountStatus.INACTIVE);
        } else if (member.getAccountStatus() == Member.AccountStatus.INACTIVE) {
            member.setAccountStatus(Member.AccountStatus.ACTIVE);
        }

        member.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteMember(Long memberId) {
        Member member = getTarget(memberId);
        validateAdminTarget(member);

        member.setAccountStatus(Member.AccountStatus.INACTIVE);
        member.setDeletedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());
    }

    private Member getTarget(Long memberId) {
        Member member = adminMemberQueryRepository.findMember(memberId);
        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }
        return member;
    }

    private void validateAdminTarget(Member member) {
        if (member.getRole() == Member.MemberRole.ADMIN) {
            throw new IllegalArgumentException("관리자 계정은 처리할 수 없습니다.");
        }
    }
}