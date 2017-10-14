package fr.univtln.bruno.coursjava.librarymanager.ihmjdbc;


import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.exceptions.ConfigImportException;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.EntityManager;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.entities.Auteur;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.utils.ConfigReader;

/**
 * Cette classe permet de consulter la relation AUTEUR dans la base de données.
 * Cette un exemple d'utilisation de l'entity manager.
 */
public class Consultation {
    public static void main(String[] args) throws ConfigImportException, PersistanceException {
        //Importation des paramètres de configuration (cf. src/main/resources/config.xml)
        ConfigReader.importConfig();

        EntityManager entityManager = EntityManager.getInstance();

        try {
            for (Auteur auteur : Auteur.findAll())
                System.out.println(auteur);
        } catch (PersistanceException e) {
            e.getException().printStackTrace();

        }

        entityManager.dispose();
        Auteur.dispose();
    }
}
