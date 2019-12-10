package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.CalculationResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CalculationResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalculationResultRepository extends JpaRepository<CalculationResult, Long> {

}
