package ch.sbb.roteroktober.server.rest.mapper;

import ch.sbb.roteroktober.server.model.ProjektEntity;
import ch.sbb.roteroktober.server.rest.controller.ProjektRestController;
import ch.sbb.roteroktober.server.rest.model.ProjektResource;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource
 */
@Component
public class ProjektMapper {

    public ProjektResource fromEntity(ProjektEntity entity) {
        ProjektResource result = new ProjektResource();
        result.setPublicId(entity.getPublicId());
        result.setName(entity.getName());
        result.setOeName(entity.getOeName());
        result.setCreatedAt(entity.getCreatedAt());

        result.add(linkTo(methodOn(ProjektRestController.class).getByPublicId(entity.getPublicId())).withSelfRel());

        return result;
    }

    public ProjektEntity toEntity(ProjektResource resource) {
        ProjektEntity result = new ProjektEntity();
        result.setName(resource.getName());
        result.setOeName(resource.getOeName());
        result.setPublicId(resource.getPublicId());

        return result;
    }
}
