package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	private static String REP_CONF = "src/main/resources/";
	
	protected static final Logger logger = Logger.getLogger(Configuration.class);
	private static InputStream inputConf = null;

    private static String fichierConf = "conf.properties"; 
    
    private static Properties propConf = new Properties();
    private static Configuration instance;
    
    private static Configuration getInstance() {
    	if (instance == null) {
    		instance = new Configuration();
    	}
    	return instance;
    }

	public void initConfig() {
    	if (inputConf != null) { return;}
    	try {
    		logger.debug("Fichier de conf ou contexte : " + REP_CONF+fichierConf);
    		logger.debug(getClass().getClassLoader().getResourceAsStream(fichierConf));
    		
    		inputConf = getClass().getClassLoader().getResourceAsStream(fichierConf);
    		logger.info("Chargement du fichier de conf : " + REP_CONF+fichierConf);
    		propConf.load(inputConf);

    	} catch (Exception e) {
    		e.printStackTrace();
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
    	getInstance().initConfig();
		logger.debug("Lecture de la clé " + key + " dans la config");
		ret = propConf.getProperty(key);
		if (ret == null) {
			logger.error("La clé '" + key + "' n'existe pas dans le fichier " + REP_CONF+fichierConf);
		}
		logger.debug("Lecture de la cl� " + key + " dans la config : " + ret);
		return ret;
    }
}
