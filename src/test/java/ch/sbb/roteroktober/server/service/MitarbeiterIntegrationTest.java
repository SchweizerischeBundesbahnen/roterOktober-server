package ch.sbb.roteroktober.server.service;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integrationtest des Mitarbeiters über die REST-Schnittstelle
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2,test")
public class MitarbeiterIntegrationTest {

    @LocalServerPort
    private String port;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void testCRUD() throws Exception {
        // Daten laden
        when().get("/mitarbeiter").then().body("size()", is(0)).statusCode(200);
    }
}
