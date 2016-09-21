package ch.sbb.roteroktober.server.rest.mapper;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.rest.controller.EinsatzRestController;
import ch.sbb.roteroktober.server.rest.controller.MitarbeiterRestController;
import ch.sbb.roteroktober.server.rest.controller.PensumRestController;
import ch.sbb.roteroktober.server.rest.controller.ProjektRestController;
import ch.sbb.roteroktober.server.rest.model.EinsatzResource;
import ch.sbb.roteroktober.server.rest.model.PensumResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource
 */
@Component
public class EinsatzMapper {

    @Autowired
    private ProjektRestController projektRestController;

    @Autowired
    private PensumMapper pensumMapper;

    public EinsatzResource fromEntity(EinsatzEntity entity) {
        EinsatzResource result = new EinsatzResource();
        result.setPublicId(entity.getPublicId());
        result.setRolle(entity.getRolle());
        result.setSenioritaet(entity.getSenioritaet());

        // Pensen auslesen
        List<PensumResource> pensen = entity.getPensen().stream().map(pensumMapper::fromEntity).collect(Collectors.toList());
        result.embed("pensen", pensen);

        result.add(linkTo(methodOn(EinsatzRestController.class).findById(entity.getPublicId())).withSelfRel());
        result.add(linkTo(methodOn(ProjektRestController.class).getByPublicId(entity.getProjekt().getPublicId())).withRel("projekt"));
        result.add(linkTo(methodOn(MitarbeiterRestController.class).getByUid(entity.getMitarbeiter().getUid())).withRel("mitarbeiter"));
        result.add(linkTo(methodOn(PensumRestController.class).findAllByMitarbeiterAndEinsatz(entity.getMitarbeiter().getUid(), entity.getPublicId())).withRel("pensen"));

        return result;
    }

    public EinsatzEntity toEntity(EinsatzResource source, EinsatzEntity target) {
        target.setPublicId(source.getPublicId());
        target.setSenioritaet(source.getSenioritaet());
        target.setRolle(source.getRolle());
        return target;
    }

    public EinsatzEntity toEntity(EinsatzResource resource) {
        EinsatzEntity result = new EinsatzEntity();
        return toEntity(resource, result);
    }
}
