package fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model;

import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.entities.Auteur;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Created by bruno on 16/10/14.
 * Cette classe singleton est la facade de modele.
 * Elle observe TOUTES les instances d'EntityManager pour être prévenue des changments.
 * Elle est observée par tous ceux qui veux connaitre les changements dans le modèle
 * (en particuliers les modeles dans les composants de la vue).
 * Elle permet aussi un accès unique aux méthodes de gestions (création, recherche, ...) des entités.
 */
public class ModelBibliotheque extends Observable implements Observer {
    private static final ModelBibliotheque MODEL_BIBLIOTHEQUE = new ModelBibliotheque();
    private static Logger logger = Logger.getLogger(ModelBibliotheque.class.getName());

    private ModelBibliotheque() {
    }

    public static ModelBibliotheque getInstance() {
        return MODEL_BIBLIOTHEQUE;
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.info("Model changed : " + arg);
        setChanged();
        notifyObservers(arg);
    }

    public List<Auteur> findAllAuteurs() throws PersistanceException {
        return Auteur.findAll();
    }

    public Auteur findAuteurById(int id) throws PersistanceException {
        return Auteur.findById(id);
    }

    public void ajouterAuteur(String nom, String prenom) throws PersistanceException {
        EntityManager entityManager = EntityManager.getInstance();
        entityManager.persist(new Auteur.AuteurBuilder().setPrenom(prenom).setNom(nom).createAuteur());
        entityManager.dispose();
    }

    public void supprimerAuteur(Auteur auteur) throws PersistanceException {
        EntityManager entityManager = EntityManager.getInstance();
        entityManager.remove(auteur);
        entityManager.dispose();
    }
}
