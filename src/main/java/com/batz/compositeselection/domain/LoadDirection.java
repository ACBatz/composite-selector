package com.batz.compositeselection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.batz.compositeselection.domain.enumeration.LoadDirectionEnum;

/**
 * A LoadDirection.
 */
@Entity
@Table(name = "load_direction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LoadDirection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "load_direction_enum", nullable = false, unique = true)
    private LoadDirectionEnum loadDirectionEnum;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "load_direction_load",
               joinColumns = @JoinColumn(name = "load_direction_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "load_id", referencedColumnName = "id"))
    private Set<Load> loads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoadDirectionEnum getLoadDirectionEnum() {
        return loadDirectionEnum;
    }

    public LoadDirection loadDirectionEnum(LoadDirectionEnum loadDirectionEnum) {
        this.loadDirectionEnum = loadDirectionEnum;
        return this;
    }

    public void setLoadDirectionEnum(LoadDirectionEnum loadDirectionEnum) {
        this.loadDirectionEnum = loadDirectionEnum;
    }

    public Set<Load> getLoads() {
        return loads;
    }

    public LoadDirection loads(Set<Load> loads) {
        this.loads = loads;
        return this;
    }

    public LoadDirection addLoad(Load load) {
        this.loads.add(load);
        load.getLoadDirections().add(this);
        return this;
    }

    public LoadDirection removeLoad(Load load) {
        this.loads.remove(load);
        load.getLoadDirections().remove(this);
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
        if (!(o instanceof LoadDirection)) {
            return false;
        }
        return id != null && id.equals(((LoadDirection) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LoadDirection{" +
            "id=" + getId() +
            ", loadDirectionEnum='" + getLoadDirectionEnum() + "'" +
            "}";
    }
}
