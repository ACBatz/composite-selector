package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Calculation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Calculation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {

}
