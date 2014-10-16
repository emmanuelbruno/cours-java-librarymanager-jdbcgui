package fr.univtln.bruno.d14.ihmjdbc.model;

import fr.univtln.bruno.d14.ihmjdbc.exceptions.PersistanceException;
import fr.univtln.bruno.d14.ihmjdbc.model.entities.Auteur;

import javax.swing.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by bruno on 16/10/14.
 * Cette classe est le modèle commun utilisé par les JList et JCombox de tous les auteurs.
 * Elle observe le modeleBibliotheque pour être prévenue de tout changement.
 */
public class AuteurListModel extends DefaultComboBoxModel<Auteur> implements Observer {
    private final ModelBibliotheque modelBibliotheque;
    private List<Auteur> auteurs;

    public AuteurListModel(ModelBibliotheque modelBibliotheque) {
        this.modelBibliotheque = modelBibliotheque;
        //la liste est remplie à la création
        refresh();
    }

    private void refresh() {
        try {
            auteurs = modelBibliotheque.findAllAuteurs();
        } catch (PersistanceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        //On vérifie que le changement du modèle concerne bien les auteurs
        if ((Class) arg == Auteur.class) {
            refresh();
            //si l'auteur selectionné n'est plus dans la liste on déselectionne.
            if (!auteurs.contains(getSelectedItem()))
                setSelectedItem(null);
            else System.out.println(getSelectedItem() + " in " + auteurs);
            fireContentsChanged(this, 0, auteurs.size() - 1);
        }
    }

    @Override
    public int getSize() {
        if (auteurs == null) return 0;
        else
            return auteurs.size();
    }

    @Override
    public Auteur getElementAt(int index) {
        return auteurs.get(index);
    }
}
