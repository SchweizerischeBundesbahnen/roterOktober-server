package ch.sbb.roteroktober.server.rest.model;

import ch.sbb.roteroktober.server.model.RolleEnum;
import ch.sbb.roteroktober.server.model.SenioritaetEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

/**
 * Beschreibt die REST-Ressource für einen Einsatz eines Mitarbeiter in einem Projekt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EinsatzResource extends ResourceSupport {

    /** Öffentlicher Schlüssel für diesen Einsatz*/
    private String publicId;

    private RolleEnum rolle;

    private SenioritaetEnum senioritaet;

    /** ID des Projektes, für welches der Einsatz geleistet wird */
    private String projektId;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public RolleEnum getRolle() {
        return rolle;
    }

    public void setRolle(RolleEnum rolle) {
        this.rolle = rolle;
    }

    public SenioritaetEnum getSenioritaet() {
        return senioritaet;
    }

    public void setSenioritaet(SenioritaetEnum senioritaet) {
        this.senioritaet = senioritaet;
    }

    public String getProjektId() {
        return projektId;
    }

    public void setProjektId(String projektId) {
        this.projektId = projektId;
    }
}
