package com.nutrizulia.features.catalog.repository;

import com.nutrizulia.features.catalog.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, String> {
}
