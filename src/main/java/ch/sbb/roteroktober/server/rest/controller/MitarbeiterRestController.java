package ch.sbb.roteroktober.server.rest.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.repo.EinsatzRepository;
import ch.sbb.roteroktober.server.repo.MitarbeiterRepository;
import ch.sbb.roteroktober.server.repo.MitarbeiterRepositoryCustom;
import ch.sbb.roteroktober.server.repo.PensumRepository;
import ch.sbb.roteroktober.server.rest.exceptions.DuplicateEntryException;
import ch.sbb.roteroktober.server.rest.exceptions.NotFoundException;
import ch.sbb.roteroktober.server.rest.mapper.MitarbeiterMapper;
import ch.sbb.roteroktober.server.rest.model.MitarbeiterResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Schnittstelle für den Mitarbeiter.
 */
@RestController
@RequestMapping(path = "/mitarbeiter")
public class MitarbeiterRestController {

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    private MitarbeiterRepositoryCustom mitarbeiterRepositoryCustom;

    @Autowired
    private MitarbeiterMapper mitarbeiterMapper;

    @Autowired
    private EinsatzRepository einsatzRepository;

    @Autowired
    private PensumRepository pensumRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<MitarbeiterResource> getAll() {
        // Alle Mitarbeiter laden
        List<MitarbeiterEntity> entities = mitarbeiterRepository.findAll();

        // Ressourcen erstellen
        return entities.stream().map(mitarbeiterMapper::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{uid}", method = RequestMethod.GET)
    public MitarbeiterResource getByUid(@PathVariable("uid") String uid) {
        // Mitarbeiter suchen
        MitarbeiterEntity mitarbeiter = mitarbeiterRepository.findByUID(uid);

        // Wenn wir nichts gefunden haben, geben wir einen Fehler zurück
        if (mitarbeiter == null) {
            throw new NotFoundException("Kein Mitarbeiter mit der UID " + uid + " gefunden");
        } else {
            return mitarbeiterMapper.fromEntity(mitarbeiter);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public MitarbeiterResource create(@Validated @RequestBody MitarbeiterResource newMitarbeiter) {
        // Prüfen ob der Mitarbeiter schon in der DB existiert
        MitarbeiterEntity mitarbeiter = mitarbeiterRepository.findByUID(newMitarbeiter.getUid());
        if (mitarbeiter != null) {
            throw new DuplicateEntryException("Der Mitarbeiter mit der UID " + newMitarbeiter.getUid() + " existiert bereits in der Datenbank");
        }

        // Mappen
        MitarbeiterEntity entity = mitarbeiterMapper.toEntity(newMitarbeiter);

        // Speichern
        MitarbeiterEntity savedEntity = mitarbeiterRepository.save(entity);

        // Zurückmappen und zurückgeben
        return mitarbeiterMapper.fromEntity(savedEntity);
    }

    @RequestMapping(path = "/{uid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uid") String uid) {
        // markiere alle Einsätze des Mitarbeiters zum Löschen
        final List<EinsatzEntity> einsatzEntities = einsatzRepository.findByUID(uid);
        for (EinsatzEntity einsatzEntity : einsatzEntities) {
            final String einsatzPublicId = einsatzEntity.getPublicId();
            einsatzRepository.setDeleteFlag(einsatzPublicId);

            // markiere alle Pensen des Mitarbeiters zum Löschen, sofern Einsätze vorhanden sind
            final List<PensumEntity> pensumEntities = pensumRepository.findByMitarbeiterAndEinsatz(uid, einsatzPublicId);
            for (PensumEntity pensumEntity : pensumEntities) {
                pensumRepository.setDeleteFlag(pensumEntity.getPublicId());
            }
        }

        // zuletzt markiere den Mitarbeiter zum Löschen
        mitarbeiterRepository.setDeleteFlag(uid);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public List<MitarbeiterResource> search(@RequestParam(required = false) String oeName, @RequestParam(required = false) String projektId) {
        // Suche ausführen
        Iterable<MitarbeiterEntity> searchResult = mitarbeiterRepositoryCustom.search(oeName, projektId);

        // Mappen
        return StreamSupport.stream(searchResult.spliterator(), false).map(mitarbeiterMapper::fromEntity).collect(Collectors.toList());
    }
}
