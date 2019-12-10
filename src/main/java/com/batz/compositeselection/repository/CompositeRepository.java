package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Composite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Composite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompositeRepository extends JpaRepository<Composite, Long> {

}
