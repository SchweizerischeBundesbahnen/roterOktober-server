package ch.sbb.roteroktober.server.rest.mapper;

import ch.sbb.roteroktober.server.model.PensumEntity;
import ch.sbb.roteroktober.server.rest.controller.EinsatzRestController;
import ch.sbb.roteroktober.server.rest.controller.PensumRestController;
import ch.sbb.roteroktober.server.rest.model.PensumResource;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource
 */
@Component
public class PensumMapper {
    public PensumResource fromEntity(PensumEntity entity) {
        PensumResource result = new PensumResource();
        result.setPublicId(entity.getPublicId());
        result.setPensum(entity.getPensum());
        result.setAnfang(entity.getAnfang());
        result.setEnde(entity.getEnde());

        result.add(linkTo(methodOn(PensumRestController.class).findById(entity.getPublicId())).withSelfRel());
        result.add(linkTo(methodOn(EinsatzRestController.class).findById(entity.getEinsatz().getPublicId())).withRel("einsatz"));

        return result;
    }

    public PensumEntity toEntity(PensumResource resource) {
        PensumEntity result = new PensumEntity();
        result.setPublicId(resource.getPublicId());
        result.setPensum(resource.getPensum());
        result.setAnfang(resource.getAnfang());
        result.setEnde(resource.getEnde());

        return result;
    }
}
