package esiea.metier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VoitureTest {

    @Test
    void should_check_ok() {
        Voiture voiture = new Voiture();
        voiture.setId(1);
        voiture.setMarque("Peugeot");
        voiture.setModele("3008");
        voiture.setFinition("Allure");
        voiture.setCarburant(Voiture.Carburant.DIESEL);
        voiture.setKm(100);
        voiture.setAnnee(2017);
        voiture.setPrix(15500);
        assertTrue(voiture.check());
    }

    @Test
    void should_check_ko_for_incorrect_price() {
        Voiture voiture = new Voiture();
        voiture.setId(1);
        voiture.setMarque("Peugeot");
        voiture.setModele("3008");
        voiture.setFinition("Allure");
        voiture.setCarburant(Voiture.Carburant.DIESEL);
        voiture.setKm(100);
        voiture.setAnnee(2017);
        voiture.setPrix(-15500);
        assertFalse(voiture.check());
    }

    @ParameterizedTest
    @ValueSource(strings = {"marque", "modele", "finition"})
    void should_return_string(String donnee) {
        assertEquals("string", Voiture.getTypeDonnee(donnee));
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "annee", "km", "prix"})
    void should_return_entier(String donnee) {
        assertEquals("entier", Voiture.getTypeDonnee(donnee));
    }

    @Test
    void should_return_empty_for_null() {
        assertEquals("", Voiture.getTypeDonnee(null));
    }

    @Test
    void should_return_empty_for_others() {
        assertEquals("", Voiture.getTypeDonnee("other"));
    }
}