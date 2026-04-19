package com.wanted.ailienlmsprogram.qna.service;

import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.qna.dto.*;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import com.wanted.ailienlmsprogram.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private  final MemberRepository memberRepository;

    public List<QnaResponseDTO> questionByCourse(Long courseId) {

        List<Qna> qnaList = qnaRepository.findAllByCourse_CourseIdAndQnaIsDeletedFalse(courseId);

        return qnaList.stream()
                .map(qna -> modelMapper.map(qna, QnaResponseDTO.class))
                .collect(Collectors.toList());
    }

    // qna 작성
    @Transactional
    public void qnaWrite(Long courseId, Long memberId, QnawriteRequestDTO dto) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요원입니다."));

        Qna qna = Qna.builder()
                .qnaTitle(dto.getQnaTitle())
                .qnaContent(dto.getQnaContent())
                .createdAt(LocalDateTime.now())
                .course(course)
                .author(member)
                .build();
        qnaRepository.save(qna);

    }

    @Transactional
    public void deleteQna(Long qnaId, Long memberId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + qnaId));

        // 작성자 존재하는지
        if (!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new AccessDeniedException("본인이 작성한 글만 삭제할 수 있습니다");
        }

        qna.delete();
        qnaRepository.save(qna);
    }

    // 질문 상세 조회
    public QnaDetailResponseDTO QnaDetail(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + qnaId));

        if (qna.isQnaIsDeleted()) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }

        return modelMapper.map(qna, QnaDetailResponseDTO.class);
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        if(!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다");
        }

        QnaModifyResponseDTO responseDTO = modelMapper.map(qna, QnaModifyResponseDTO.class);

        return responseDTO;

    }

    @Transactional
    public void modifyQna(Long qnaId, QnaModifyRequestDTO requsetDTO, Long memberId) {

        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        if(!qna.getAuthor().getMemberId().equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다!");
        }

        qna.changeTitleAndContent(requsetDTO.getQnaTitle(), requsetDTO.getQnaContent());

    }
}
