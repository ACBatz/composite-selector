package com.batz.compositeselection.repository;

import com.batz.compositeselection.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Property entity.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(value = "select distinct property from Property property left join fetch property.composites",
        countQuery = "select count(distinct property) from Property property")
    Page<Property> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct property from Property property left join fetch property.composites")
    List<Property> findAllWithEagerRelationships();

    @Query("select property from Property property left join fetch property.composites where property.id =:id")
    Optional<Property> findOneWithEagerRelationships(@Param("id") Long id);

}
