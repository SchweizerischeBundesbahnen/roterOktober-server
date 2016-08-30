package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Datenbank-Zugriff auf die Projekte
 */
public interface ProjektRepository extends CrudRepository<ProjektEntity, Long> {
    ProjektEntity findByPublicId(String publicId);

    @Query("SELECT p FROM ProjektEntity p WHERE upper(p.name) like upper(:name)")
    List<ProjektEntity> searchByName(@Param("name") String name);
}
