package ch.sbb.roteroktober.server.rest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * Erweitert die HATEOAS-Ressource von Spring um die HAL-spezifischen eingebetteten Objekte.
 *
 * @see <a href="https://tools.ietf.org/html/draft-kelly-json-hal-07#section-4.1.2">HAL-Spezifikation</a>
 */
public abstract class HalResource extends ResourceSupport {

    private final Map<String, Object> embedded = new HashMap<String, Object>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, Object> getEmbeddedResources() {
        return embedded;
    }

    public void embed(String relationship, ResourceSupport resource) {
        embedded.put(relationship, resource);
    }

    public void embed(String relationship, List<? extends ResourceSupport> resource) {
        embedded.put(relationship, resource);
    }
}
