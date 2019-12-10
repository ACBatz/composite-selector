package com.batz.compositeselection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MiscellaneousConstraint.
 */
@Entity
@Table(name = "miscellaneous_constraint")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MiscellaneousConstraint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "maximize")
    private Boolean maximize;

    @Column(name = "minimize")
    private Boolean minimize;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "miscellaneous_constraint_calculations",
               joinColumns = @JoinColumn(name = "miscellaneous_constraint_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "calculations_id", referencedColumnName = "id"))
    private Set<Calculation> calculations = new HashSet<>();

    @OneToMany(mappedBy = "miscellaneousConstraint")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    public MiscellaneousConstraint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isMaximize() {
        return maximize;
    }

    public MiscellaneousConstraint maximize(Boolean maximize) {
        this.maximize = maximize;
        return this;
    }

    public void setMaximize(Boolean maximize) {
        this.maximize = maximize;
    }

    public Boolean isMinimize() {
        return minimize;
    }

    public MiscellaneousConstraint minimize(Boolean minimize) {
        this.minimize = minimize;
        return this;
    }

    public void setMinimize(Boolean minimize) {
        this.minimize = minimize;
    }

    public Set<Calculation> getCalculations() {
        return calculations;
    }

    public MiscellaneousConstraint calculations(Set<Calculation> calculations) {
        this.calculations = calculations;
        return this;
    }

    public MiscellaneousConstraint addCalculations(Calculation calculation) {
        this.calculations.add(calculation);
        calculation.getMiscConstraints().add(this);
        return this;
    }

    public MiscellaneousConstraint removeCalculations(Calculation calculation) {
        this.calculations.remove(calculation);
        calculation.getMiscConstraints().remove(this);
        return this;
    }

    public void setCalculations(Set<Calculation> calculations) {
        this.calculations = calculations;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public MiscellaneousConstraint properties(Set<Property> properties) {
        this.properties = properties;
        return this;
    }

    public MiscellaneousConstraint addProperty(Property property) {
        this.properties.add(property);
        property.setMiscellaneousConstraint(this);
        return this;
    }

    public MiscellaneousConstraint removeProperty(Property property) {
        this.properties.remove(property);
        property.setMiscellaneousConstraint(null);
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
        if (!(o instanceof MiscellaneousConstraint)) {
            return false;
        }
        return id != null && id.equals(((MiscellaneousConstraint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MiscellaneousConstraint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", maximize='" + isMaximize() + "'" +
            ", minimize='" + isMinimize() + "'" +
            "}";
    }
}
