package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.MiscellaneousConstraint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the MiscellaneousConstraint entity.
 */
@Repository
public interface MiscellaneousConstraintRepository extends JpaRepository<MiscellaneousConstraint, Long> {

    @Query(value = "select distinct miscellaneousConstraint from MiscellaneousConstraint miscellaneousConstraint left join fetch miscellaneousConstraint.calculations",
        countQuery = "select count(distinct miscellaneousConstraint) from MiscellaneousConstraint miscellaneousConstraint")
    Page<MiscellaneousConstraint> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct miscellaneousConstraint from MiscellaneousConstraint miscellaneousConstraint left join fetch miscellaneousConstraint.calculations")
    List<MiscellaneousConstraint> findAllWithEagerRelationships();

    @Query("select miscellaneousConstraint from MiscellaneousConstraint miscellaneousConstraint left join fetch miscellaneousConstraint.calculations where miscellaneousConstraint.id =:id")
    Optional<MiscellaneousConstraint> findOneWithEagerRelationships(@Param("id") Long id);

}
