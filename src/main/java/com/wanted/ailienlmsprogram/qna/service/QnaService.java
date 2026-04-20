package com.wanted.ailienlmsprogram.qna.service;

import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.qna.dto.*;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import com.wanted.ailienlmsprogram.qna.repository.QnaRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private  final MemberRepository memberRepository;

    public List<QnaResponseDTO> questionByCourse(Long courseId, String status) {
        // 1. 쿼리 단 1번 실행 (작성자 + 답변여부 포함)
        List<Object[]> results = qnaRepository.findAllByCourseIdWithAuthorAndReplyStatus(courseId);

        return results.stream()
                .map(result -> {
                    Qna qna = (Qna) result[0];
                    boolean isAnswered = (boolean) result[1]; // 서브쿼리 결과값 (true/false)

                    QnaResponseDTO dto = modelMapper.map(qna, QnaResponseDTO.class);
                    dto.setAuthorName(qna.getAuthor().getName()); // Fetch Join으로 이미 로딩됨
                    dto.setAnswered(isAnswered); // 추가 쿼리 없이 바로 셋팅!
                    return dto;
                })
                .filter(dto -> {
                    if("PENDING".equals(status)) return !dto.isAnswered();
                    if("RESOLVED".equals(status)) return dto.isAnswered();
                    return true;
                })
                .collect(Collectors.toList());
    }

    // qna 작성
    @Transactional
    public void qnaWrite(Long courseId, Long memberId, QnawriteRequestDTO dto) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "해당 강좌가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 요원입니다."));

        Qna qna = Qna.create(dto.getQnaTitle(), dto.getQnaContent(), course, member, null);
        qnaRepository.save(qna);

    }

    @Transactional
    public void deleteQna(Long qnaId, Long memberId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 게시글입니다. id=" + qnaId));

        if (!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다");
        }

        qna.delete();
        qnaRepository.save(qna);
    }

    // 질문 상세 조회
    public QnaDetailResponseDTO QnaDetail(Long qnaId) {
       // 질문 가져옴
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 게시글입니다. id=" + qnaId));

        if (qna.isQnaIsDeleted()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "삭제된 게시글입니다.");
        }

        QnaDetailResponseDTO responseDTO = modelMapper.map(qna, QnaDetailResponseDTO.class);
        responseDTO.setAuthorName(qna.getAuthor().getName());

        // 답변 찾기
        qnaRepository.findAllByParent_QnaIdAndQnaIsDeletedFalse(qnaId).stream()
                .findFirst()
                .ifPresent(reply -> {
                    responseDTO.setAnswerContent(reply.getQnaContent());
                    responseDTO.setAnsweredAt(reply.getCreatedAt());
                    responseDTO.setAnswered(true);
                });

        return responseDTO;
    }

    // 답변 로직
    public List<QnaDetailResponseDTO> Replies(Long qnaId) {
        List<Qna> replies = qnaRepository.findAllByParent_QnaIdAndQnaIsDeletedFalse(qnaId);

        return replies.stream()
                .map(qna -> modelMapper.map(qna, QnaDetailResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true) // 단순 조회이기에 readOnly 붙임
    public QnaModifyResponseDTO modifyPage(Long qnaId, Long memberId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 게시글입니다"));

        if(!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "수정 권한이 없습니다");
        }

        QnaModifyResponseDTO responseDTO = modelMapper.map(qna, QnaModifyResponseDTO.class);

        return responseDTO;

    }

    @Transactional
    public void modifyQna(Long qnaId, QnaModifyRequestDTO requsetDTO, Long memberId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 게시물입니다"));

        if(!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "수정 권한이 없습니다");
        }

        qna.changeTitleAndContent(requsetDTO.getQnaTitle(), requsetDTO.getQnaContent());

    }

    // 강사 답변 작성
    @Transactional
    public void replyWrite(Long courseId, Long memberId, Long parentId, QnawriteRequestDTO dto) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "해당 강좌가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 요원입니다."));

        Qna parent = qnaRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 질문입니다."));

        if (qnaRepository.existsReplyByParentId(parentId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 답변이 등록된 질문입니다.");
        }

        Qna reply = Qna.create("REPLY", dto.getQnaContent(), course, member, parent);

        qnaRepository.save(reply);
    }
}
