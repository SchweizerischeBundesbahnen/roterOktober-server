package ch.sbb.roteroktober.server.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.repo.PensumRepository;
import ch.sbb.roteroktober.server.rest.exceptions.BadRequestException;
import ch.sbb.roteroktober.server.rest.exceptions.NotFoundException;
import ch.sbb.roteroktober.server.rest.mapper.PensumMapper;
import ch.sbb.roteroktober.server.rest.model.PensumResource;
import ch.sbb.roteroktober.server.service.PensumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Schnittestelle für die Pensen.
 */
@RestController
public class PensumRestController {

    @Autowired
    private PensumRepository pensumRepository;

    @Autowired
    private PensumMapper pensumMapper;

    @Autowired
    private PensumService pensumService;

    @RequestMapping(path = "/mitarbeiter/{uid}/einsatz/{einsatzId}/pensum", method = RequestMethod.GET)
    public List<PensumResource> findAllByMitarbeiterAndEinsatz(@PathVariable("uid") String uid, @PathVariable("einsatzId") String einsatzId) {
        // Einsätze suchen
        List<PensumEntity> pensen = pensumRepository.findByMitarbeiterAndEinsatz(uid, einsatzId);

        // Ressourcen erstellen
        return pensen.stream().map(pensumMapper::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/pensum/{pensumId}", method = RequestMethod.GET)
    public PensumResource findById(@PathVariable("pensumId") String pensumId) {
        // Pensum suchen
        PensumEntity pensum = pensumRepository.findByPublicId(pensumId);

        // Wenn wir nicht gefunden haben, geben wir einen Fehler zurück
        if (pensum == null) {
            throw new NotFoundException("Kein Pensum mit der ID '" + pensumId + "' gefunden");
        } else {
            return pensumMapper.fromEntity(pensum);
        }
    }

    @RequestMapping(path = "/mitarbeiter/{uid}/einsatz/{einsatzId}/pensum", method = RequestMethod.POST)
    public PensumResource createNew(@PathVariable("uid") String uid, @PathVariable("einsatzId") String einsatzId, @Validated @RequestBody PensumResource resource) {
        // Entity erstellen
        PensumEntity newPensum = pensumMapper.toEntity(resource);

        // Pensum speichern
        PensumEntity savedPensum = pensumService.createPensum(newPensum, einsatzId);

        // Wieder eine Ressource erstellen und zurückgeben
        return pensumMapper.fromEntity(savedPensum);
    }

    @RequestMapping(path = "/pensum/{pensumId}", method = RequestMethod.PUT)
    public PensumResource update(@PathVariable("pensumId") String pensumId, @Validated @RequestBody PensumResource resource) {
        // Pensum mit dem gegebenen Schlüssel laden
        PensumEntity existingPensum = pensumRepository.findByPublicId(pensumId);
        if (existingPensum == null) {
            throw new BadRequestException("Kein Pensum mit der ID " + pensumId + "vorhanden. Speichern nicht möglich");
        }

        // Werte mappen
        resource.setPublicId(pensumId);
        existingPensum = pensumMapper.toEntity(resource, existingPensum);

        // Wieder speichern
        PensumEntity savedPensum = pensumRepository.save(existingPensum);

        return pensumMapper.fromEntity(savedPensum);
    }

    @RequestMapping(path = "/pensum/{pensumId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("pensumId") String pensumId) {
        pensumRepository.setDeleteFlag(pensumId);
    }
}
