package ch.sbb.roteroktober.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Repräsentiert die Rolle eines Mitarbeiters
 */
public enum RolleEnum {
    ApplicationEngineer("ae"),
    SoftwareArchitect("sa"),
    TestManager("tm"),
    ApplicationOperationManager("aom"),
    BusinessAnalyst("ba");

    private final String dbValue;

    RolleEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    @JsonCreator
    public static RolleEnum fromDbValue(String dbValue) {
        for (RolleEnum rolle : values()) {
            if (rolle.getDbValue().equalsIgnoreCase(dbValue)) {
                return rolle;
            }
        }

        throw new IllegalArgumentException("Keine Rolle für den DB-Wert '" + dbValue + "' gefunden");
    }
}
