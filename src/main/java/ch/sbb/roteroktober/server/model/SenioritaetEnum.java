package ch.sbb.roteroktober.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Repräsentiert die Seniorität eines Mitarbeiters.
 */
public enum SenioritaetEnum {

    Junior("junior"),
    Professional("prof"),
    Senior("senior"),;

    private final String dbValue;

    SenioritaetEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

     @JsonCreator
    public static SenioritaetEnum fromDbValue(String dbValue) {
        for (SenioritaetEnum senioritaet : values()) {
            if (senioritaet.getDbValue().equalsIgnoreCase(dbValue)) {
                return senioritaet;
            }
        }

        throw new IllegalArgumentException("Keine Seniorität für den DB-Wert '" + dbValue + "' gefunden");
    }
}
