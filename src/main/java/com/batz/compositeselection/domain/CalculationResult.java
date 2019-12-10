package com.batz.compositeselection.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A CalculationResult.
 */
@Entity
@Table(name = "calculation_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CalculationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rating_factor")
    private Double ratingFactor;

    @OneToOne
    @JoinColumn(unique = true)
    private Composite composite;

    @ManyToOne
    @JsonIgnoreProperties("results")
    private Calculation calculation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRatingFactor() {
        return ratingFactor;
    }

    public CalculationResult ratingFactor(Double ratingFactor) {
        this.ratingFactor = ratingFactor;
        return this;
    }

    public void setRatingFactor(Double ratingFactor) {
        this.ratingFactor = ratingFactor;
    }

    public Composite getComposite() {
        return composite;
    }

    public CalculationResult composite(Composite composite) {
        this.composite = composite;
        return this;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public CalculationResult calculation(Calculation calculation) {
        this.calculation = calculation;
        return this;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalculationResult)) {
            return false;
        }
        return id != null && id.equals(((CalculationResult) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CalculationResult{" +
            "id=" + getId() +
            ", ratingFactor=" + getRatingFactor() +
            "}";
    }
}
