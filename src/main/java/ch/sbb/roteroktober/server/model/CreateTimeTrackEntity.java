package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

/**
 * Basisklasse für eine Entität, deren Erstelldatum automatisch gesetzt wird
 */
@MappedSuperclass
public abstract class CreateTimeTrackEntity extends BaseEntity {

    /** Erstell-Datum */
    @Column(name="created_at")
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    void prePersist() {
        // Datum setzen
        createdAt = new Date();
    }
}
