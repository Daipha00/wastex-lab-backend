package com.suza.wasteX.wastex_site_api.repositories;

import com.suza.wasteX.wastex_site_api.models.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
