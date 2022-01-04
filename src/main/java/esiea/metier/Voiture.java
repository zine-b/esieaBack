package esiea.metier;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Voiture {
	private int id;
	
	private String marque;
	private String modele;
	private String finition;
	
	private Carburant carburant;
	private int km;
	private int annee;
	private int prix;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getModele() {
		return modele;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}

	public String getFinition() {
		return finition;
	}

	public void setFinition(String finition) {
		this.finition = finition;
	}

	public Carburant getCarburant() {
		return carburant;
	}

	public void setCarburant(Carburant carburant) {
		this.carburant = carburant;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}
	
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException json) {
			json.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Vérifie les champs d'une voiture pour déterminer si la voiture est cohérente, pour être insérée en base
	 * @return Retourne true si les données sont cohérentes
	 */
	public boolean check() {
		if (id < 0) { return false; }
		if (marque == null || marque.isEmpty()) { return false; }
		if (modele == null || modele.isEmpty()) { return false; }
		if (finition == null || finition.isEmpty()) { return false; }
		if (carburant == null) { return false; }
		if (km < 0) { return false; }
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if (annee < 1900 || annee >	calendar.get(Calendar.YEAR)) { return false; }
		if (prix < 0) { return false; }
		return true;
	}

	public static String getTypeDonnee(String donnee) {
		String[] strings = {"marque", "modele", "finition" };
		String[] entiers = {"id", "annee", "km", "prix"};
		
		if(donnee != null) {
			for (String attribut : strings) {
				if(attribut.equals(donnee)) {
					return "string";
				}
			} 
			for (String attribut : entiers) {
				if(attribut.equals(donnee)) {
					return "entier";
				}
			}
		}
		return "";
	}
	
	public enum Carburant {
		ESSENCE("E"),
		DIESEL("D"),
		HYBRIDE("H"),
		ELECTRIQUE("W");
		
		private String label;
		
		Carburant (String carburant) {
			this.label = carburant;
		}
		
		public String toString() {
			return label;
		}
		
		public char getChar() {
			return label.charAt(0);
		}
		
		public static Carburant get(String valeur) {
			for (Carburant c : values()) { 
				if(c.label.equals(valeur)) {
					return c;
				}
			}
			return null;
		}
	}
}
