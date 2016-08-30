package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Repräsentiert ein Projekt
 */
@Entity
@Table(name = "projekt")
public class ProjektEntity extends PublicIdBaseEntity {
    @Column(name = "namen")
    private String name;

    /** Name der Organisationseinheit */
    @Column(name = "oe_name")
    private String oeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOeName() {
        return oeName;
    }

    public void setOeName(String oeName) {
        this.oeName = oeName;
    }
}
