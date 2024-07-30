package com.suza.wasteX.startup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StartupRepository extends JpaRepository<Startup,Long> {
    Optional<Startup> findByStartup(String startup);

}
