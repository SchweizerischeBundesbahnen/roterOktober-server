package ch.sbb.roteroktober.server.service;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

/**
 * Integrationtest der Einsätze über die REST-Schnittstelle
 */
public class PensumIntegrationTest extends IntegrationTestBase {

    @Test
    public void testCreateDelete() throws Exception {
        // Mitarbeiter, Projekt und Einsatz anlegen
        String uid = TestDatenGenerator.createMitarbeiter("Muster", "Hans", "u123456", "IT-SWE");
        String projektId = TestDatenGenerator.createProjekt("SVS Webshop", "IT-SCP-MVD-VKA");
        String einsatzId = TestDatenGenerator.createEinsatz("ae", "prof", uid, projektId);

        // Es sollte noch kein Pensum geben
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(0));

        // Pensum anlegen
        String pensumId = given().
                body("{\"pensum\":80,\"anfang\":\"2016-02-01T00:00:00.000Z\"}").
                contentType(ContentType.JSON).
                post("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").
                then().
                statusCode(200).
                body("pensum", is(80)).
                extract().path("publicId");

        // Jetzt sollte das Pensum vorhanden sein
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(1));
        when().get("/pensum/" + pensumId).then().statusCode(200).body("publicId", is(pensumId));

        // Pensum wieder löschen
        when().delete("/pensum/" + pensumId).then().statusCode(200);

        // Jetzt sollte wieder alles weg sein
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(0));
        when().get("/pensum/" + pensumId).then().statusCode(404);
    }
}
