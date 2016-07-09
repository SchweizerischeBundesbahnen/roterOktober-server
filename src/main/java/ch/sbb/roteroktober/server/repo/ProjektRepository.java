package ch.sbb.roteroktober.server.repo;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Datenbank-Zugriff auf die Projekte
 */
public interface ProjektRepository extends CrudRepository<ProjektEntity, Long> {
    ProjektEntity findByPublicId(String publicId);
}
