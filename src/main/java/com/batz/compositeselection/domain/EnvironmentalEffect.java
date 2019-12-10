package com.batz.compositeselection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EnvironmentalEffect.
 */
@Entity
@Table(name = "environmental_effect")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnvironmentalEffect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "environmental_effect_calculations",
               joinColumns = @JoinColumn(name = "environmental_effect_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "calculations_id", referencedColumnName = "id"))
    private Set<Calculation> calculations = new HashSet<>();

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

    public EnvironmentalEffect name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Calculation> getCalculations() {
        return calculations;
    }

    public EnvironmentalEffect calculations(Set<Calculation> calculations) {
        this.calculations = calculations;
        return this;
    }

    public EnvironmentalEffect addCalculations(Calculation calculation) {
        this.calculations.add(calculation);
        calculation.getEnvironmentalEffects().add(this);
        return this;
    }

    public EnvironmentalEffect removeCalculations(Calculation calculation) {
        this.calculations.remove(calculation);
        calculation.getEnvironmentalEffects().remove(this);
        return this;
    }

    public void setCalculations(Set<Calculation> calculations) {
        this.calculations = calculations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnvironmentalEffect)) {
            return false;
        }
        return id != null && id.equals(((EnvironmentalEffect) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EnvironmentalEffect{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
