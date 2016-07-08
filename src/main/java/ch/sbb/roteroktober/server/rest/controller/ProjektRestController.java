package ch.sbb.roteroktober.server.rest.controller;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import ch.sbb.roteroktober.server.repo.ProjektRepository;
import ch.sbb.roteroktober.server.rest.exceptions.NotFoundException;
import ch.sbb.roteroktober.server.rest.mapper.ProjektMapper;
import ch.sbb.roteroktober.server.rest.model.ProjektResource;
import ch.sbb.roteroktober.server.service.ProjektService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST-Schnittstelle für die Projekte
 */
@RestController
@RequestMapping(path = "/projekt")
public class ProjektRestController {

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private ProjektMapper projektMapper;

    @Autowired
    private ProjektService projektService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ProjektResource> getAll() {
        // Alle Projekte laden
        Iterable<ProjektEntity> projekte = projektRepository.findAll();

        // Ressourcen erstellen
        return StreamSupport.stream(projekte.spliterator(), false).map(projektMapper::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{publicId}")
    public ProjektResource getByPublicId(@PathVariable("publicId") String publicId) {
        // Projekt suchen
        ProjektEntity projekt = projektRepository.findByPublicId(publicId);

        // Wenn wir nichts gefunden haben geben wir einen Fehler zurück
        if (projekt == null) {
            throw new NotFoundException("Kein Projekt mit der ID " + publicId + " gefunden");
        } else {
            return projektMapper.fromEntity(projekt);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ProjektResource create(@RequestBody ProjektResource resource) {
        // Entität erstellen
        ProjektEntity entity = projektMapper.toEntity(resource);

        // Speichern
        ProjektEntity savedEntity = projektService.createProjekt(entity);

        // Ressource erstellen und zurückgeben
        return projektMapper.fromEntity(savedEntity);
    }
}
