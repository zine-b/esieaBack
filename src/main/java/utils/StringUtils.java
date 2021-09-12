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
	
	public static int nbOccurrence(String base, char aChercher) {
		int ret = 0;
		for (int i=0; i<base.length(); i++) {
			if(base.charAt(i) == aChercher) {
				ret++;
			}
		}
		return ret;
	}
	
}
