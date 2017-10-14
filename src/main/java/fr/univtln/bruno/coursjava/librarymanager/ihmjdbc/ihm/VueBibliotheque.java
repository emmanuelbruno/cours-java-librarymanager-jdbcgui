package fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.ihm;

import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.AuteurListModel;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.ModelBibliotheque;
import fr.univtln.bruno.coursjava.librarymanager.ihmjdbc.model.entities.Auteur;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by bruno on 03/10/14.
 * Cette classe gère l'affichage de la vue.
 */
public class VueBibliotheque extends JFrame {
    private final ModelBibliotheque modeleBibliotheque;
    private final ControleurBibliotheque controleurBibliotheque;

    private final AuteurListModel auteurListModel;

    private final JPanel auteursSuppressionPanel = new JPanel(new GridBagLayout());
    private final JPanel auteurDetailPanel = new JPanel(new GridBagLayout());
    private final JPanel auteurAjoutPanel = new JPanel(new GridBagLayout());

    private final JList<Auteur> auteurSuppressionJList;
    private final JButton supprimerAuteurJButton = new JButton("Supprimer Auteur");

    private final JComboBox<Auteur> auteurDetailJComboBox;
    private final JTextField nomAuteurAjoutjTextField;
    private final JTextField prenomAuteurAjoutjTextField;
    private final JLabel nomAuteurJLabel = new JLabel("Nom");
    private final JLabel prenomAuteurJLabel = new JLabel("Prénom");
    private final JButton annulerAuteurJButton = new JButton("Annuler");
    private final JButton ajouterAuteurJButton = new JButton("Ajouter Auteur");

    private final JTextField idAuteurDetailjTextField = new JTextField();
    private final JTextField prenomAuteurDetailjTextField = new JTextField();
    private final JTextField nomAuteurDetailjTextField = new JTextField();

    public VueBibliotheque(ModelBibliotheque modeleBibliotheque) {
        super("Bibliotheque");
        setSize(800, 600);

        this.modeleBibliotheque = modeleBibliotheque;
        this.controleurBibliotheque = new ControleurBibliotheque(this, modeleBibliotheque);
        this.auteurListModel = new AuteurListModel(this.modeleBibliotheque);

        modeleBibliotheque.addObserver(auteurListModel);

        auteurSuppressionJList = new JList<>(auteurListModel);
        auteurSuppressionJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setSuppressionAuteurOk(auteurSuppressionJList.getSelectedValue() != null);
            }
        });

        auteurDetailJComboBox = new JComboBox<Auteur>(auteurListModel);
        auteurDetailJComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()) {
                    case ItemEvent.DESELECTED:
                        montrerDetail(null);
                        break;
                    case ItemEvent.SELECTED:
                        montrerDetail(auteurDetailJComboBox.getItemAt(auteurDetailJComboBox.getSelectedIndex()));
                        break;
                }

            }
        });

        supprimerAuteurJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Auteur auteur : auteurSuppressionJList.getSelectedValuesList())
                    controleurBibliotheque.supprimerAuteur(auteur);
            }
        });

        ajouterAuteurJButton.setEnabled(false);
        ajouterAuteurJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleurBibliotheque.ajouterAuteur();
            }
        });

        supprimerAuteurJButton.setEnabled(false);

        annulerAuteurJButton.setEnabled(false);
        annulerAuteurJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleurBibliotheque.clearAuteur();
            }
        });

        //Les modèles des champs textes sont dans le contrôleur
        nomAuteurAjoutjTextField = new JTextField(controleurBibliotheque.getNomNouvelAuteurModel(), "", 10);
        prenomAuteurAjoutjTextField = new JTextField(controleurBibliotheque.getPrenomNouvelAuteurModel(), "", 10);

        idAuteurDetailjTextField.setEditable(false);
        prenomAuteurDetailjTextField.setEditable(false);
        nomAuteurDetailjTextField.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        // Suppression d'un auteur
        auteursSuppressionPanel.setBorder(BorderFactory.createTitledBorder("Suppression"));
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(2, 2, 2, 2);
        c.weighty = 0.8;
        c.gridx = 0;
        c.gridy = 0;
        auteursSuppressionPanel.add(auteurSuppressionJList, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.weighty = 0.2;
        c.gridx = 0;
        c.gridy = 1;
        auteursSuppressionPanel.add(supprimerAuteurJButton, c);

        //Le détail d'un auteur
        auteurDetailPanel.setBorder(BorderFactory.createTitledBorder("Détails"));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        auteurDetailPanel.add(auteurDetailJComboBox, c);
        c.gridx = 0;
        c.gridy = 1;
        auteurDetailPanel.add(idAuteurDetailjTextField, c);
        c.gridx = 0;
        c.gridy = 2;
        auteurDetailPanel.add(nomAuteurDetailjTextField, c);
        c.gridx = 0;
        c.gridy = 3;
        auteurDetailPanel.add(prenomAuteurDetailjTextField, c);


        //L'ajout d'un auteur
        auteurAjoutPanel.setBorder(BorderFactory.createTitledBorder("Ajout"));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        auteurAjoutPanel.add(nomAuteurJLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        auteurAjoutPanel.add(nomAuteurAjoutjTextField, c);

        c.gridx = 0;
        c.gridy = 1;
        auteurAjoutPanel.add(prenomAuteurJLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        auteurAjoutPanel.add(prenomAuteurAjoutjTextField, c);

        c.gridx = 0;
        c.gridy = 2;
        auteurAjoutPanel.add(annulerAuteurJButton, c);

        c.gridx = 1;
        c.gridy = 2;
        auteurAjoutPanel.add(ajouterAuteurJButton, c);

        //Ajout des panel de suppression, d'ajout et de detail
        getContentPane().setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        getContentPane().add(auteurDetailPanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        getContentPane().add(auteursSuppressionPanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        getContentPane().add(auteurAjoutPanel, c);
        //pack();

        setVisible(true);
    }

    //Les actions à faire quand les informations saisies/selectionnées permettent
    //de supprimer un auteur
    private void setSuppressionAuteurOk(boolean b) {
        supprimerAuteurJButton.setEnabled(b);
    }

    //Les actions à faire quand les informations saisies permettent
    //de créer l'auteur
    public void setCreationAuteurOk(boolean creationAuteurOk) {
        ajouterAuteurJButton.setEnabled(creationAuteurOk);
        annulerAuteurJButton.setEnabled(creationAuteurOk);
    }

    //Mise à jour de la vue pour présenter les détails d'un auteur
    public void montrerDetail(Auteur auteur) {
        if (auteur == null) {
            idAuteurDetailjTextField.setText("");
            prenomAuteurDetailjTextField.setText("");
            nomAuteurDetailjTextField.setText("");
        } else {
            idAuteurDetailjTextField.setText(String.valueOf(auteur.ID));
            prenomAuteurDetailjTextField.setText(auteur.getPrenom());
            nomAuteurDetailjTextField.setText(auteur.getNom());
        }
    }

}
