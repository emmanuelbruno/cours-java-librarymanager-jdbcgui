package fr.univtln.bruno.d14.ihmjdbc;

import fr.univtln.bruno.d14.ihmjdbc.exceptions.ConfigImportException;
import fr.univtln.bruno.d14.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.d14.ihmjdbc.model.EntityManager;
import fr.univtln.bruno.d14.ihmjdbc.model.entities.Auteur;
import fr.univtln.bruno.d14.ihmjdbc.utils.ConfigReader;
import fr.univtln.bruno.d14.ihmjdbc.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Cette classe permet de créer/recréer la relation AUTEUR dans la base de données.
 */
public class Creation {
    private static String ddl =
            "DROP TABLE IF EXISTS AUTEUR;\n" +
                    "CREATE TABLE AUTEUR (\n" +
                    "        ID BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "        NOM VARCHAR(255),\n" +
                    "PRENOM VARCHAR(255),\n" +
                    "PRIMARY KEY (ID)\n" +
                    ");";

    public static void main(String[] args) throws ConfigImportException, PersistanceException, SQLException {
        //Importation des paramètres de configuration (cf. src/main/resources/config.xml)
        ConfigReader.importConfig();

        //Utilisation de l'entity manager pour ajouter tous les auteurs dans UNE SEULE transaction.
        EntityManager entityManager = EntityManager.getInstance();

        try {
            entityManager.setAutoCommit(false);

            Connection connection = DatabaseManager.getConnection();
            connection.createStatement().executeUpdate(ddl);
            DatabaseManager.releaseConnection(connection);

            List<Auteur> auteurs = Arrays.asList(
                    new Auteur.AuteurBuilder().setPrenom("Jean").setNom("Martin").createAuteur(),
                    new Auteur.AuteurBuilder().setPrenom("Marie").setNom("Durand").createAuteur());

            for (Auteur auteur : auteurs)
                entityManager.persist(auteur);

            entityManager.commit();

        } catch (PersistanceException e) {
            System.out.println(e.getException());
        } finally {
            entityManager.dispose();
        }

    }
}
