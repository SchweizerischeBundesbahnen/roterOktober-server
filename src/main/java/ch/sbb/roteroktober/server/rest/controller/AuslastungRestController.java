package ch.sbb.roteroktober.server.rest.controller;

import ch.sbb.roteroktober.server.rest.model.AuslastungResource;
import ch.sbb.roteroktober.server.service.AuslastungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-Schnittstelle für die Auslastung
 */
@RestController
@RequestMapping(path = "/mitarbeiter/{uid}/auslastung")
public class AuslastungRestController {
    @Autowired
    private AuslastungService auslastungService;

    @RequestMapping(method = RequestMethod.GET)
    public List<AuslastungResource> getAuslastung(@PathVariable("uid") String uid) {
        return auslastungService.berechneAuslastung(uid);
    }
}
