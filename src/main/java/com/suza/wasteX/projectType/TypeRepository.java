package com.suza.wasteX.projectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);

    @Query("SELECT COUNT(t) FROM Type t WHERE LOWER(t.name) = LOWER(:name)")
    Long countByNameIgnoreCase(@Param("name") String name);

}
