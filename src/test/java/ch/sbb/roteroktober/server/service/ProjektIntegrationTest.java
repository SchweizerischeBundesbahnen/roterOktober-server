package ch.sbb.roteroktober.server.service;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import io.restassured.http.ContentType;
import org.junit.Test;

/**
 * Integrationtest der Projekte über die REST-Schnittstelle.
 */
public class ProjektIntegrationTest extends IntegrationTestBase {
    @Test
    public void testCreateDelete() throws Exception {
        // Daten laden. Noch sollte alles leer sein
        when().get("/projekt").then().body("size()", is(0)).statusCode(200);

        // Projekt erfassen
        String publicId = given().body("{\"name\":\"SVS Webshop\",\"oeName\":\"IT-SCP-MVD-VKA\"}").contentType(ContentType.JSON).when().post("/projekt").then().statusCode(200).body("name", is("SVS Webshop")).extract().path("publicId");

        // Jetzt sollten wir das Projekt finden
        when().get("/projekt").then().body("size()", is(1)).statusCode(200);
        when().get("/projekt/" + publicId).then().body("publicId", is(publicId)).body("name", is("SVS Webshop"));

        // Projekt löschen
        when().delete("/projekt/" + publicId).thenReturn();

        // Jetzt sollte wieder alles leer sein
        when().get("/projekt").then().body("size()", is(0)).statusCode(200);
    }
}
