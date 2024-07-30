package com.suza.wasteX.wastex_site_api.repositories;

import com.suza.wasteX.wastex_site_api.models.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CarouselRepository extends JpaRepository<Carousel, Long> {
    List<Carousel> findAllByOrderByOrderIndexAsc();
    @Query("SELECT MAX(c.orderIndex) FROM Carousel c")
    Integer findMaxOrderIndex();
}
