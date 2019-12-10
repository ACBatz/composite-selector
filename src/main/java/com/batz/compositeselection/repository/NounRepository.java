package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Noun;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Noun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {

}
