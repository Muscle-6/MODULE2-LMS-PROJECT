package com.wanted.ailienlmsprogram.continent.repository;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContinentRepository extends JpaRepository<Continent, Long> {


    List<Continent> findByContinentNameContaining(String result);
}