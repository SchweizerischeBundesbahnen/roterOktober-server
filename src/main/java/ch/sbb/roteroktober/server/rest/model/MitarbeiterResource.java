package ch.sbb.roteroktober.server.rest.model;

import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Size;

/**
 * Beschreibt die REST-Ressource für den Mitarbeiter
 */
public class MitarbeiterResource extends ResourceSupport {
    @Size(min = 1, max = 100)
    private String name;

    @Size(min = 1, max = 100)
    private String vorname;

    @Size(min = 7, max = 15)
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
