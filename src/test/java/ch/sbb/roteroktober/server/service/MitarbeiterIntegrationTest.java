package ch.sbb.roteroktober.server.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

/**
 * Integrationtest des Mitarbeiters über die REST-Schnittstelle
 */
public class MitarbeiterIntegrationTest extends IntegrationTestBase {

    @Test
    public void testCreateDelete() throws Exception {
        // Daten laden. Es sollte noch alles leer sein
        when().get("/mitarbeiter").then().body("size()", is(0)).statusCode(200);
        when().get("/mitarbeiter/u123456").then().statusCode(404);

        // Mitarbeiter erfassen
        given().
                body("{\"name\":\"Muster\",\"vorname\":\"Hans\",\"uid\":\"u123456\",\"oeName\":\"IT-SWE-CD3-JV6\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200).
                body("name", is("Muster"));

        // Mitarbeiter sollte jetzt vorhanden sein
        when().get("/mitarbeiter").then().body("size()", is(1)).statusCode(200);
        when().
                get("/mitarbeiter/u123456").
                then().
                statusCode(200).
                body("name", is("Muster")).
                body("vorname", is("Hans")).
                body("uid", is("u123456")).
                body("oeName", is("IT-SWE-CD3-JV6")).
                body("_links.self.href", endsWith("/mitarbeiter/u123456"));

        // Mitarbeiter wieder löschen
        when().delete("/mitarbeiter/u123456").thenReturn();

        // Jetzt sollte wieder alles leer sein
        when().get("/mitarbeiter").then().body("size()", is(0)).statusCode(200);
        when().get("/mitarbeiter/u123456").then().statusCode(404);
    }

    @Test
    public void testSearch() throws Exception {
        // Einige Mitarbeite erfassen
        given().
                body("{\"name\":\"Muster\",\"vorname\":\"Hans\",\"uid\":\"u123456\",\"oeName\":\"IT-SWE-CD3-JV6\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200);
        given().
                body("{\"name\":\"Müller\",\"vorname\":\"Peter\",\"uid\":\"u999888\",\"oeName\":\"IT-SWE-CD3-JV3\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200);
        given().
                body("{\"name\":\"Meier\",\"vorname\":\"Andreas\",\"uid\":\"u111222\",\"oeName\":\"IT-SWE-TF-TF1\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(200);

        // Suche nach OE
        given().
                when().
                get("/mitarbeiter/search?oeName=IT-SWE-CD3-JV6").
                then().
                statusCode(200).
                body("size()", is(1)).
                body("[0].name", is("Muster"));

        given().
                when().
                get("/mitarbeiter/search?oeName=IT-AQ").
                then().
                statusCode(200).
                body("size()", is(0));

        given().
                when().
                get("/mitarbeiter/search?oeName=IT-SWE-CD3*").
                then().
                statusCode(200).
                body("size()", is(2));
    }
}
