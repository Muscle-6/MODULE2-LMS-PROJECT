package com.wanted.ailienlmsprogram.continent.repository;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContinentRepository extends JpaRepository<Continent, Long> {

    List<Continent> findByContinentNameContaining(String query);

}