package com.personal.backzone.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.personal.backzone.domain.ZonePest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ZonePestDTO implements Serializable {

    private Long id;

    private Instant createdat;

    private Instant updatedat;

    private ZoneDTO zoneId;

    private PestDTO pestId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public ZoneDTO getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneDTO zoneId) {
        this.zoneId = zoneId;
    }

    public PestDTO getPestId() {
        return pestId;
    }

    public void setPestId(PestDTO pestId) {
        this.pestId = pestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZonePestDTO)) {
            return false;
        }

        ZonePestDTO zonePestDTO = (ZonePestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, zonePestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZonePestDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", zoneId=" + getZoneId() +
            ", pestId=" + getPestId() +
            "}";
    }
}
