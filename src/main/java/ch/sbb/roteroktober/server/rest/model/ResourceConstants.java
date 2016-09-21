package ch.sbb.roteroktober.server.rest.model;

/**
 * Wichtige Werte, welche in den REST-Ressourcen verwendet werden.
 */
public final class ResourceConstants {

    /**
     * Formatiert ein Datum gemäss ISO 8601
     *
     * @see <href a="https://en.wikipedia.org/wiki/ISO_8601">https://en.wikipedia.org/wiki/ISO_8601</href>
     * <href a="http://stackoverflow.com/questions/10286204/the-right-json-date-format">@link http://stackoverflow.com/questions/10286204/the-right-json-date-format</href>
     */
    public final static String ISO_8601_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Diese Klasse enthält nur statische Konstanten und darf nicht initialisiert werden.
     */
    private ResourceConstants() {
    }
}
