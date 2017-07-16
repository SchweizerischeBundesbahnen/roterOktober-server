package ch.sbb.roteroktober.server.service;

import io.restassured.http.ContentType;
import org.junit.Test;

import static ch.sbb.roteroktober.server.service.TestDatenGenerator.createEinsatz;
import static ch.sbb.roteroktober.server.service.TestDatenGenerator.createMitarbeiter;
import static ch.sbb.roteroktober.server.service.TestDatenGenerator.createPensum;
import static ch.sbb.roteroktober.server.service.TestDatenGenerator.createProjekt;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

/**
 * Integrationtest des Mitarbeiters über die REST-Schnittstelle.
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
    public void testDeleteWithEnsatzUndPensen() {
        // Einige Mitarbeiter erfassen
        createMitarbeiter("Muster", "Hans", "u123456", "IT-SWE-CD3-JV6");
        createMitarbeiter("Müller", "Peter", "u999888", "IT-SWE-CD3-JV3");
        createMitarbeiter("Meier", "Andreas", "u111222", "IT-SWE-TF-TF1");

        // Projekte erstellen
        String roterOktoberId = createProjekt("Roter Oktober", "IT-SWE");
        String webshopId = createProjekt("Webshop", "IT-SCP");

        // Einsätze erstellen
        String einsatzROMuster = createEinsatz("sa", "prof", "u123456", roterOktoberId);
        String einsatzWSMuster = createEinsatz("sa", "prof", "u123456", webshopId);
        String einsatzWSMueller = createEinsatz("ae", "senior", "u999888", webshopId);

        // Pensen hinzufügen
        String pensumROMuster = createPensum("80", "u123456", einsatzROMuster);
        String pensumWSMuster = createPensum("80", "u123456", einsatzWSMuster);
        createPensum("80", "u999888", einsatzWSMueller);

        // Mitarbeiter wieder löschen
        when().delete("/mitarbeiter/u123456").thenReturn();

        // Mitarbeiter hat nun keine Einsätze und Pensen mehr
        when().get("/mitarbeiter").then().body("size()", is(2)).statusCode(200);
        when().get("/mitarbeiter/u123456").then().statusCode(404);
        when().get("/mitarbeiter/u123456/einsatz/").then().body("size()", is(0)).statusCode(200);
        when().get("/mitarbeiter/u123456/einsatz/" + pensumROMuster + "/pensum").then().body("size()", is(0)).statusCode(200);
        when().get("/mitarbeiter/u123456/einsatz/" + pensumWSMuster + "/pensum").then().body("size()", is(0)).statusCode(200);
    }

    @Test
    public void testSearch() throws Exception {
        // Einige Mitarbeiter erfassen
        createMitarbeiter("Muster", "Hans", "u123456", "IT-SWE-CD3-JV6");
        createMitarbeiter("Müller", "Peter", "u999888", "IT-SWE-CD3-JV3");
        createMitarbeiter("Meier", "Andreas", "u111222", "IT-SWE-TF-TF1");

        // Projekte erstellen
        String roterOktoberId = createProjekt("Roter Oktober", "IT-SWE");
        String webshopId = createProjekt("Webshop", "IT-SCP");

        // Einsätze erstellen
        String einsatzROMuster = createEinsatz("sa", "prof", "u123456", roterOktoberId);
        String einsatzWSMuster = createEinsatz("sa", "prof", "u123456", webshopId);
        String einsatzWSMueller = createEinsatz("ae", "senior", "u999888", webshopId);

        // Pensen hinzufügen
        createPensum("80", "u123456", einsatzROMuster);
        createPensum("80", "u123456", einsatzWSMuster);
        createPensum("80", "u999888", einsatzWSMueller);

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

        // Suche nach Projekt
        given().
                when().
                get("/mitarbeiter/search?projektId=" + roterOktoberId).
                then().
                statusCode(200).
                body("size()", is(1)).
                body("[0].name", is("Muster"));

        given().
                when().
                get("/mitarbeiter/search?projektId=" + webshopId).
                then().
                statusCode(200).
                body("size()", is(2));
    }

    @Test
    public void testCreateDuplicateMitarbeiter() {
        // Einige Mitarbeiter erfassen
        createMitarbeiter("Muster", "Hans", "u123456", "IT-SWE-CD3-JV6");

        when().get("/mitarbeiter").then().body("size()", is(1)).statusCode(200);

        // Mitarbeiter nochmals erfassen
        given().
                body("{\"name\":\"Muster\",\"vorname\":\"Hans\",\"uid\":\"u123456\",\"oeName\":\"IT-SWE-CD3-JV6\"}").
                contentType(ContentType.JSON).
                when().
                post("/mitarbeiter").
                then().
                statusCode(409);
    }
}
