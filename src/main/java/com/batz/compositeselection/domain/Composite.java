package com.batz.compositeselection.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Composite.
 */
@Entity
@Table(name = "composite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Composite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToOne(mappedBy = "composite")
    @JsonIgnore
    private CalculationResult result;

    @ManyToMany(mappedBy = "composites")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Property> properties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Composite name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CalculationResult getResult() {
        return result;
    }

    public Composite result(CalculationResult calculationResult) {
        this.result = calculationResult;
        return this;
    }

    public void setResult(CalculationResult calculationResult) {
        this.result = calculationResult;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Composite properties(Set<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Composite addProperties(Property property) {
        this.properties.add(property);
        property.getComposites().add(this);
        return this;
    }

    public Composite removeProperties(Property property) {
        this.properties.remove(property);
        property.getComposites().remove(this);
        return this;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Composite)) {
            return false;
        }
        return id != null && id.equals(((Composite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Composite{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
