package ch.sbb.roteroktober.server.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Beschreibt die REST-Ressource für die Auslastung
 */
public class AuslastungResource {
    @Min(0)
    @Max(100)
    private int pensum;

    @NotNull
    @JsonFormat(pattern = ResourceConstants.ISO_8601_DATE_PATTERN)
    private Date anfang;

    @JsonFormat(pattern = ResourceConstants.ISO_8601_DATE_PATTERN)
    private Date ende;

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
