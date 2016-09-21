package ch.sbb.roteroktober.server.repo;

import java.util.List;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Datenbank-Zugriff auf die Einsätze.
 */
public interface EinsatzRepository extends CrudRepository<EinsatzEntity, Long> {

    @Query("SELECT e FROM EinsatzEntity e WHERE e.deleted = false")
    List<EinsatzEntity> findAll();

    @Query("SELECT e FROM EinsatzEntity e WHERE e.publicId = :publicId AND e.deleted = false")
    EinsatzEntity findByPublicId(@Param("publicId") String publicId);

    @Query("SELECT e FROM EinsatzEntity e JOIN e.mitarbeiter m WHERE m.uid = :uid AND e.deleted = false")
    List<EinsatzEntity> findByUID(@Param("uid") String uid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE EinsatzEntity e SET e.deleted = true, e.deletedAt = CURRENT_TIMESTAMP WHERE e.publicId = :publicId")
    void setDeleteFlag(@Param("publicId") String publicId);
}
