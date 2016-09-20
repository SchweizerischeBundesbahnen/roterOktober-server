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

        // Und wir fügen nochmals ein Pensum dazu
        String pensum2Id = given().
                body("{\"pensum\":60,\"anfang\":\"2015-02-01T00:00:00.000Z\", \"ende\":\"2015-06-01T00:00:00.000Z\"}").
                contentType(ContentType.JSON).
                post("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").
                then().
                statusCode(200).
                body("pensum", is(60)).
                body("ende", is("2015-06-01T00:00:00.000Z")).
                extract().path("publicId");

        // Jetzt sollten zwei Pensen da sein
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(2));
        when().get("/pensum/" + pensum2Id).then().statusCode(200).body("publicId", is(pensum2Id));

        // Pensum aktualisieren
        given().
                body("{\"pensum\":70,\"anfang\":\"2015-02-01T00:00:00.000Z\", \"ende\":\"2015-08-12T00:00:00.000Z\"}").
                contentType(ContentType.JSON).
                put("/pensum/" + pensum2Id).
                then().
                statusCode(200).
                body("pensum", is(70)).
                body("ende", is("2015-08-12T00:00:00.000Z"));

        // Pensum nochmals suchen
        when().get("/pensum/" + pensum2Id)
                .then()
                .statusCode(200)
                .body("publicId", is(pensum2Id))
                .body("pensum", is(70))
                .body("ende", is("2015-08-12T00:00:00.000Z"));

        // Pensum wieder löschen
        when().delete("/pensum/" + pensumId).then().statusCode(200);
        when().delete("/pensum/" + pensum2Id).then().statusCode(200);

        // Jetzt sollte wieder alles weg sein
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(0));
        when().get("/pensum/" + pensumId).then().statusCode(404);
    }
}
