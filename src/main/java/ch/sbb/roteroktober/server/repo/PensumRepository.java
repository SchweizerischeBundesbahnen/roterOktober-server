package ch.sbb.roteroktober.server.repo;

import java.util.List;

import ch.sbb.roteroktober.server.model.PensumEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Datenbank-Zugriffe auf die Pensen.
 */
public interface PensumRepository extends CrudRepository<PensumEntity, Long> {

    @Query("SELECT p FROM PensumEntity p WHERE p.deleted = false")
    List<PensumEntity> findAll();

    @Query("SELECT p FROM PensumEntity p WHERE p.publicId = :publicId AND p.deleted = false")
    PensumEntity findByPublicId(@Param("publicId") String publicId);

    @Query("SELECT p FROM PensumEntity p JOIN p.einsatz e JOIN e.mitarbeiter m WHERE m.uid = :uid AND e.publicId = :einsatzId AND p.deleted = false AND m.deleted = false and e.deleted = false")
    List<PensumEntity> findByMitarbeiterAndEinsatz(@Param("uid") String uid, @Param("einsatzId") String einsatzId);

    @Query("SELECT p FROM PensumEntity p JOIN p.einsatz e JOIN e.mitarbeiter m WHERE m.uid = :uid AND p.deleted = false AND e.deleted = false AND m.deleted = false")
    List<PensumEntity> findByMitarbeiter(@Param("uid") String uid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PensumEntity p SET p.deleted = true, p.deletedAt = CURRENT_TIMESTAMP WHERE p.publicId = :publicId")
    void setDeleteFlag(@Param("publicId") String publicId);
}
