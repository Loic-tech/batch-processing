package com.medicaldust.batch.repository;

import com.medicaldust.batch.entity.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeRepository extends JpaRepository<Range, Long> {


    @Modifying
    @Query(value = "UPDATE Range r SET r.min=?1 WHERE r.id=1")
    int incrementValue(@Param("min") Integer value);
}
