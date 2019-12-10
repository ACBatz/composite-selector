package com.batz.compositeselection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Limit.
 */
@Entity
@Table(name = "jhi_limit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Limit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "maximum")
    private Boolean maximum;

    @Column(name = "value")
    private Double value;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "jhi_limit_calculations",
               joinColumns = @JoinColumn(name = "limit_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "calculations_id", referencedColumnName = "id"))
    private Set<Calculation> calculations = new HashSet<>();

    @OneToMany(mappedBy = "limits")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();

    @OneToMany(mappedBy = "limits")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Property> properties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMaximum() {
        return maximum;
    }

    public Limit maximum(Boolean maximum) {
        this.maximum = maximum;
        return this;
    }

    public void setMaximum(Boolean maximum) {
        this.maximum = maximum;
    }

    public Double getValue() {
        return value;
    }

    public Limit value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Set<Calculation> getCalculations() {
        return calculations;
    }

    public Limit calculations(Set<Calculation> calculations) {
        this.calculations = calculations;
        return this;
    }

    public Limit addCalculations(Calculation calculation) {
        this.calculations.add(calculation);
        calculation.getLimits().add(this);
        return this;
    }

    public Limit removeCalculations(Calculation calculation) {
        this.calculations.remove(calculation);
        calculation.getLimits().remove(this);
        return this;
    }

    public void setCalculations(Set<Calculation> calculations) {
        this.calculations = calculations;
    }

    public Set<UnitOfMeasure> getUnitOfMeasures() {
        return unitOfMeasures;
    }

    public Limit unitOfMeasures(Set<UnitOfMeasure> unitOfMeasures) {
        this.unitOfMeasures = unitOfMeasures;
        return this;
    }

    public Limit addUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasures.add(unitOfMeasure);
        unitOfMeasure.setLimits(this);
        return this;
    }

    public Limit removeUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasures.remove(unitOfMeasure);
        unitOfMeasure.setLimits(null);
        return this;
    }

    public void setUnitOfMeasures(Set<UnitOfMeasure> unitOfMeasures) {
        this.unitOfMeasures = unitOfMeasures;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Limit properties(Set<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Limit addProperty(Property property) {
        this.properties.add(property);
        property.setLimits(this);
        return this;
    }

    public Limit removeProperty(Property property) {
        this.properties.remove(property);
        property.setLimits(null);
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
        if (!(o instanceof Limit)) {
            return false;
        }
        return id != null && id.equals(((Limit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Limit{" +
            "id=" + getId() +
            ", maximum='" + isMaximum() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
