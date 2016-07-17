package ch.sbb.roteroktober.server.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Beschreibt die REST-Ressource für das Pensum eines Mitabeiters in einem Projekt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PensumResource extends ResourceSupport {

    /** Öffentlicher Schlüssel für diesen Einsatz*/
    private String publicId;

    private int pensum;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date anfang;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ende;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
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
