package com.batz.compositeselection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.batz.compositeselection.domain.enumeration.LoadShearEnum;

/**
 * A LoadShear.
 */
@Entity
@Table(name = "load_shear")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LoadShear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "load_shear_enum", nullable = false, unique = true)
    private LoadShearEnum loadShearEnum;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "load_shear_load",
               joinColumns = @JoinColumn(name = "load_shear_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "load_id", referencedColumnName = "id"))
    private Set<Load> loads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoadShearEnum getLoadShearEnum() {
        return loadShearEnum;
    }

    public LoadShear loadShearEnum(LoadShearEnum loadShearEnum) {
        this.loadShearEnum = loadShearEnum;
        return this;
    }

    public void setLoadShearEnum(LoadShearEnum loadShearEnum) {
        this.loadShearEnum = loadShearEnum;
    }

    public Set<Load> getLoads() {
        return loads;
    }

    public LoadShear loads(Set<Load> loads) {
        this.loads = loads;
        return this;
    }

    public LoadShear addLoad(Load load) {
        this.loads.add(load);
        load.getLoadShears().add(this);
        return this;
    }

    public LoadShear removeLoad(Load load) {
        this.loads.remove(load);
        load.getLoadShears().remove(this);
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
        if (!(o instanceof LoadShear)) {
            return false;
        }
        return id != null && id.equals(((LoadShear) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LoadShear{" +
            "id=" + getId() +
            ", loadShearEnum='" + getLoadShearEnum() + "'" +
            "}";
    }
}
