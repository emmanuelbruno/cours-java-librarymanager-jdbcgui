package fr.univtln.bruno.d14.ihmjdbc.ihm;

import fr.univtln.bruno.d14.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.d14.ihmjdbc.model.ModelBibliotheque;
import fr.univtln.bruno.d14.ihmjdbc.model.entities.Auteur;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Created by bruno on 03/10/14.
 * Cette classe est le contrôleur de l'IHM.
 * Il contient les modèles des champs d'édition du formmulaire.
 */
public class ControleurBibliotheque {
    private VueBibliotheque vueBibliotheque;
    private ModelBibliotheque modeleBibliotheque;

    //Les modèles utilisés par les champs texte de la vue
    private Document nomNouvelAuteurModel = new PlainDocument();
    private Document prenomNouvelAuteurModel = new PlainDocument();

    public ControleurBibliotheque(final VueBibliotheque vueBibliotheque, ModelBibliotheque modeleBibliotheque) {
        this.vueBibliotheque = vueBibliotheque;
        this.modeleBibliotheque = modeleBibliotheque;

        DocumentListener ecouteurChangementTexte = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            //Le test pour autoriser ou non la création d'un auteur en fonction
            //de l'état des champs textes.
            @Override
            public void changedUpdate(DocumentEvent e) {
                if ((nomNouvelAuteurModel.getLength() == 0 || prenomNouvelAuteurModel.getLength() == 0))
                    //c'est la vue qui gère les actions visuelles.
                    vueBibliotheque.setCreationAuteurOk(false);
                else
                    vueBibliotheque.setCreationAuteurOk(true);
            }
        };
        nomNouvelAuteurModel.addDocumentListener(ecouteurChangementTexte);
        prenomNouvelAuteurModel.addDocumentListener(ecouteurChangementTexte);

    }

    public Document getNomNouvelAuteurModel() {
        return nomNouvelAuteurModel;
    }

    public Document getPrenomNouvelAuteurModel() {
        return prenomNouvelAuteurModel;
    }

    public void ajouterAuteur() {
        try {
            //L'ajout d'un auteur est délégué au modèle.
            modeleBibliotheque.ajouterAuteur(
                    prenomNouvelAuteurModel.getText(0, prenomNouvelAuteurModel.getLength()),
                    nomNouvelAuteurModel.getText(0, nomNouvelAuteurModel.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (PersistanceException e) {
            e.getException().printStackTrace();
        }
        clearAuteur();
    }

    public void supprimerAuteur(Auteur auteur) {
        try {
            //La suppression est déléguée au modèle.
            modeleBibliotheque.supprimerAuteur(auteur);
        } catch (PersistanceException e) {
            e.getException().printStackTrace();
        }
    }

    public void clearAuteur() {
        try {
            nomNouvelAuteurModel.remove(0, nomNouvelAuteurModel.getLength());
            prenomNouvelAuteurModel.remove(0, prenomNouvelAuteurModel.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
