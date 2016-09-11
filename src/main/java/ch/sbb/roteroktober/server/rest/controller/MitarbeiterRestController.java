package ch.sbb.roteroktober.server.rest.controller;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.repo.MitarbeiterRepository;
import ch.sbb.roteroktober.server.rest.exceptions.NotFoundException;
import ch.sbb.roteroktober.server.rest.mapper.MitarbeiterMapper;
import ch.sbb.roteroktober.server.rest.model.MitarbeiterResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST-Schnittstelle für den Mitarbeiter
 */
@RestController
@RequestMapping(path = "/mitarbeiter")
public class MitarbeiterRestController {

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    private MitarbeiterMapper mitarbeiterMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<MitarbeiterResource> getAll(){
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
            throw new NotFoundException("Kein Benutzer mit der UID " + uid + " gefunden");
        } else {
            return mitarbeiterMapper.fromEntity(mitarbeiter);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public MitarbeiterResource create(@Validated @RequestBody MitarbeiterResource newMitarbeiter) {
        // Mappen
        MitarbeiterEntity entity = mitarbeiterMapper.toEntity(newMitarbeiter);

        // Speichern
        MitarbeiterEntity savedEntity = mitarbeiterRepository.save(entity);

        // Zurückmappen und zurückgeben
        return mitarbeiterMapper.fromEntity(savedEntity);
    }

//    @Transactional
    @RequestMapping(path = "/{uid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uid") String uid){
        mitarbeiterRepository.setDeleteFlag(uid);
    }
}
