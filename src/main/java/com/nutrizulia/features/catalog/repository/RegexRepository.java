package com.nutrizulia.features.catalog.repository;

import com.nutrizulia.features.catalog.model.Regex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegexRepository extends JpaRepository<Regex, String> {
}
