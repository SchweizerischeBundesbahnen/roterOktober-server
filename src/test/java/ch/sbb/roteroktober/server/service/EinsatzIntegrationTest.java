package ch.sbb.roteroktober.server.service;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

/**
 * Integrationtest der Einsätze über die REST-Schnittstelle
 */
public class EinsatzIntegrationTest extends IntegrationTestBase {

    @Test
    public void testCreateDelete() throws Exception {
        // Für die Einsätze brauchen wir einen Mitarbeiter und ein Projekt.
        String uid = TestDatenGenerator.createMitarbeiter("Muster", "Hans", "u123456", "IT-SWE");
        String projektId = TestDatenGenerator.createProjekt("SVS Webshop", "IT-SCP-MVD-VKA");

        // Noch sollte es keinen Einsatz geben
        when().get("/mitarbeiter/" + uid + "/einsatz").then().body("size()", is(0));

        // Einsatz anlegen
        String publicId = given().
                body("{\"rolle\":\"ae\",\"senioritaet\":\"prof\",\"projektId\":\"" + projektId + "\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter/" + uid + "/einsatz").
                then().statusCode(200).body("rolle", is("ae")).body("senioritaet", is("prof")).extract().path("publicId");

        // Nach dem Einsatz suchen
        when().get("/mitarbeiter/" + uid + "/einsatz").then().body("size()", is(1));
        when().get("/einsatz/" + publicId).then().statusCode(200).body("publicId", is(publicId)).body("rolle", is("ae"));

        // Einsatz aktualisieren
        given().
                body("{\"rolle\":\"sa\",\"senioritaet\":\"prof\"}").
                contentType(ContentType.JSON).
                when().
                put("/einsatz/" + publicId).
                then().
                statusCode(200).
                body("rolle", is("sa")).
                body("senioritaet", is("prof"));

        // Den aktualisierten Einsatz laden
        when().get("/einsatz/" + publicId).
                then().
                statusCode(200).
                body("publicId", is(publicId)).
                body("rolle", is("sa")).
                body("senioritaet", is("prof"));

        // Einsatz löschen
        when().delete("/einsatz/" + publicId).then().statusCode(200);

        // Jetzt sollte nichts mehr da sein
        when().get("/mitarbeiter/" + uid + "/einsatz").then().body("size()", is(0));
        when().get("/einsatz/" + publicId).then().statusCode(404);
    }
}
