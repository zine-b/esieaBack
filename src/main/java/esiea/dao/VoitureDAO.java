package esiea.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.mysql.cj.xdevapi.PreparableStatement;

import esiea.metier.Voiture;
import esiea.metier.Voiture.Carburant;
import utils.StringUtils;

public class VoitureDAO {
	
	private static Connection connection;
	private String url = "jdbc:mysql://localhost:3306/stockcar";
	private String user = "brice";
	private String pwd = "brice";
	
	public VoitureDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			if (connection == null) {
				connection = DriverManager.getConnection(url, user, pwd);
			}
		} catch (SQLException sql) {sql.printStackTrace(); } 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ajouterVoiture(Voiture voiture) throws SQLException {
		String requete = "INSERT INTO Voiture (marque, modele, finition, carburant, km, annee, prix) VALUES  "
				+ "(?,?,?,?,?,?,?)";
		PreparedStatement stmt = connection.prepareStatement(requete);
		stmt.setString(1, voiture.getMarque());
		stmt.setString(2, voiture.getModele());
		stmt.setString(3, voiture.getFinition());
		stmt.setString(4, voiture.getCarburant().toString());
		stmt.setInt(5, voiture.getKm());
		stmt.setInt(6, voiture.getAnnee());
		stmt.setInt(7, voiture.getPrix());
		stmt.executeUpdate();
	}
	
	public void modifierVoiture(int id, Voiture nouvelle) throws SQLException {
		String requete = "UPDATE Voiture SET marque = ?, "
				+ "modele = ?, "
				+ "finition = ?, "
				+ "carburant = ?, "
				+ "km = ?, "
				+ "annee = ?, "
				+ "prix = ?), "
				+ "WHERE id = ?";
		PreparedStatement stmt = connection.prepareStatement(requete);
		stmt.setString(1, nouvelle.getMarque());
		stmt.setString(2, nouvelle.getModele());
		stmt.setString(3, nouvelle.getFinition());
		//stmt.set(4, nouvelle.getCarburant().getChar());
		stmt.setInt(5, nouvelle.getKm());
		stmt.setInt(6, nouvelle.getAnnee());
		stmt.setInt(7, nouvelle.getPrix());
		stmt.setInt(8, id);
		stmt.executeQuery();
	}
	
	public Voiture[] getVoiture(String saisie) throws SQLException {
		HashMap<String, String> criteres = new HashMap<String, String>();
		if(StringUtils.estEntier(saisie)) {
			criteres.put("id", saisie);
		} else {
			criteres.put("masque", saisie);
		}
		Voiture[] ret = getVoitures(criteres);
	//	if (ret.length > 0 && StringUtils.estEntier(saisie)) {
			return ret;
	//	}
		//return null;
	}
	
	public Voiture[] getVoitures(HashMap<String, String> criteres) throws SQLException {
		String requete = "SELECT id, marque, modele, finition, carburant, km, annee, prix "
				+ "FROM Voiture ";
		String masque = null;
		int nbCol = 0;
		if (criteres != null && !criteres.isEmpty()) {
			requete += "WHERE ";
			masque = criteres.get("masque");
			if (masque != null) {
				requete += construireRequeteMasque(masque);
				nbCol = StringUtils.nbOccurrence(requete, '?')/masque.split(" ").length;
			} else {
				for(String colonne : criteres.keySet()) {
					requete += colonne + " = ?,";
				}
				//retrait de la dernière virgule
				requete = requete.substring(0, requete.length()-1);
			}
		}
		
		PreparedStatement stmt = connection.prepareStatement(requete, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
						stmt.setString(indexMot * nbCol + i, "%"+mot+"%");
					}
					indexMot++;
				}
			}
		}
		ResultSet res = stmt.executeQuery();
		res.last();
		Voiture[] ret = new Voiture[res.getRow()];
		res.beforeFirst();
		cpt = 0;
		while (res.next()) {
			ret[cpt++] = setVoiture(res);
		}
		return ret;
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
		PreparedStatement stmt = connection.prepareStatement(requete);
		stmt.setString(1, id);
		stmt.executeUpdate();
	}

}
