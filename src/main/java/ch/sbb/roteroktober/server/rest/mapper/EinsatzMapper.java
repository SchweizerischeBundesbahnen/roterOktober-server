package ch.sbb.roteroktober.server.rest.mapper;

import ch.sbb.roteroktober.server.model.EinsatzEntity;
import ch.sbb.roteroktober.server.rest.controller.EinsatzRestController;
import ch.sbb.roteroktober.server.rest.controller.MitarbeiterRestController;
import ch.sbb.roteroktober.server.rest.controller.ProjektRestController;
import ch.sbb.roteroktober.server.rest.model.EinsatzResource;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource
 */
@Component
public class EinsatzMapper {

    public EinsatzResource fromEntity(EinsatzEntity entity) {
        EinsatzResource result = new EinsatzResource();
        result.setPublicId(entity.getPublicId());
        result.setRolle(entity.getRolle());
        result.setSenioritaet(entity.getSenioritaet());

        result.add(linkTo(methodOn(EinsatzRestController.class).findById(entity.getPublicId())).withSelfRel());
        result.add(linkTo(methodOn(ProjektRestController.class).getByPublicId(entity.getProjekt().getPublicId())).withRel("projekt"));
        result.add(linkTo(methodOn(MitarbeiterRestController.class).getByUid(entity.getMitarbeiter().getUid())).withRel("mitarbeiter"));

        return result;
    }

    public EinsatzEntity toEntity(EinsatzResource resource) {
        EinsatzEntity result = new EinsatzEntity();
        result.setPublicId(resource.getPublicId());
        result.setSenioritaet(resource.getSenioritaet());
        result.setRolle(resource.getRolle());
        return result;
    }
}
