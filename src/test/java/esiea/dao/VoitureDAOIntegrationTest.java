package esiea.dao;

import config.TestConfig;
import esiea.metier.Voiture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class VoitureDAOIntegrationTest {
    private Connection connection;
    private VoitureDAO voitureDAO;

    @BeforeEach
    void setup() throws SQLException {
        // Create an H2 database connection
        connection = TestConfig.H2DatabaseConfig.createConnection();

        // Create the VoitureDAO instance with the database connection
        voitureDAO = new VoitureDAO();
        VoitureDAO.connection=connection;

    }

    @AfterEach
    void cleanup() {
        try {
            // Close the database connection
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void should_ajouter_voiture() throws SQLException {
        //Given
        Voiture voiture = new Voiture();
        voiture.setMarque("Peugeot");
        voiture.setModele("3008");
        voiture.setFinition("Allure");
        voiture.setCarburant(Voiture.Carburant.DIESEL);
        voiture.setKm(100);
        voiture.setAnnee(2017);
        voiture.setPrix(15500);

        //When
        voitureDAO.ajouterVoiture(voiture);
        ResultSet resultSet = connection.createStatement().executeQuery("select * from voiture where id=2");

        //Then
        assertTrue(resultSet.next());
        assertEquals(2, resultSet.getInt("ID"));
        assertEquals("3008", resultSet.getString("MODELE"));
        assertEquals(100, resultSet.getInt("KM"));
        assertEquals("D", resultSet.getString("CARBURANT"));
        assertEquals(15500, resultSet.getInt("PRIX"));
        assertEquals(2017, resultSet.getInt("ANNEE"));
        assertEquals("Peugeot", resultSet.getString("MARQUE"));
        assertEquals("Allure", resultSet.getString("FINITION"));

    }



    @Test
    void should_supprimer_voiture() throws SQLException {
        // Given
        int voitureId = 1;
        Voiture voiture = new Voiture();
        voiture.setMarque("Renault");
        voiture.setModele("Clio");
        voiture.setFinition("Zen");
        voiture.setCarburant(Voiture.Carburant.ESSENCE);
        voiture.setKm(5000);
        voiture.setAnnee(2020);
        voiture.setPrix(12000);

        voitureDAO.ajouterVoiture(voiture);

        // When
        voitureDAO.supprimerVoiture(String.valueOf(voitureId));

        // Then
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM voiture WHERE id=" + voitureId);
        assertFalse(resultSet.next());
    }


    @Test
    public void should_construire_requete_masque() {
        // Given
        String saisie = "Peugeot 3008 Essence";

        // When
        String requete = voitureDAO.construireRequeteMasque(saisie);

        // Then
        assertEquals("marque like ?  OR modele like ?  OR finition like ?  OR carburant like ?  " +
                "OR marque like ?  OR modele like ?  OR finition like ?  OR carburant like ?  " +
                "OR marque like ?  OR modele like ?  OR finition like ?  OR carburant like ? ", requete);
    }


    @Test
    void should_get_voitures() throws SQLException {
        // Given

        Voiture voiture1 = new Voiture();
        voiture1.setMarque("Peugeot");
        voiture1.setModele("Clio");
        voiture1.setFinition("Zen");
        voiture1.setCarburant(Voiture.Carburant.ESSENCE);
        voiture1.setKm(5000);
        voiture1.setAnnee(2020);
        voiture1.setPrix(12000);

        Voiture voiture2 = new Voiture();
        voiture2.setMarque("Renault");
        voiture2.setModele("Clio");
        voiture2.setFinition("Zen");
        voiture2.setCarburant(Voiture.Carburant.DIESEL);
        voiture2.setKm(5999);
        voiture2.setAnnee(2022);
        voiture2.setPrix(12001);

        voitureDAO.ajouterVoiture(voiture1);
        voitureDAO.ajouterVoiture(voiture2);


        HashMap<String, String> criteres = new HashMap<>();
        criteres.put("marque", "Peugeot");
        int mini = 0;
        int nbVoitures = 1;

        // When
        ReponseVoiture reponseVoiture = voitureDAO.getVoitures(criteres, mini, nbVoitures);

        // Then
        Voiture[] voitures = reponseVoiture.getData();
        for (Voiture voiture : voitures) {
            assertEquals("Peugeot", voiture.getMarque());
        }
        assertEquals(nbVoitures, reponseVoiture.getVolume());
    }





    //----------!!!!!!!!!!!!!-----------
    //it dose not work because of the original methode modifier_voiture()
    // becuase of this line //stmt.set(4, nouvelle.getCarburant().getChar());
    @Test
    void should_modifier_voiture() throws SQLException {
        // Given
        int voitureId = 1;
        Voiture voiture = new Voiture();
        voiture.setMarque("Renault");
        voiture.setModele("Clio");
        voiture.setFinition("Zen");
        voiture.setCarburant(Voiture.Carburant.ESSENCE);
        voiture.setKm(5000);
        voiture.setAnnee(2020);
        voiture.setPrix(12000);

        // When
        voitureDAO.modifierVoiture(voitureId, voiture);
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM voiture WHERE id=" + voitureId);

        // Then
        assertTrue(resultSet.next());
        assertEquals(voitureId, resultSet.getInt("ID"));
        assertEquals("Clio", resultSet.getString("MODELE"));
        assertEquals(5000, resultSet.getInt("KM"));
        assertEquals("E", resultSet.getString("CARBURANT"));
        assertEquals(12000, resultSet.getInt("PRIX"));
        assertEquals(2020, resultSet.getInt("ANNEE"));
        assertEquals("Renault", resultSet.getString("MARQUE"));
        assertEquals("Zen", resultSet.getString("FINITION"));
    }

}


