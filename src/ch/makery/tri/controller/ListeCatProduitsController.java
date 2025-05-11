package ch.makery.tri.controller;

import ch.makery.tri.model.Produit;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListeCatProduitsController {

    @FXML private VBox formulairePane;
    @FXML private TextField nomProduitField;
    @FXML private TextField categorieField;
    @FXML private TextField prixField;
    @FXML private TextField searchField;

    @FXML private TableView<Produit> produitsTable;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, Double> colPrix;

    private static final ObservableList<Produit> produits = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        produitsTable.setItems(produits);

        colNom.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getNom()));
        colCategorie.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCategorie()));
        colPrix.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrix()));
    }

    @FXML
    private void handleAfficherFormulaire() {
        formulairePane.setVisible(true);
        formulairePane.setManaged(true);
    }

    @FXML
    private void handleValiderProduit() {
        String nom = nomProduitField.getText();
        String categorie = categorieField.getText();
        String prixText = prixField.getText();

        if (nom.isBlank() || categorie.isBlank() || prixText.isBlank()) {
            System.out.println("Tous les champs doivent être remplis.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixText);
            Produit produit = new Produit(0, categorie, nom, prix);  // ⚠️ Adapte selon ton constructeur
            produits.add(produit);

            // Réinitialisation du formulaire
            nomProduitField.clear();
            categorieField.clear();
            prixField.clear();
            formulairePane.setVisible(false);
            formulairePane.setManaged(false);

        } catch (NumberFormatException e) {
            System.out.println("Le prix doit être un nombre.");
        }
    }

    @FXML
    private void handleModifierProduit() {
        Produit selected = produitsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nomProduitField.setText(selected.getNom());
            categorieField.setText(selected.getCategorie());
            prixField.setText(String.valueOf(selected.getPrix()));

            produits.remove(selected); // Supprime temporairement, on le remplacera à la validation
            formulairePane.setVisible(true);
            formulairePane.setManaged(true);
        } else {
            System.out.println("Aucun produit sélectionné.");
        }
    }

    @FXML
    private void handleSupprimerProduit() {
        Produit selected = produitsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            produits.remove(selected);
        } else {
            System.out.println("Aucun produit sélectionné.");
        }
    }
}
