package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.EnvironmentalEffect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the EnvironmentalEffect entity.
 */
@Repository
public interface EnvironmentalEffectRepository extends JpaRepository<EnvironmentalEffect, Long> {

    @Query(value = "select distinct environmentalEffect from EnvironmentalEffect environmentalEffect left join fetch environmentalEffect.calculations",
        countQuery = "select count(distinct environmentalEffect) from EnvironmentalEffect environmentalEffect")
    Page<EnvironmentalEffect> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct environmentalEffect from EnvironmentalEffect environmentalEffect left join fetch environmentalEffect.calculations")
    List<EnvironmentalEffect> findAllWithEagerRelationships();

    @Query("select environmentalEffect from EnvironmentalEffect environmentalEffect left join fetch environmentalEffect.calculations where environmentalEffect.id =:id")
    Optional<EnvironmentalEffect> findOneWithEagerRelationships(@Param("id") Long id);

}
