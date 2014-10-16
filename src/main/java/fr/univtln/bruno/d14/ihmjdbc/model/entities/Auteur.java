package fr.univtln.bruno.d14.ihmjdbc.model.entities;

import fr.univtln.bruno.d14.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.d14.ihmjdbc.model.Entity;
import fr.univtln.bruno.d14.ihmjdbc.utils.DatabaseManager;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by bruno on 03/10/14.
 * Cette classe représente un Auteurs et assure leur persistance.
 */
public class Auteur implements Serializable, Entity {
    //en suivant le pattern flyweight
    //Les auteurs lus ou ecire dans la BD sont ajoutés dans la map
    //En cas de recherche, ils sont recherchés ici en premier
    //ATTENTION, l'implantation proposée ici est SIMPLISTE
    //Les objets ne sont retirés que lorsqu'ils sont supprimés de la base.
    private static Map<Integer, Auteur> cacheAuteurs = new HashMap<>();

    private static Logger logger = Logger.getLogger(Auteur.class.getName());

    private static PreparedStatement findByID;

    private static PreparedStatement findAll;

    public int ID;
    private String nom;
    private String prenom;

    //Ce constructeur est utilisé en privé quand un auteur est extrait de la BD
    private Auteur(int ID, String nom, String prenom) {
        this(nom, prenom);
        this.ID = ID;
    }

    //Ce constructeur est utilisé par le builder quand un auteur est créé dans l'application.
    //L'ID sera attribué après la persistance par la BD.
    private Auteur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    //Cette méthode doit être appelée à la fin de l'application pour libérer les connexions
    //des preparedstatments.
    public static void dispose() throws PersistanceException {
        try {
            findByID.close();
            findAll.close();
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    //L'initialisation des preparedstatments.
    static {
        try {
            Connection connection = DatabaseManager.getConnection();
            findByID = connection.prepareStatement("select ID, NOM, PRENOM from AUTEUR where ID=?");
            findAll = connection.prepareStatement("select ID, NOM, PRENOM from AUTEUR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Auteur createFromResultSet(ResultSet result) throws SQLException {
        return new Auteur(result.getInt("ID"), result.getString("NOM"), result.getString("PRENOM"));
    }

    public static Auteur findById(int id) throws PersistanceException {
        try {
            Auteur auteur;
            //si l'auteur est déjà en mémoire on retourne sa référence
            if ((auteur = cacheAuteurs.get(id)) != null) {
                logger.finest("find auteur dans le cache : " + auteur);
                return auteur;
            }
            //sinon on va le chercheur et on l'ajoute à la map
            else {
                findByID.setInt(1, id);
                ResultSet result = findByID.executeQuery();
                if (result.next()) {
                    auteur = createFromResultSet(result);
                    cacheAuteurs.put(id, auteur);
                    logger.finest("find auteur dans la base : " + auteur);
                    return auteur;
                } else
                    throw new PersistanceException("Auteur " + id + " introuvable.");
            }
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }


    public static List<Auteur> findAll() throws PersistanceException {
        try {
            ResultSet result = findAll.executeQuery();
            List<Auteur> resultAuteurs = new ArrayList<>();
            while (result.next()) {
                //On retourne l'auteur si déjà dans la map d'cacheAuteurs
                Auteur auteur = cacheAuteurs.get(result.getInt("ID"));
                //Sinon on le cherche dans la base de données
                if (auteur == null) {
                    auteur = createFromResultSet(result);
                    //On l'ajoute dans le cache
                    cacheAuteurs.put(auteur.ID, auteur);
                    logger.info("find auteur dans la base : " + auteur);
                } else {
                    logger.info("find auteur dans le cache : " + auteur);
                }
                resultAuteurs.add(auteur);
            }
            return resultAuteurs;
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    //La clé de l'auteur est auto générée par la base de données.
    //après l'insertion cette clé est retrouvée et ajoutée à l'auteur.
    @Override
    public void persist(Connection connection) throws PersistanceException {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO AUTEUR(NOM, PRENOM) VALUES ('" + nom + "','" + prenom + "')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ID = rs.getInt(1);
            } else {
               throw  new PersistanceException("La clé de l'auteur n'a pas pu être retrouvée.");
            }
            cacheAuteurs.put(ID, this);
            logger.info("creation de l'auteur : " + this);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    //Mise à jour de l'instance à partir de la base de données
    //Cette méthode n'a pas été testée
    @Override
    public void merge(Connection connection) throws PersistanceException {
        try {
            connection.createStatement().executeUpdate("UPDATE AUTEUR SET NOM=" + nom + "\", SET PRENOM=\"" + prenom + "\")");
            logger.info("merge de l'auteur : " + this);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    //Mise à jour de la base de données à partir de l'instance
    //Cette méthode n'a pas été testée
    @Override
    public void update(Connection connection) throws PersistanceException {
        Auteur auteur = Auteur.findById(ID);
        this.nom = auteur.nom;
        this.prenom = auteur.prenom;
        logger.info("update de l'auteur : " + this);
    }

    @Override
    public void remove(Connection connection) throws PersistanceException {
        try {
            connection.createStatement().executeUpdate("DELETE FROM AUTEUR WHERE ID=\'" + ID + "\'");
            cacheAuteurs.remove(ID);
            logger.info("delete de l'auteur : " + this);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return nom + ", " + prenom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auteur auteur = (Auteur) o;

        if (ID != auteur.ID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    public static class AuteurBuilder {
        private String nom;
        private String prenom;

        public AuteurBuilder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public AuteurBuilder setPrenom(String prenom) {
            this.prenom = prenom;
            return this;
        }

        public Auteur createAuteur() {
            return new Auteur(nom, prenom);
        }
    }
}
