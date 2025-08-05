package com.suza.wasteX.sponsor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SponsorRepository  extends JpaRepository<Sponsor, Long> {
    List<Sponsor> findByNameIn(List<String> names);

    Optional<Sponsor> findByName(String name);


}
