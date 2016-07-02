package ch.sbb.roteroktober.server.rest.model;

import org.springframework.hateoas.ResourceSupport;

/**
 * Beschreibt die REST-Ressource für ein Ping
 */
public class PingResource extends ResourceSupport {

    private String message = "Give me a ping, Vasily. One ping only, please.";

    public String getMessage() {
        return message;
    }
}
