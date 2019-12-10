package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Verb;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Verb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VerbRepository extends JpaRepository<Verb, Long> {

}
