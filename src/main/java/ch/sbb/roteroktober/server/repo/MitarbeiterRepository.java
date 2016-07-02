package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Datenbank-Zugriff auf den Mitarbeiter
 */
public interface MitarbeiterRepository extends CrudRepository<MitarbeiterEntity, Long> {

    @Query("SELECT m FROM MitarbeiterEntity m WHERE m.uid = :uid")
    MitarbeiterEntity findByUID(@Param("uid") String uid);
}
