package ch.sbb.roteroktober.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Repräsentiert einen Einsatz eines Mitarbeiters in einem Projekt.
 */
@Entity
@Table(name = "einsatz")
public class EinsatzEntity extends PublicIdBaseEntity {

    @ManyToOne
    @JoinColumn(name = "mitarbeiter_fk")
    private MitarbeiterEntity mitarbeiter;

    @ManyToOne
    @JoinColumn(name = "projekt_fk")
    private ProjektEntity projekt;

    @Column(name = "rolle")
    private String rolle;

    @Column(name = "senioritaet")
    private String senioritaet;

    @OneToMany(mappedBy = "einsatz", fetch = FetchType.EAGER)
    private List<PensumEntity> pensen;

    public MitarbeiterEntity getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(MitarbeiterEntity mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public ProjektEntity getProjekt() {
        return projekt;
    }

    public void setProjekt(ProjektEntity projekt) {
        this.projekt = projekt;
    }

    public RolleEnum getRolle() {
        return rolle == null ? null : RolleEnum.fromDbValue(rolle);
    }

    public void setRolle(RolleEnum rolle) {
        this.rolle = rolle == null ? null : rolle.getDbValue();
    }

    public SenioritaetEnum getSenioritaet() {
        return senioritaet == null ? null : SenioritaetEnum.fromDbValue(senioritaet);
    }

    public void setSenioritaet(SenioritaetEnum senioritaet) {
        this.senioritaet = senioritaet == null ? null : senioritaet.getDbValue();
    }

    public List<PensumEntity> getPensen() {
        if (this.pensen == null) {
            this.pensen = new ArrayList<>();
        }
        return this.pensen;
    }
}
