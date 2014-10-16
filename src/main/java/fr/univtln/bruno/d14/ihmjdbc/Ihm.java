package fr.univtln.bruno.d14.ihmjdbc;

import fr.univtln.bruno.d14.ihmjdbc.exceptions.ConfigImportException;
import fr.univtln.bruno.d14.ihmjdbc.ihm.VueBibliotheque;
import fr.univtln.bruno.d14.ihmjdbc.model.ModelBibliotheque;
import fr.univtln.bruno.d14.ihmjdbc.utils.ConfigReader;

/**
 * Created by bruno on 16/10/14.
 * Cette classe lance l'IHM pour consulter/ajouter/supprimer des Auteurs.
 */
public class Ihm {
    public static void main(String[] args) {
        try {
            //Importation des paramètres de configuration (cf. src/main/resources/config.xml)
            ConfigReader.importConfig();

            //On crée le modèle et la vue. Le contrôleur est créé dans la Vue.
            new VueBibliotheque(ModelBibliotheque.getInstance());

        } catch (ConfigImportException e) {
            e.printStackTrace();
        }
    }
}
