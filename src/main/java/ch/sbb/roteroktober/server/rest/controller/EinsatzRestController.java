package ch.sbb.roteroktober.server.rest.controller;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.repo.EinsatzRepository;
import ch.sbb.roteroktober.server.rest.exceptions.NotFoundException;
import ch.sbb.roteroktober.server.rest.mapper.EinsatzMapper;
import ch.sbb.roteroktober.server.rest.model.EinsatzResource;
import ch.sbb.roteroktober.server.service.EinsatzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-Schnittstelle für die Einsätze
 */
@RestController
public class EinsatzRestController {

    @Autowired
    private EinsatzRepository einsatzRepository;

    @Autowired
    private EinsatzMapper einsatzMapper;

    @Autowired
    private EinsatzService einsatzService;

    @RequestMapping(path = "/mitarbeiter/{uid}/einsatz", method = RequestMethod.GET)
    public List<EinsatzResource> findAllByMitarbeiter(@PathVariable("uid") String uid) {
        // Einsätze suchen
        List<EinsatzEntity> einsaetze = einsatzRepository.findByUID(uid);

        // Ressourcen erstellen
        return einsaetze.stream().map(einsatzMapper::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/einsatz/{einsatzId}")
    public EinsatzResource findById(@PathVariable("einsatzId") String einsatzId) {
        // Einsatz suchen
        EinsatzEntity einsatzEntity = einsatzRepository.findByPublicId(einsatzId);

        // Wenn wir nicht gefunden haben, geben wir einen Fehler zurück
        if (einsatzEntity == null) {
            throw new NotFoundException("Kein Einsatz mit der ID " + einsatzId + " gefunden");
        } else {
            return einsatzMapper.fromEntity(einsatzEntity);
        }
    }

    @RequestMapping(path = "/mitarbeiter/{uid}/einsatz", method = RequestMethod.POST)
    public EinsatzResource createResourceForMitarbeiter(@PathVariable("uid") String uid, @Validated @RequestBody EinsatzResource resource) {
        // Entität erstellen
        EinsatzEntity newEinsatz = einsatzMapper.toEntity(resource);

        // Speichern
        EinsatzEntity savedEinsatz = einsatzService.createEinsatz(newEinsatz, uid, resource.getProjektId());

        // Wieder eine Ressource erstellen und zurückgeben
        return einsatzMapper.fromEntity(savedEinsatz);
    }

    @RequestMapping(path = "/einsatz/{einsatzId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("einsatzId") String einsatzId) {
        einsatzRepository.setDeleteFlag(einsatzId);
    }
}
