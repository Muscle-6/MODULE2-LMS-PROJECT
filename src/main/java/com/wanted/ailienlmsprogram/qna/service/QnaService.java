package com.wanted.ailienlmsprogram.qna.service;

import com.wanted.ailienlmsprogram.qna.dto.QnaResponseDTO;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import com.wanted.ailienlmsprogram.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;

    public List<QnaResponseDTO> questionByCourse(Long courseId) {

        List<Qna> qnaList = qnaRepository.findAllByCourse_CourseId(courseId);

        return qnaList.stream()
                .map(qna -> modelMapper.map(qna, QnaResponseDTO.class))
                .collect(Collectors.toList());
    }
}
