package ch.sbb.roteroktober.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Repräsentiert das Pensum eines Mitarbeiters auf einem Einsatz.
 */
@Entity
@Table(name = "pensum")
public class PensumEntity extends PublicIdBaseEntity {

    @ManyToOne
    @JoinColumn(name = "einsatz_fk", nullable = false)
    private EinsatzEntity einsatz;

    /** Pensum als ganzzahliger Prozentwert zwischen 0 und 100 */
    @Column(name = "pensum", nullable = false)
    @Min(0)
    @Max(100)
    private int pensum;

    @Column(name = "anfang", nullable = false)
    private Date anfang;

    @Column(name = "ende", nullable = true)
    private Date ende;

    public PensumEntity() {
    }

    public PensumEntity(int pensum, Date anfang, Date ende) {
        this.pensum = pensum;
        this.anfang = anfang;
        this.ende = ende;
    }

    public EinsatzEntity getEinsatz() {
        return einsatz;
    }

    public void setEinsatz(EinsatzEntity einsatz) {
        this.einsatz = einsatz;
    }

    public int getPensum() {
        return pensum;
    }

    public void setPensum(int pensum) {
        this.pensum = pensum;
    }

    public Date getAnfang() {
        return anfang;
    }

    public void setAnfang(Date anfang) {
        this.anfang = anfang;
    }

    public Date getEnde() {
        return ende;
    }

    public void setEnde(Date ende) {
        this.ende = ende;
    }
}
