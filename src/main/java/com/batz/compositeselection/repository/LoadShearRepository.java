package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.LoadShear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the LoadShear entity.
 */
@Repository
public interface LoadShearRepository extends JpaRepository<LoadShear, Long> {

    @Query(value = "select distinct loadShear from LoadShear loadShear left join fetch loadShear.loads",
        countQuery = "select count(distinct loadShear) from LoadShear loadShear")
    Page<LoadShear> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct loadShear from LoadShear loadShear left join fetch loadShear.loads")
    List<LoadShear> findAllWithEagerRelationships();

    @Query("select loadShear from LoadShear loadShear left join fetch loadShear.loads where loadShear.id =:id")
    Optional<LoadShear> findOneWithEagerRelationships(@Param("id") Long id);

}
