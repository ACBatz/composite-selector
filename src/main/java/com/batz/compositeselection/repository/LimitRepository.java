package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Limit entity.
 */
@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    @Query(value = "select distinct limit from Limit limit left join fetch limit.calculations",
        countQuery = "select count(distinct limit) from Limit limit")
    Page<Limit> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct limit from Limit limit left join fetch limit.calculations")
    List<Limit> findAllWithEagerRelationships();

    @Query("select limit from Limit limit left join fetch limit.calculations where limit.id =:id")
    Optional<Limit> findOneWithEagerRelationships(@Param("id") Long id);

}
