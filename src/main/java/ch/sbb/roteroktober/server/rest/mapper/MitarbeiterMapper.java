package ch.sbb.roteroktober.server.rest.mapper;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import ch.sbb.roteroktober.server.model.MitarbeiterEntity;
import ch.sbb.roteroktober.server.rest.controller.AuslastungRestController;
import ch.sbb.roteroktober.server.rest.controller.EinsatzRestController;
import ch.sbb.roteroktober.server.rest.controller.MitarbeiterRestController;
import ch.sbb.roteroktober.server.rest.model.MitarbeiterResource;
import org.springframework.stereotype.Component;

/**
 * Mapper zwischen der JPA-Entität und der REST-Ressource.
 */
@Component
public class MitarbeiterMapper {
    public MitarbeiterResource fromEntity(MitarbeiterEntity entity) {
        MitarbeiterResource result = new MitarbeiterResource();
        result.setName(entity.getName());
        result.setVorname(entity.getVorname());
        result.setUid(entity.getUid());
        result.setOeName(entity.getOeName());

        result.add(linkTo(methodOn(MitarbeiterRestController.class).getByUid(entity.getUid())).withSelfRel());
        result.add(linkTo(methodOn(EinsatzRestController.class).findAllByMitarbeiter(entity.getUid())).withRel("einsaetze"));
        result.add(linkTo(methodOn(AuslastungRestController.class).getAuslastung(entity.getUid())).withRel("auslastung"));

        return result;
    }

    public MitarbeiterEntity toEntity(MitarbeiterResource resource) {
        MitarbeiterEntity result = new MitarbeiterEntity();
        result.setName(resource.getName());
        result.setVorname(resource.getVorname());
        result.setUid(resource.getUid());
        result.setOeName(resource.getOeName());

        return result;
    }
}
