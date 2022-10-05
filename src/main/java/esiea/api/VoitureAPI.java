package esiea.api;

import java.sql.SQLException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONObject;

import esiea.dao.ReponseVoiture;
import esiea.dao.VoitureDAO;
import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;
import utils.StringUtils;

@Path("/voiture")
public class VoitureAPI {
	
	private VoitureDAO vDao = new VoitureDAO();
	
	@Path("get/{param}")
	@GET
	@Produces("application/json")
	public String getVoituresJson(@PathParam("param") String param) {
		return getVoituresJson(param, "-1", "-1");
	}
	
	@Path("get/{param}/{mini}/{nbVoitures}")
	@GET
	@Produces("application/json")
	public String getVoituresJson(@PathParam("param") String param, 
			@PathParam("mini") String miniS, 
			@PathParam("nbVoitures") String nbVoituresS) {
		int mini = Integer.parseInt(miniS), nbVoitures = Integer.parseInt(nbVoituresS);
		ReponseVoiture reponse;
		JSONObject ret = new JSONObject();
		JSONArray liste = new JSONArray();
		if ("all".equals(param)) { 
			reponse = getToutesVoitures(mini, nbVoitures);
			for (Voiture v : reponse.getData()) {
				liste.put(v);
			}
			ret.put("voitures", liste);
			ret.put("volume", reponse.getVolume());
		} else  if (StringUtils.estEntier(param)){
			reponse = getReponse(param, mini, nbVoitures);
			if (reponse.getData().length > 0) {
				ret.put("voiture", reponse.getData()[0]);
				ret.put("volume", 1);
			}
		}
		else {
			reponse = getReponse(param, mini, nbVoitures);
			for (Voiture v : reponse.getData()) {
				liste.put(v);
			}
			ret.put("voitures", liste);
			ret.put("volume", reponse.getVolume());
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
			Voiture v = voitureFromJson(json);
			if (v.check()) {
				vDao.ajouterVoiture(v);
				succes = true;
			}
			
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
			vDao.supprimerVoiture(id);
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
	public ReponseVoiture getToutesVoitures(int mini, int nbVoitures) {
		ReponseVoiture ret = new ReponseVoiture();
		try {
			ret = vDao.getVoitures(null, mini, nbVoitures);
			/*vDao.getVoitures(null, mini, nbVoitures);
			ret[0] = new Voiture();
			ret[0].setAnnee(2008);
			ret[0].setCarburant(Carburant.DIESEL);
			ret[0].setFinition("Initiale");
			ret[0].setId(1);
			ret[0].setKm(174826);
			ret[0].setMarque("Renault");
			ret[0].setModele("VelSatis");
			ret[0].setPrix(4600);
			
			ret[1] = new Voiture();
			ret[1].setAnnee(2013);
			ret[1].setCarburant(Carburant.DIESEL);
			ret[1].setFinition("Business");
			ret[1].setId(2);
			ret[1].setKm(124987);
			ret[1].setMarque("Renault");
			ret[1].setModele("Scénic");
			ret[1].setPrix(8800);

			ret[2] = new Voiture();
			ret[2].setAnnee(2018);
			ret[2].setCarburant(Carburant.DIESEL);
			ret[2].setFinition("Feel");
			ret[2].setId(3);
			ret[2].setKm(78730);
			ret[2].setMarque("Citroën");
			ret[2].setModele("Spacetourer");
			ret[2].setPrix(29000);
			
			for (int i=3; i< ret.length; i++) {
				ret[i] = ret[i%3];
			}*/
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
	public ReponseVoiture getReponse(String param, int mini, int nbVoitures) {
		ReponseVoiture ret = new ReponseVoiture();
		try {
			ret = vDao.rechercherVoitures(param, mini, nbVoitures);
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
			voiture.setModele(json.getString("modele"));
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
