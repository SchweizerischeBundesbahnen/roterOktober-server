package ch.sbb.roteroktober.server.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.restassured.http.ContentType;

/**
 * Generiert Testdaten.
 */
public class TestDatenGenerator {

    public static String createProjekt(String name, String oe) {
        String jsonTemplate = "{\"name\":\"%s\",\"oeName\":\"%s\"}";
        String json = String.format(jsonTemplate, name, oe);

        String publicId = given().
                body(json).
                contentType(ContentType.JSON).
                when().
                post("/projekt").
                then().
                statusCode(200).extract().path("publicId");
        return publicId;
    }

    public static String createMitarbeiter(String name, String vorname, String uid, String oe) {
        String jsonTemplate = "{\"name\":\"%s\",\"vorname\":\"%s\",\"uid\":\"%s\",\"oeName\":\"%s\"}";
        String json = String.format(jsonTemplate, name, vorname, uid, oe);

        given().
                body(json).
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200);
        return uid;
    }

    public static String createEinsatz(String rolle, String senioritaet, String uid, String projektId) {
        String jsonTemplate = "{\"rolle\":\"%s\",\"senioritaet\":\"%s\",\"projektId\":\"%s\"}";
        String json = String.format(jsonTemplate, rolle, senioritaet, projektId);

        String publicId = given().
                body(json).
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter/" + uid + "/einsatz").
                then().statusCode(200).body("rolle", is(rolle)).body("senioritaet", is(senioritaet)).extract().path("publicId");
        return publicId;
    }

    public static String createPensum(String pensum, String uid, String einsatzId) {
        String jsonTemplate = "{\"pensum\":%s,\"anfang\":\"2016-02-01T00:00:00.000Z\"}";
        String json = String.format(jsonTemplate, pensum);

        String pensumId = given().
                body(json).
                contentType(ContentType.JSON).
                post("/mitarbeiter/" + uid + "/einsatz/" + einsatzId + "/pensum").
                then().
                statusCode(200).
                body("pensum", is(80)).
                extract().path("publicId");
        return pensumId;
    }
}
