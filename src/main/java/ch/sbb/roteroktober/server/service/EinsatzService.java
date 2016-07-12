package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.model.ProjektEntity;
import ch.sbb.roteroktober.server.model.PublicIdEntity;
import ch.sbb.roteroktober.server.repo.EinsatzRepository;
import ch.sbb.roteroktober.server.repo.MitarbeiterRepository;
import ch.sbb.roteroktober.server.repo.ProjektRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Verwaltet die Einsätze der Mitarbeiter in den Projekten
 */
@Component
public class EinsatzService {

    public static final String PUBLIC_ID_PREFIX = "ES";
    public static final int PUBLIC_ID_LENGTH = 8;

    @Autowired
    private PublicIdService publicIdService;

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private EinsatzRepository einsatzRepository;

    public EinsatzEntity createEinsatz(EinsatzEntity entity, String uid, String projektId) {
        // Mitarbeiter suchen und überprüfen
        MitarbeiterEntity mitarbeiter = mitarbeiterRepository.findByUID(uid);
        if (mitarbeiter == null) {
            throw new IllegalArgumentException("Kein Mitarbeiter mit der UID " + uid + " gefunden");
        }

        // Projekt suchen und überprüfen
        ProjektEntity projekt = projektRepository.findByPublicId(projektId);
        if (projekt == null) {
            throw new IllegalArgumentException("Kein Projekt mit der ID " + projektId + " gefunden");
        }

        // Öffentliche ID lösen
        PublicIdEntity publicId = publicIdService.createNewPublicId(PUBLIC_ID_PREFIX, PUBLIC_ID_LENGTH);

        // Alles in die Entität reinpacken
        entity.setPublicId(publicId.getPublicId());
        entity.setProjekt(projekt);
        entity.setMitarbeiter(mitarbeiter);

        // Speichern und gespeicherte Entität zurückgeben
        return einsatzRepository.save(entity);
    }
}
