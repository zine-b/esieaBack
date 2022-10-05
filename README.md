# Application Back Stock Car
Ce projet contient le code de la partie Back du projet Stock Car. Il est composé de code Java intéragissant avec une base de données.

# Prérequis
1. Installer un server Mysql téléchargeable sur le site officiel
2. Configurer le serveur Mysql à votre guise (utilisateurs, droits, etc.)
3. Créer la base et le schéma de base à partir du fichier init.sql situé dans src/main/resources/

# Installation

1. Déployer le fichier War (dans target) dans un serveur d'application (Tomcat)
2. Arrêter le server d'application (Tomcat)
3. Modifier le fichier de configuration situé dans webapps/esieaBack/WEB-INF/conf.properties
4. Redémarrer le serveur d'application (Tomcat)
5. Tester le fonctionnement de l'API en lançant une requête exemple : http://localhost:8080/esieaBack/rest/voiture/get/all
6. Un ensemble de voitures au format Json apparaît sur la page
