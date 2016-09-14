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
        String uid = createMitarbeiter();
        String projektId = createProjekt();
        String einsatzId = createEinsatz(uid, projektId);

        // Es sollte noch kein Pensum geben
        when().get("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").then().body("size()", is(0));

        // Einsatz anlegen
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

    private String createMitarbeiter(){
        given().
                body("{\"name\":\"Muster\",\"vorname\":\"Hans\",\"uid\":\"u123456\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200).
                body("name", is("Muster"));
        return "u123456";
    }

    private String createProjekt(){
        String publicId = given().
                body("{\"name\":\"SVS Webshop\",\"oeName\":\"IT-SCP-MVD-VKA\"}").
                contentType(ContentType.JSON).
                when().
                post("/projekt").
                then().
                statusCode(200).extract().path("publicId");
        return publicId;
    }

    private String createEinsatz(String uid, String projektId){
        String publicId = given().
                body("{\"rolle\":\"ae\",\"senioritaet\":\"prof\",\"projektId\":\"" + projektId + "\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter/" + uid + "/einsatz").
                then().statusCode(200).body("rolle", is("ae")).body("senioritaet", is("prof")).extract().path("publicId");
        return publicId;
    }
}
