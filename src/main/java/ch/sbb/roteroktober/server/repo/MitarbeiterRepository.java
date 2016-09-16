package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Datenbank-Zugriff auf den Mitarbeiter
 */
public interface MitarbeiterRepository extends CrudRepository<MitarbeiterEntity, Long> {

    @Query("SELECT m FROM MitarbeiterEntity m WHERE m.uid = :uid AND m.deleted = false")
    MitarbeiterEntity findByUID(@Param("uid") String uid);

    @Query("SELECT m FROM MitarbeiterEntity m WHERE m.deleted = false")
    List<MitarbeiterEntity> findAll();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE MitarbeiterEntity m SET m.deleted = true, m.deletedAt = CURRENT_TIMESTAMP WHERE m.uid = :uid")
    void setDeleteFlag(@Param("uid") String uid);
}
