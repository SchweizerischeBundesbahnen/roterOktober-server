package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import ch.sbb.roteroktober.server.model.PublicIdEntity;
import ch.sbb.roteroktober.server.repo.ProjektRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Verwaltet die Projekte
 */
@Component
public class ProjektService {

    public static final String PUBLIC_ID_PREFIX = "PJ";
    public static final int PUBLIC_ID_LENGTH = 8;

    @Autowired
    private PublicIdService publicIdService;

    @Autowired
    private ProjektRepository projektRepository;

    /**
     * Speichert ein neues Projekt
     * @param newProjekt Zu erstellendes Projekt
     * @return Ersteltes Projekt inkl. der vergebenen IDs
     */
    public ProjektEntity createProjekt(ProjektEntity newProjekt) {
        // Öffentliche ID lösen
        PublicIdEntity publicId = publicIdService.createNewPublicId(PUBLIC_ID_PREFIX, PUBLIC_ID_LENGTH);
        newProjekt.setPublicId(publicId.getPublicId());

        // Definition speichern
        return projektRepository.save(newProjekt);
    }
}
