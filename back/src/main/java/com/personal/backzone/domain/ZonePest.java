package com.personal.backzone.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A ZonePest.
 */
@Entity
@Table(name = "zone_pest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ZonePest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "updatedat")
    private Instant updatedat;

    @ManyToOne
    private Zone zoneId;

    @ManyToOne
    private Pest pestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ZonePest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ZonePest createdat(Instant createdat) {
        this.setCreatedat(createdat);
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ZonePest updatedat(Instant updatedat) {
        this.setUpdatedat(updatedat);
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Zone getZoneId() {
        return this.zoneId;
    }

    public void setZoneId(Zone zone) {
        this.zoneId = zone;
    }

    public ZonePest zoneId(Zone zone) {
        this.setZoneId(zone);
        return this;
    }

    public Pest getPestId() {
        return this.pestId;
    }

    public void setPestId(Pest pest) {
        this.pestId = pest;
    }

    public ZonePest pestId(Pest pest) {
        this.setPestId(pest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZonePest)) {
            return false;
        }
        return id != null && id.equals(((ZonePest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZonePest{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            "}";
    }
}
