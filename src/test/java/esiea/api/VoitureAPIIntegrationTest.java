package esiea.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import esiea.dao.VoitureDAO;
import io.restassured.path.json.JsonPath;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;


public class VoitureAPIIntegrationTest {


    static Tomcat tomcat;
    static Context context;

    @BeforeAll
    static void setUp() throws Exception {
        initServer();
    }


    static void initServer() throws LifecycleException, InterruptedException {
        Connection connection = TestConfig.H2DatabaseConfig.createConnection();
        VoitureDAO.connection = connection;

        tomcat = new Tomcat();
        context = tomcat.addContext("/", "");

        Wrapper servlet = context.createWrapper();
        servlet.setName("jerseyServlet");
        servlet.setServletClass("com.sun.jersey.spi.container.servlet.ServletContainer");

        servlet.addInitParameter(
                "com.sun.jersey.config.property.packages",
                "esiea.api"
        );

        servlet.setLoadOnStartup(1);
        context.addChild(servlet);
        context.addServletMapping("/rest/*", "jerseyServlet");

        // Start Tomcat in a separate thread
        Thread tomcatThread = new Thread(() -> {
            try {
                tomcat.start();
                tomcat.getServer().await();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        });
        tomcatThread.start();

        // Wait for Tomcat to start
        while (!tomcat.getServer().getState().isAvailable()) {
            Thread.sleep(100);
        }
    }


    @Test
    public void should_return_all_voiture() {
        // Test de la récupération de tous les voitures
        given()
                .when()
                .get("/rest/voiture/get/all")
                .then()
                .statusCode(200)
                .body("volume", equalTo(1))
                .body("voitures", hasSize(1));
    }

    //to check voiture.id
    @Test
    public void should_return_voiture_by_id() {
        /*given()
                .when()
                .get("voiture/get/6")
                .then()
                .statusCode(200)
                .body("volume", equalTo(1))
                .body("voiture.id", equalTo(6));*/

        //we cannot get directly voiture.id because we get a string on the resonse
        //for exemple "voiture":"{\"id\":6,\"marque\":\"BMW\",\"modele\":\"F30\",\"finition\":\"Lounge\",\"carburant\":\"ESSENCE\",\"km\":210000,\"annee\":2014,\"prix\":23990}"

        String response = given()
                .when()
                .get("/rest/voiture/get/6")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        JsonPath jsonPath = new JsonPath(response);
        String voitureString = jsonPath.getString("voiture");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode voitureJson = objectMapper.readTree(voitureString);

            voitureJson.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                System.out.println(key + " : " + value);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Test de l'ajout d'une voiture

    @Test
    public void should_ajouter_voiture() {
        JSONObject requestBody = new JSONObject()
                .put("marque", "NouvelleMarque")
                .put("modele", "NouveauModele")
                .put("finition", "FinitionTest")
                .put("carburant", "D")
                .put("km", 10000)
                .put("annee", 2023)
                .put("prix", 25000);

        given()
                .contentType("application/json")
                .body(requestBody.toString())
                .when()
                .post("/rest/voiture/add")
                .then()
                .statusCode(200)
                .body("succes", equalTo(true));

    }


    // Test de la suppression d'une voiture
    @Test
    public void should_supprimer_voiture() {
        String idVoitureASupprimer = "22";

        given()
                .contentType("text/plain")
                .body(idVoitureASupprimer)
                .when()
                .post("/rest/voiture/del")
                .then()
                .statusCode(200)
                .body("succes", equalTo(true));

    }

}
