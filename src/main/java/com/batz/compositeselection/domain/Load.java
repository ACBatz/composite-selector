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
 * A Load.
 */
@Entity
@Table(name = "load")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Load implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_tensile_or_compressive")
    private Boolean isTensileOrCompressive;

    @Column(name = "is_shear")
    private Boolean isShear;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "load_calculations",
               joinColumns = @JoinColumn(name = "load_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "calculations_id", referencedColumnName = "id"))
    private Set<Calculation> calculations = new HashSet<>();

    @ManyToMany(mappedBy = "loads")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<LoadShear> loadShears = new HashSet<>();

    @ManyToMany(mappedBy = "loads")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<LoadDirection> loadDirections = new HashSet<>();

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

    public Load name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsTensileOrCompressive() {
        return isTensileOrCompressive;
    }

    public Load isTensileOrCompressive(Boolean isTensileOrCompressive) {
        this.isTensileOrCompressive = isTensileOrCompressive;
        return this;
    }

    public void setIsTensileOrCompressive(Boolean isTensileOrCompressive) {
        this.isTensileOrCompressive = isTensileOrCompressive;
    }

    public Boolean isIsShear() {
        return isShear;
    }

    public Load isShear(Boolean isShear) {
        this.isShear = isShear;
        return this;
    }

    public void setIsShear(Boolean isShear) {
        this.isShear = isShear;
    }

    public Set<Calculation> getCalculations() {
        return calculations;
    }

    public Load calculations(Set<Calculation> calculations) {
        this.calculations = calculations;
        return this;
    }

    public Load addCalculations(Calculation calculation) {
        this.calculations.add(calculation);
        calculation.getLoads().add(this);
        return this;
    }

    public Load removeCalculations(Calculation calculation) {
        this.calculations.remove(calculation);
        calculation.getLoads().remove(this);
        return this;
    }

    public void setCalculations(Set<Calculation> calculations) {
        this.calculations = calculations;
    }

    public Set<LoadShear> getLoadShears() {
        return loadShears;
    }

    public Load loadShears(Set<LoadShear> loadShears) {
        this.loadShears = loadShears;
        return this;
    }

    public Load addLoadShear(LoadShear loadShear) {
        this.loadShears.add(loadShear);
        loadShear.getLoads().add(this);
        return this;
    }

    public Load removeLoadShear(LoadShear loadShear) {
        this.loadShears.remove(loadShear);
        loadShear.getLoads().remove(this);
        return this;
    }

    public void setLoadShears(Set<LoadShear> loadShears) {
        this.loadShears = loadShears;
    }

    public Set<LoadDirection> getLoadDirections() {
        return loadDirections;
    }

    public Load loadDirections(Set<LoadDirection> loadDirections) {
        this.loadDirections = loadDirections;
        return this;
    }

    public Load addLoadDirection(LoadDirection loadDirection) {
        this.loadDirections.add(loadDirection);
        loadDirection.getLoads().add(this);
        return this;
    }

    public Load removeLoadDirection(LoadDirection loadDirection) {
        this.loadDirections.remove(loadDirection);
        loadDirection.getLoads().remove(this);
        return this;
    }

    public void setLoadDirections(Set<LoadDirection> loadDirections) {
        this.loadDirections = loadDirections;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Load)) {
            return false;
        }
        return id != null && id.equals(((Load) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Load{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isTensileOrCompressive='" + isIsTensileOrCompressive() + "'" +
            ", isShear='" + isIsShear() + "'" +
            "}";
    }
}
