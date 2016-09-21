package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Repräsentiert einen öffentlichen Schlüssel.
 */
@Entity
@Table(name = "publicids")
public class PublicIdEntity extends CreateTimeTrackEntity {

    @Column(name = "public_id", nullable = false, unique = true)
    public String publicId;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
