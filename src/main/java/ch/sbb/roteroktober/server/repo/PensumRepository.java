package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.PensumEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Datenbank-Zugriffe auf die Pensen
 */
public interface PensumRepository extends CrudRepository<PensumEntity, Long> {
    PensumEntity findByPublicId(String publicId);

    @Query("SELECT p FROM PensumEntity p JOIN p.einsatz e JOIN e.mitarbeiter m WHERE m.uid = :uid AND e.publicId = :einsatzId")
    List<PensumEntity> findByMitarbeiterAndEinsatz(@Param("uid") String uid, @Param("einsatzId") String einsatzId);
}