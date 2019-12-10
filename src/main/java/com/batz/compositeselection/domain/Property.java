package com.batz.compositeselection.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Property.
 */
@Entity
@Table(name = "property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("properties")
    private WeightingFactor weightingFactors;

    @ManyToOne
    @JsonIgnoreProperties("properties")
    private Limit limits;

    @ManyToOne
    @JsonIgnoreProperties("properties")
    private MiscellaneousConstraint miscellaneousConstraint;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "property_composites",
               joinColumns = @JoinColumn(name = "property_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "composites_id", referencedColumnName = "id"))
    private Set<Composite> composites = new HashSet<>();

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

    public Property name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WeightingFactor getWeightingFactors() {
        return weightingFactors;
    }

    public Property weightingFactors(WeightingFactor weightingFactor) {
        this.weightingFactors = weightingFactor;
        return this;
    }

    public void setWeightingFactors(WeightingFactor weightingFactor) {
        this.weightingFactors = weightingFactor;
    }

    public Limit getLimits() {
        return limits;
    }

    public Property limits(Limit limit) {
        this.limits = limit;
        return this;
    }

    public void setLimits(Limit limit) {
        this.limits = limit;
    }

    public MiscellaneousConstraint getMiscellaneousConstraint() {
        return miscellaneousConstraint;
    }

    public Property miscellaneousConstraint(MiscellaneousConstraint miscellaneousConstraint) {
        this.miscellaneousConstraint = miscellaneousConstraint;
        return this;
    }

    public void setMiscellaneousConstraint(MiscellaneousConstraint miscellaneousConstraint) {
        this.miscellaneousConstraint = miscellaneousConstraint;
    }

    public Set<Composite> getComposites() {
        return composites;
    }

    public Property composites(Set<Composite> composites) {
        this.composites = composites;
        return this;
    }

    public Property addComposites(Composite composite) {
        this.composites.add(composite);
        composite.getProperties().add(this);
        return this;
    }

    public Property removeComposites(Composite composite) {
        this.composites.remove(composite);
        composite.getProperties().remove(this);
        return this;
    }

    public void setComposites(Set<Composite> composites) {
        this.composites = composites;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Property)) {
            return false;
        }
        return id != null && id.equals(((Property) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Property{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
