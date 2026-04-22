package com.wanted.ailienlmsprogram.continent.service;

import com.wanted.ailienlmsprogram.continent.dto.ContinentAllResponseDTO;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.repository.ContinentRepository;
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
@Transactional(readOnly = true)
public class ContinentService {

    private final ContinentRepository continentRepository;
    private final ModelMapper modelMapper;

    // 대륙 전체 조회
    public List<ContinentAllResponseDTO> findAllContinents(String query) {

        List<Continent> continentList;

        if(query == null || query.trim().isBlank()) {
            continentList = continentRepository.findAll();
        } else {
            String result = query.trim();
            continentList = continentRepository.findByContinentNameContaining(result);
        }

        return continentList.stream()
                .map(continent -> modelMapper.map(continent , ContinentAllResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 대륙 상세 조회
    public ContinentAllResponseDTO getContinentDetail(Long continentId) {

        Continent continent = continentRepository.findById(continentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 대륙입니다."));

        ContinentAllResponseDTO continentAllResponseDTO = modelMapper.map(continent, ContinentAllResponseDTO.class);

        return continentAllResponseDTO;
    }
}