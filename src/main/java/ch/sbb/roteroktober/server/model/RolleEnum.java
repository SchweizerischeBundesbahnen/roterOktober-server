package ch.sbb.roteroktober.server.model;

/**
 * Repräsentiert die Rolle eines Mitarbeiters
 */
public enum RolleEnum {
    ApplicationEngineer("ae"),
    SoftwareArchitect("sa"),
    TestManager("tm"),
    ApplicationOperationManager("aom");

    private final String dbValue;

    RolleEnum(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static RolleEnum fromDbValue(String dbValue) {
        for (RolleEnum rolle : values()) {
            if (rolle.getDbValue().equalsIgnoreCase(dbValue)) {
                return rolle;
            }
        }

        throw new IllegalArgumentException("Keine Rolle für den DB-Wert '" + dbValue + "' gefunden");
    }
}
