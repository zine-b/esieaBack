package esiea.api;

import esiea.metier.Voiture;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VoitureAPITest {
    private VoitureAPI voitureAPI = new VoitureAPI();

    @BeforeEach
    public void setUp() {
        voitureAPI = new VoitureAPI();
    }

    @Test
    void getVoituresJson() {
    }

    @Test
    void testGetVoituresJson() {
    }

    @Test
    void ajouterVoiture() {

        // Given
        JSONObject json = new JSONObject();
        json.put("id", 1);
        json.put("marque", "Peugeot");
        json.put("modele", "3008");
        json.put("finition", "Allure");
        json.put("carburant", "D");
        json.put("km", 100);
        json.put("annee", 2017);
        json.put("prix", 15500);
    }

    @Test
    public void should_convert_json_to_voiture() {
        // Given
        JSONObject json = new JSONObject();
        json.put("id", 1);
        json.put("marque", "Peugeot");
        json.put("modele", "3008");
        json.put("finition", "Allure");
        json.put("carburant", "D");
        json.put("km", 100);
        json.put("annee", 2017);
        json.put("prix", 15500);

        // When
        Voiture voiture = voitureAPI.voitureFromJson(json);

        // Then
        assertNotNull(voiture);
        assertEquals(1, voiture.getId());
        assertEquals("Peugeot", voiture.getMarque());
        assertEquals("3008", voiture.getModele());
        assertEquals("Allure", voiture.getFinition());
        assertEquals(Voiture.Carburant.DIESEL, voiture.getCarburant());
        assertEquals(100, voiture.getKm());
        assertEquals(2017, voiture.getAnnee());
        assertEquals(15500, voiture.getPrix());
    }
}