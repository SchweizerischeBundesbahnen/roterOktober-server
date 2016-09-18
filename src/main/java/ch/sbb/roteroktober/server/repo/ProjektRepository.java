package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Datenbank-Zugriff auf die Projekte
 */
public interface ProjektRepository extends CrudRepository<ProjektEntity, Long> {

    @Query("SELECT p FROM ProjektEntity p WHERE p.deleted = false")
    List<ProjektEntity> findAll();

    @Query("SELECT p FROM ProjektEntity p WHERE p.publicId = :publicId AND p.deleted = false")
    ProjektEntity findByPublicId(@Param("publicId") String publicId);

    @Query("SELECT p FROM ProjektEntity p WHERE upper(p.name) like upper(:name) AND p.deleted = false")
    List<ProjektEntity> searchByName(@Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProjektEntity p SET p.deleted = true, p.deletedAt = CURRENT_TIMESTAMP WHERE p.publicId = :publicId")
    void setDeleteFlag(@Param("publicId") String publicId);
}
