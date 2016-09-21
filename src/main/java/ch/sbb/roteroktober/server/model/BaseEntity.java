package ch.sbb.roteroktober.server.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Basisklasse für alle Entitäten.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * Technischer Primärschlüssel. Wird von JPA vergeben
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

    /**
     * @return Technischer Primärschlüssel
     */
    public Long getId() {
        return id;
    }
}
