package com.wanted.ailienlmsprogram.qna.service;

import com.wanted.ailienlmsprogram.coursecommand.dao.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.qna.dto.QnaResponseDTO;
import com.wanted.ailienlmsprogram.qna.dto.QnawriteRequestDTO;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import com.wanted.ailienlmsprogram.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

        List<Qna> qnaList = qnaRepository.findAllByCourse_CourseId(courseId);

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
                .course(course) // courseId(Long)가 아니라 course 객체!
                .author(member) // memberId(Long)가 아니라 member(author) 객체!
                .build();
        qnaRepository.save(qna);

    }
}
