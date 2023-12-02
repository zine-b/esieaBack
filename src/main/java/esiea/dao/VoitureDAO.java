package esiea.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;
import utils.Configuration;
import utils.StringUtils;

public class VoitureDAO {


	public static Connection connection;
	protected static final Logger logger = Logger.getLogger(VoitureDAO.class);
	
	public VoitureDAO() {
		
	}
	
	private Connection getConnexion() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			if (connection == null) {
				connection = DriverManager.getConnection(getUrlBase(), Configuration.getConfig("bdd.utilisateur"), Configuration.getConfig("bdd.mdp"));
			}
		} catch (SQLException sql) {
			sql.printStackTrace(); 
			logger.debug("Impossible de se connecter � la base !" + sql);} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		logger.debug("Connexion � la base OK !" + connection);
		return connection;
	}
	
	public String getUrlBase() {
		String url = "jdbc:mysql://";
		url += Configuration.getConfig("bdd.serveur");
		url += ":" + Configuration.getConfig("bdd.port");
		url += "/" + Configuration.getConfig("bdd.nom");
		return url;
	}
	
	private void deconnecter() throws SQLException {
		if(connection != null) {
			//connection.close();
		}
	}
	
	public void ajouterVoiture(Voiture voiture) throws SQLException {
		String requete = "INSERT INTO Voiture (marque, modele, finition, carburant, km, annee, prix) VALUES  "
				+ "(?,?,?,?,?,?,?)";
		PreparedStatement stmt = getConnexion().prepareStatement(requete);
		stmt.setString(1, voiture.getMarque());
		stmt.setString(2, voiture.getModele());
		stmt.setString(3, voiture.getFinition());
		stmt.setString(4, voiture.getCarburant().toString());
		stmt.setInt(5, voiture.getKm());
		stmt.setInt(6, voiture.getAnnee());
		stmt.setInt(7, voiture.getPrix());
		stmt.executeUpdate();
		deconnecter();
	}
	
	public void modifierVoiture(int id, Voiture nouvelle) throws SQLException {
		String requete = "UPDATE Voiture SET marque = ?, modele = ?, finition = ?, carburant = ?, km = ?, annee = ?, prix = ? WHERE id = ?";
		PreparedStatement stmt = getConnexion().prepareStatement(requete);
		stmt.setString(1, nouvelle.getMarque());
		stmt.setString(2, nouvelle.getModele());
		stmt.setString(3, nouvelle.getFinition());
		//stmt.set(4, nouvelle.getCarburant().getChar());
		stmt.setInt(5, nouvelle.getKm());
		stmt.setInt(6, nouvelle.getAnnee());
		stmt.setInt(7, nouvelle.getPrix());
		stmt.setInt(8, id);
		stmt.executeQuery();
		deconnecter();
	}
	
	public ReponseVoiture rechercherVoitures(String saisie, int mini, int nbVoitures) throws SQLException {
		HashMap<String, String> criteres = new HashMap<String, String>();
		if(StringUtils.estEntier(saisie)) {
			criteres.put("id", saisie);
		} else {
			criteres.put("masque", saisie);
		}
		return getVoitures(criteres, mini, nbVoitures);
	}
	
	public ReponseVoiture getVoitures(HashMap<String, String> criteres, int mini, int nbVoitures) throws SQLException {
		String requete = "SELECT id, marque, modele, finition, carburant, km, annee, prix "
				+ "FROM Voiture ",
				requeteComptage = "SELECT COUNT(*) FROM Voiture ";
		String masque = null;
		ReponseVoiture ret = new ReponseVoiture();
		int nbCol = 0;
		if (criteres != null && !criteres.isEmpty()) {
			requete += "WHERE ";
			requeteComptage += "WHERE ";
			masque = criteres.get("masque");
			if (masque != null) {
				requete += construireRequeteMasque(masque);
				requeteComptage += construireRequeteMasque(masque);
				nbCol = StringUtils.nbOccurrence(requete, '?')/masque.split(" ").length;
			} else {
				for(String colonne : criteres.keySet()) {
					requete += colonne + " = ?,";
					requeteComptage += colonne + " = ?,";
				}
				//retrait de la dernière virgule
				requete = requete.substring(0, requete.length()-1);
				requeteComptage = requeteComptage.substring(0, requeteComptage.length()-1);
			}
		}
		if (nbVoitures > 1 && mini >= 0) {
			requete += " LIMIT ? OFFSET ?";
		}
		PreparedStatement pstmt = getPreparedStatemnt (requete, masque, nbCol, criteres, mini, nbVoitures);
		ResultSet res = pstmt.executeQuery();
		res.last();
		ret.setData(new Voiture[res.getRow()]);
		res.beforeFirst();
		int cpt = 0;
		while (res.next()) {
			ret.setData(setVoiture(res), cpt++);
		}
		pstmt = getPreparedStatemnt (requeteComptage, masque, nbCol, criteres, -1, -1);
		res = pstmt.executeQuery();
		if (res.next()) {
			ret.setVolume(res.getInt(1));
		}
		deconnecter();
		return ret;
	}

	private PreparedStatement getPreparedStatemnt (String requete, String masque, int nbCol, HashMap<String, String> criteres, int mini, int nbVoitures) throws SQLException {
		PreparedStatement stmt = getConnexion().prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		int cpt =1;
		if (criteres != null) {
			if (masque == null) {
				for(String colonne : criteres.keySet()) {
					if("string".equals(Voiture.getTypeDonnee(colonne))) {
						stmt.setString(cpt, criteres.get(colonne));
					}
					if("entier".equals(Voiture.getTypeDonnee(colonne))) {
						stmt.setInt(cpt, Integer.parseInt(criteres.get(colonne)));
					}
					cpt++;
				}
			} else if (masque != null) {
				String[] mots = masque.split(" ");
				int indexMot = 0;
				for (String mot : mots) {
					for (int i=1; i< nbCol+1; i++) {
						cpt = indexMot * nbCol + i;
						stmt.setString(cpt, "%"+mot+"%");
					}
					indexMot++;
				}
				cpt++;
			}
		}
		if (nbVoitures > 1 && mini >= 0) {
			stmt.setInt(cpt++, nbVoitures);
			stmt.setInt(cpt++, mini);
		}
		return stmt;
	}

	public String construireRequeteMasque(String saisie) {
		StringBuilder sb = new StringBuilder();
		String[] colonnes = {"marque", "modele", "finition", "carburant"};
		String[] mots = saisie.split(" ");
		boolean or = false;
		for (int i=0; i<mots.length; i++) {
			for (String col : colonnes) {
				if (or) {
					sb.append(" OR ");
				}
				sb.append(col);
				sb.append(" like ? ");
				or = true;
			}
		}
		return sb.toString();
	}

	private Voiture setVoiture(ResultSet res) throws SQLException {
		Voiture ret = new Voiture();
		ret.setId(res.getInt("id"));
		ret.setMarque(res.getString("marque"));
		ret.setModele(res.getString("modele"));
		ret.setFinition(res.getString("finition"));
		ret.setCarburant(Carburant.get(res.getString("carburant")));
		ret.setKm(res.getInt("km"));
		ret.setAnnee(res.getInt("annee"));
		ret.setPrix(res.getInt("prix"));
		return ret;
	}
	
	public void supprimerVoiture(String id) throws SQLException {
		String requete = "DELETE FROM Voiture WHERE id = ?";
		PreparedStatement stmt = getConnexion().prepareStatement(requete);
		stmt.setString(1, id);
		stmt.executeUpdate();
		deconnecter();
	}

}
