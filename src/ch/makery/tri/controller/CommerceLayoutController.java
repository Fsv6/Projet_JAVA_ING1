package ch.makery.tri.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class CommerceLayoutController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private void initialize() {
        // Charge la page Produits par défaut au démarrage
        handleShowProduits();
    }

    @FXML
    private void handleShowProduits() {
        loadView("/ch/makery/tri/views/ListeCatProduits.fxml");
    }

    @FXML
    private void handleShowContrat() {
        loadView("/ch/makery/tri/views/ContratCommerce.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxmlPath));
            // Ancrage pour que la vue remplisse tout l'espace
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
