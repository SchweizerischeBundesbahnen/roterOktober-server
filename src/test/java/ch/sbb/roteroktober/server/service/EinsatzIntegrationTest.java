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
        String uid = createMitarbeiter();
        String projektId = createProjekt();

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
        when().get("/einsatz/" + publicId).then().statusCode(200).body("publicId", is(publicId));

        // Einsatz löschen
        when().delete("/einsatz/" + publicId).then().statusCode(200);

        // Jetzt sollte nichts mehr da sein
        when().get("/mitarbeiter/" + uid + "/einsatz").then().body("size()", is(0));
        when().get("/einsatz/" + publicId).then().statusCode(404);
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

}
