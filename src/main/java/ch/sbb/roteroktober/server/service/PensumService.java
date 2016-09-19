package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.model.PublicIdEntity;
import ch.sbb.roteroktober.server.repo.EinsatzRepository;
import ch.sbb.roteroktober.server.repo.PensumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Verwaltet die Pensen zu einem Einsatz
 */
@Component
public class PensumService {

    public static final String PUBLIC_ID_PREFIX = "PE";
    public static final int PUBLIC_ID_LENGTH = 8;

    @Autowired
    private PublicIdService publicIdService;

    @Autowired
    private PensumRepository pensumRepository;

    @Autowired
    private EinsatzRepository einsatzRepository;

    public PensumEntity createPensum(PensumEntity entity, String einsatzId) {
        // Einsatz suchen und überprüfen
        EinsatzEntity einsatz = einsatzRepository.findByPublicId(einsatzId);
        if (einsatz == null) {
            throw new IllegalArgumentException("Kein Einsatz mit der ID '" + einsatzId + "' gefunden");
        }

        // Öffentliche ID lösen
        PublicIdEntity publicId = publicIdService.createNewPublicId(PUBLIC_ID_PREFIX, PUBLIC_ID_LENGTH);

        // Alles in die Entität packen
        entity.setPublicId(publicId.getPublicId());
        entity.setEinsatz(einsatz);

        // Speichern
        return pensumRepository.save(entity);
    }
}
