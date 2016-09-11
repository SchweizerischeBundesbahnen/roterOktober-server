package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.model.ProjektEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Datenbank-Zugriff auf die Einsätze
 */
public interface EinsatzRepository extends CrudRepository<EinsatzEntity, Long> {

    @Query("SELECT e FROM EinsatzEntity e WHERE e.deleted = false")
    List<EinsatzEntity> findAll();

    @Query("SELECT e FROM EinsatzEntity e WHERE e.publicId = :publicId AND e.deleted = false")
    EinsatzEntity findByPublicId(String publicId);

    @Query("SELECT e FROM EinsatzEntity e JOIN e.mitarbeiter m WHERE m.uid = :uid AND e.deleted = false")
    List<EinsatzEntity> findByUID(@Param("uid") String uid);
}
