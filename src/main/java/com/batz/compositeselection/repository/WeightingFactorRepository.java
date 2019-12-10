package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.WeightingFactor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WeightingFactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeightingFactorRepository extends JpaRepository<WeightingFactor, Long> {

}
