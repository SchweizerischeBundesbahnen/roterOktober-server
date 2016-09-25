package ch.sbb.roteroktober.server.service;

import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.repo.PensumRepository;
import ch.sbb.roteroktober.server.rest.model.AuslastungResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Berechnet die Auslastung eines Mitarbeiters
 */
@Component
public class AuslastungService {

    @Autowired
    private PensumRepository pensumRepository;

    public List<AuslastungResource> berechneAuslastung(String uid) {
        // Alle Pensen für diesen Benutzer laden
        List<PensumEntity> pensen = pensumRepository.findByMitarbeiter(uid);
        return berechneAuslastung(pensen);
    }

    public List<AuslastungResource> berechneAuslastung(List<PensumEntity> pensumEntities) {
        return null;
    }
}
