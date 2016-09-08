package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Repräsentiert einen Mitarbeiter
 */
@Entity
@Table(name = "mitarbeiter")
public class MitarbeiterEntity extends DeletableEntity {

    @Column(name = "namen")
    private String name;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "uid")
    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
