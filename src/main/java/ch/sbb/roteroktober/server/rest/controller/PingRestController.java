package ch.sbb.roteroktober.server.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Schnittstelle für ein einfaches Ping
 */
@RestController
@RequestMapping(path = "/ping")
public class PingRestController {

    @RequestMapping(method = RequestMethod.GET)
    public String ping(){
        return "Give me a ping, Vasily. One ping only, please.";
    }
}
