package ch.sbb.roteroktober.server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column
    private String oeName;

    @OneToMany(mappedBy = "mitarbeiter")
    private List<EinsatzEntity> einsaetze;

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

    public String getOeName() {
        return oeName;
    }

    public void setOeName(String oeName) {
        this.oeName = oeName;
    }

    public List<EinsatzEntity> getEinsaetze() {
        if(einsaetze == null){
            einsaetze = new ArrayList<>();
        }
        return einsaetze;
    }
}
