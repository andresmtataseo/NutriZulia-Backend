package com.nutrizulia.catalog.repository;

import com.nutrizulia.catalog.model.Regex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegexRepository extends JpaRepository<Regex, String> {
}
