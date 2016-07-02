package ch.sbb.roteroktober.server.rest.mapper;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.rest.controller.MitarbeiterRestController;
import ch.sbb.roteroktober.server.rest.model.MitarbeiterResource;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource
 */
@Component
public class MitarbeiterMapper {
    public MitarbeiterResource fromEntity(MitarbeiterEntity entity) {
        MitarbeiterResource result = new MitarbeiterResource();
        result.setName(entity.getName());
        result.setVorname(entity.getVorname());
        result.setUid(entity.getUid());

        result.add(linkTo(methodOn(MitarbeiterRestController.class).getByUid(entity.getUid())).withSelfRel());

        return result;
    }

    public MitarbeiterEntity toEntity(MitarbeiterResource resource) {
        MitarbeiterEntity result = new MitarbeiterEntity();
        result.setName(resource.getName());
        result.setVorname(resource.getVorname());
        result.setUid(resource.getUid());

        return result;
    }
}
