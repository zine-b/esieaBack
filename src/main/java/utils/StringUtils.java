package utils;

public class StringUtils {

	/**
	 * Détermine si la chaine passée en paramètre est un nombre entier
	 * @param str La chaine à parser
	 * @return Retourne true si la chaine est un entier, sinon retourne false
	 */
	public static boolean estEntier(String str) {
		if (str == null) { return false; }
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
}
