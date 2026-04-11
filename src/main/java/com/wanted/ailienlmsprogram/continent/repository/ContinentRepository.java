package com.wanted.ailienlmsprogram.continent.repository;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContinentRepository extends JpaRepository<Continent, Long> {
}