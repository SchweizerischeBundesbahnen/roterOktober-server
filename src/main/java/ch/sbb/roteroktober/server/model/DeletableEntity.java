package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import java.util.Date;

/**
 * Basisklasse für eine Enität, welche gelöscht werden kann.
 */
public class DeletableEntity extends CreateTimeTrackEntity {
    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Date deletedAt;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
