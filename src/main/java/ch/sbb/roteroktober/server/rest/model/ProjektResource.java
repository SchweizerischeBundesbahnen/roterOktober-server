package ch.sbb.roteroktober.server.rest.model;

import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Beschreibt die REST-Ressource eines Projektes
 */
public class ProjektResource extends ResourceSupport {

    /** Öffentlicher Schlüssel dieses Projektes */
    private String publicId;

    private String name;

    /** Name der Organisationseinheit */
    private String oeName;

    private Date createdAt;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
