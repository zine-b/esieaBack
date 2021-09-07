package esiea.api;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONObject;

import esiea.dao.VoitureDAO;
import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;
import utils.StringUtils;

@Path("/voiture")
public class VoitureAPI {
	 
	@Path("get/{param}")
	@GET
	@Produces("application/json")
	public String getVoituresJson(@PathParam("param") String param) {
		Voiture[] voitures;
		JSONObject ret = new JSONObject();
		JSONArray liste = new JSONArray();
		if ("all".equals(param)) { 
			voitures = getToutesVoitures();
			for (Voiture v : voitures) {
				liste.put(v);
			}
			ret.put("voitures", liste);
		} else  if (StringUtils.estEntier(param)){
			ret.put("voiture", getVoiture(param));
		}
		else {
			liste.put(getVoiture(param));
			ret.put("voitures", liste);
		}
		return ret.toString();
	}
	
	
	
	/**
	 * Utilise le DAO pour insérer une voiture en base de données
	 * @return Retourne true si l'insertion s'est déroulée avec succès
	 */
	@Path("add")
	@POST
	@Produces("application/json")
	public String ajouterVoiture(String saisieJson) {
		JSONObject json = new JSONObject(saisieJson);
		boolean succes = false;
		try {
			new VoitureDAO().ajouterVoiture(voitureFromJson(json));
			succes = true;
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		JSONObject ret = new JSONObject();
		ret.put("succes", succes);
		return ret.toString();
	}
	
	/**
	 * Utilise le DAO pour supprimer une voiture en base de données
	 * @return Retourne true si l'insertion s'est déroulée avec succès
	 */
	@Path("del")
	@POST
	@Produces("application/json")
	public String supprimerVoiture(String id) {
		boolean succes = false;
		try {
			new VoitureDAO().supprimerVoiture(id);
			succes = true;
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		JSONObject ret = new JSONObject();
		ret.put("succes", succes);
		return ret.toString();
	}
	
	/**
	 * Récupère toutes les voitures en base
	 * @return Retourne un ensemble de voitures sous forme de tableau de Voitures
	 */
	public Voiture[] getToutesVoitures() {
		Voiture[] ret = new Voiture[0];
		try {
			ret = new VoitureDAO().getVoitures(null);
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Utilise le DAO pour obtenir les informations d'une voiture à partir de son ID 
	 * @param id L'ID de la voiture à récupérer en base
	 * @return Retourne une voiture sous forme d'objet voiture
	 */
	public Voiture getVoiture(String param) {
		Voiture ret = null;
		try {
			ret = new VoitureDAO().getVoiture(param);
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		return ret;
	}
	
	public Voiture voitureFromJson(JSONObject json) {
		Voiture voiture = new Voiture();
		if(json.has("id")) {
			voiture.setId(json.getInt("id"));
		} if(json.has("marque")) {
			voiture.setMarque(json.getString("marque"));
		} if(json.has("modele")) {
			voiture.setMarque(json.getString("modele"));
		} if(json.has("finition")) {
			voiture.setFinition(json.getString("finition"));
		} if(json.has("carburant")) {
			voiture.setCarburant(Carburant.get(json.getString("carburant")));
		} if(json.has("km")) {
			voiture.setKm(json.getInt("km"));
		} if(json.has("annee")) {
			voiture.setAnnee(json.getInt("annee"));
		} if(json.has("prix")) {
			voiture.setPrix(json.getInt("prix"));
		}
		return voiture;
	}
}
