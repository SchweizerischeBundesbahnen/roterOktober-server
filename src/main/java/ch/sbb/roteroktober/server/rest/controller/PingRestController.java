package ch.sbb.roteroktober.server.rest.controller;

import ch.sbb.roteroktober.server.rest.model.PingResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * REST-Schnittstelle für ein einfaches Ping
 */
@RestController
@RequestMapping(path = "/ping")
public class PingRestController {

    @RequestMapping(method = RequestMethod.GET)
    public PingResource ping(){
        PingResource result = new PingResource();
        result.add(linkTo(methodOn(PingRestController.class).ping()).withSelfRel());
        return result;
    }
}
