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
 * A Calculation.
 */
@Entity
@Table(name = "calculation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Calculation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @OneToMany(mappedBy = "calculation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CalculationResult> results = new HashSet<>();

    @OneToMany(mappedBy = "calculation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WeightingFactor> weightingFactors = new HashSet<>();

    @OneToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Noun> nouns = new HashSet<>();

    @OneToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Verb> verbs = new HashSet<>();

    @ManyToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Limit> limits = new HashSet<>();

    @ManyToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<EnvironmentalEffect> environmentalEffects = new HashSet<>();

    @ManyToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<MiscellaneousConstraint> miscConstraints = new HashSet<>();

    @ManyToMany(mappedBy = "calculations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Load> loads = new HashSet<>();

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

    public Calculation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Calculation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CalculationResult> getResults() {
        return results;
    }

    public Calculation results(Set<CalculationResult> calculationResults) {
        this.results = calculationResults;
        return this;
    }

    public Calculation addResults(CalculationResult calculationResult) {
        this.results.add(calculationResult);
        calculationResult.setCalculation(this);
        return this;
    }

    public Calculation removeResults(CalculationResult calculationResult) {
        this.results.remove(calculationResult);
        calculationResult.setCalculation(null);
        return this;
    }

    public void setResults(Set<CalculationResult> calculationResults) {
        this.results = calculationResults;
    }

    public Set<WeightingFactor> getWeightingFactors() {
        return weightingFactors;
    }

    public Calculation weightingFactors(Set<WeightingFactor> weightingFactors) {
        this.weightingFactors = weightingFactors;
        return this;
    }

    public Calculation addWeightingFactors(WeightingFactor weightingFactor) {
        this.weightingFactors.add(weightingFactor);
        weightingFactor.setCalculation(this);
        return this;
    }

    public Calculation removeWeightingFactors(WeightingFactor weightingFactor) {
        this.weightingFactors.remove(weightingFactor);
        weightingFactor.setCalculation(null);
        return this;
    }

    public void setWeightingFactors(Set<WeightingFactor> weightingFactors) {
        this.weightingFactors = weightingFactors;
    }

    public Set<Noun> getNouns() {
        return nouns;
    }

    public Calculation nouns(Set<Noun> nouns) {
        this.nouns = nouns;
        return this;
    }

    public Calculation addNoun(Noun noun) {
        this.nouns.add(noun);
        noun.setCalculations(this);
        return this;
    }

    public Calculation removeNoun(Noun noun) {
        this.nouns.remove(noun);
        noun.setCalculations(null);
        return this;
    }

    public void setNouns(Set<Noun> nouns) {
        this.nouns = nouns;
    }

    public Set<Verb> getVerbs() {
        return verbs;
    }

    public Calculation verbs(Set<Verb> verbs) {
        this.verbs = verbs;
        return this;
    }

    public Calculation addVerb(Verb verb) {
        this.verbs.add(verb);
        verb.setCalculations(this);
        return this;
    }

    public Calculation removeVerb(Verb verb) {
        this.verbs.remove(verb);
        verb.setCalculations(null);
        return this;
    }

    public void setVerbs(Set<Verb> verbs) {
        this.verbs = verbs;
    }

    public Set<Limit> getLimits() {
        return limits;
    }

    public Calculation limits(Set<Limit> limits) {
        this.limits = limits;
        return this;
    }

    public Calculation addLimits(Limit limit) {
        this.limits.add(limit);
        limit.getCalculations().add(this);
        return this;
    }

    public Calculation removeLimits(Limit limit) {
        this.limits.remove(limit);
        limit.getCalculations().remove(this);
        return this;
    }

    public void setLimits(Set<Limit> limits) {
        this.limits = limits;
    }

    public Set<EnvironmentalEffect> getEnvironmentalEffects() {
        return environmentalEffects;
    }

    public Calculation environmentalEffects(Set<EnvironmentalEffect> environmentalEffects) {
        this.environmentalEffects = environmentalEffects;
        return this;
    }

    public Calculation addEnvironmentalEffects(EnvironmentalEffect environmentalEffect) {
        this.environmentalEffects.add(environmentalEffect);
        environmentalEffect.getCalculations().add(this);
        return this;
    }

    public Calculation removeEnvironmentalEffects(EnvironmentalEffect environmentalEffect) {
        this.environmentalEffects.remove(environmentalEffect);
        environmentalEffect.getCalculations().remove(this);
        return this;
    }

    public void setEnvironmentalEffects(Set<EnvironmentalEffect> environmentalEffects) {
        this.environmentalEffects = environmentalEffects;
    }

    public Set<MiscellaneousConstraint> getMiscConstraints() {
        return miscConstraints;
    }

    public Calculation miscConstraints(Set<MiscellaneousConstraint> miscellaneousConstraints) {
        this.miscConstraints = miscellaneousConstraints;
        return this;
    }

    public Calculation addMiscConstraints(MiscellaneousConstraint miscellaneousConstraint) {
        this.miscConstraints.add(miscellaneousConstraint);
        miscellaneousConstraint.getCalculations().add(this);
        return this;
    }

    public Calculation removeMiscConstraints(MiscellaneousConstraint miscellaneousConstraint) {
        this.miscConstraints.remove(miscellaneousConstraint);
        miscellaneousConstraint.getCalculations().remove(this);
        return this;
    }

    public void setMiscConstraints(Set<MiscellaneousConstraint> miscellaneousConstraints) {
        this.miscConstraints = miscellaneousConstraints;
    }

    public Set<Load> getLoads() {
        return loads;
    }

    public Calculation loads(Set<Load> loads) {
        this.loads = loads;
        return this;
    }

    public Calculation addLoads(Load load) {
        this.loads.add(load);
        load.getCalculations().add(this);
        return this;
    }

    public Calculation removeLoads(Load load) {
        this.loads.remove(load);
        load.getCalculations().remove(this);
        return this;
    }

    public void setLoads(Set<Load> loads) {
        this.loads = loads;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calculation)) {
            return false;
        }
        return id != null && id.equals(((Calculation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Calculation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
