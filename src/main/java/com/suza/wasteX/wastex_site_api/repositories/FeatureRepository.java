package com.suza.wasteX.wastex_site_api.repositories;

import com.suza.wasteX.wastex_site_api.models.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
}
