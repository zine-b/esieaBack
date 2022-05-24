package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	private static String REP_CONF = "src/main/resources/config/";
	
	protected static final Logger logger = Logger.getLogger(Configuration.class);
	private static FileInputStream inputConf = null;

    private static String fichierConf; 
    
    private static Properties propConf = new Properties();

	public static void initConfig() {
    	if (inputConf != null) { return;}
    	
    	try {
    		logger.debug("Fichier de conf ou contexte : " + REP_CONF+fichierConf);
    		inputConf = new FileInputStream(new File(REP_CONF+fichierConf));
    		logger.info("Chargement du fichier de conf : " + REP_CONF+fichierConf);
    		propConf.load(inputConf);

    	} catch (Exception e) {
    		logger.info("Erreur lors du chargement du fichier de configuration", e);
    	}
    }

	/**
     * Lit un paramétrage du fichier de configuration 
     * @param key La clé de configuration à lire
     * @return Retourne la configuration sous forme de String
     */
    public static String getConfig(String key) {
    	String ret;
    	initConfig();
		logger.debug("Lecture de la clé " + key + " dans la config");
		ret = propConf.getProperty(key);
		if (ret == null) {
			logger.error("La clé '" + key + "' n'existe pas dans le fichier " + REP_CONF+fichierConf);
		}
		return ret;
    }
}
