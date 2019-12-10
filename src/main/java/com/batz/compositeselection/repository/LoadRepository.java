package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Load;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Load entity.
 */
@Repository
public interface LoadRepository extends JpaRepository<Load, Long> {

    @Query(value = "select distinct load from Load load left join fetch load.calculations",
        countQuery = "select count(distinct load) from Load load")
    Page<Load> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct load from Load load left join fetch load.calculations")
    List<Load> findAllWithEagerRelationships();

    @Query("select load from Load load left join fetch load.calculations where load.id =:id")
    Optional<Load> findOneWithEagerRelationships(@Param("id") Long id);

}
