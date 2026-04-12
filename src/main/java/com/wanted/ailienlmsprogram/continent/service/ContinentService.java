package com.wanted.ailienlmsprogram.continent.service;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.repository.ContinentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContinentService {

    private final ContinentRepository continentRepository;

    public List<Continent> findAllContinents() {
        return continentRepository.findAll();

    }

    public Continent findById(Long continentId) {
        return continentRepository.findById(continentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대륙입니다."));
    }
}
