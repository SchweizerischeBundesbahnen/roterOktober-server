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
    EinsatzEntity findByPublicId(String publicId);

    @Query("SELECT e FROM EinsatzEntity e JOIN e.mitarbeiter m WHERE m.uid = :uid")
    List<EinsatzEntity> findByUID(@Param("uid") String uid);
}
