package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.LoadDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the LoadDirection entity.
 */
@Repository
public interface LoadDirectionRepository extends JpaRepository<LoadDirection, Long> {

    @Query(value = "select distinct loadDirection from LoadDirection loadDirection left join fetch loadDirection.loads",
        countQuery = "select count(distinct loadDirection) from LoadDirection loadDirection")
    Page<LoadDirection> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct loadDirection from LoadDirection loadDirection left join fetch loadDirection.loads")
    List<LoadDirection> findAllWithEagerRelationships();

    @Query("select loadDirection from LoadDirection loadDirection left join fetch loadDirection.loads where loadDirection.id =:id")
    Optional<LoadDirection> findOneWithEagerRelationships(@Param("id") Long id);

}
