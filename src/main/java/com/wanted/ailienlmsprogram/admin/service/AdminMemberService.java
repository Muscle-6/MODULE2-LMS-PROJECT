package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminMemberListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminMemberSearchCondition;
import com.wanted.ailienlmsprogram.admin.repository.AdminMemberQueryRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*관리자 회원 관리 비즈니스 로직을 담당하는 서비스.
*
* - 검색 조건에 따른 회원 목록을 조회한다.
* - 특정 회원의 계정 상태를 변경한다.
* - 특정 회원을 삭제 처리한다.
* - 관리자 계정은 변경/삭제 대상에서 제외한다.*/
@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberService {

    private final AdminMemberQueryRepository adminMemberQueryRepository;

    //검색 조건에 맞는 회원 목록을 조회한다.
    public List<AdminMemberListResponse> getMembers(AdminMemberSearchCondition condition) {
        return adminMemberQueryRepository.findMembers(condition);
    }

    //특정 회원의 계정 상태를 토글한다.
    public void toggleMemberStatus(Long memberId) {
        Member member = getTarget(memberId);
        validateAdminTarget(member);

        if (member.getAccountStatus() == Member.AccountStatus.ACTIVE) {
            member.ban(LocalDateTime.now());
        } else if (member.getAccountStatus() == Member.AccountStatus.BANNED
        || member.getAccountStatus() == Member.AccountStatus.INACTIVE) {
            member.activate();
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "처리할 수 없는 계정 상태입니다.");
        }
    }

    //특정 회원을 삭제 처리한다.
    public void deleteMember(Long memberId) {
        Member member = getTarget(memberId);
        validateAdminTarget(member);

        adminMemberQueryRepository.delete(member);
    }

    //처리 대상 회원 엔티티를 조회한다.
    private Member getTarget(Long memberId) {
        Member member = adminMemberQueryRepository.findMember(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "회원이 존재하지 않습니다.");
        }
        return member;
    }

    //관리자 계정인지 검증한다.
    private void validateAdminTarget(Member member) {
        if (member.getRole() == Member.MemberRole.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "관리자 계정은 처리할 수 없습니다.");
        }
    }
}