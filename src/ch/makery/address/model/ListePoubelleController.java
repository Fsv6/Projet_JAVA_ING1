package ch.makery.address.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import ch.makery.address.view.PoubelleIntelligente;

public class ListePoubelleController {

    @FXML
    private Button btnPoubelles;
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnFaireDepot;
    @FXML
    private Button btnBonsAchat;
    @FXML
    private Button btnCommerces;
    @FXML
    private TableView<PoubelleIntelligente> tablePoubelles;
    @FXML
    private TableColumn<PoubelleIntelligente, Integer> colId;
    @FXML
    private TableColumn<PoubelleIntelligente, String> colLocalisation;
    @FXML
    private TableColumn<PoubelleIntelligente, String> colCapacite;
    @FXML
    private Label lblStatusBar;

    @FXML
    public void initialize() {
        // Colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocalisation.setCellValueFactory(new PropertyValueFactory<>("nomQuartier"));
        colCapacite.setCellValueFactory(cellData -> {
            PoubelleIntelligente p = cellData.getValue();
            String status = p.notifierSiBacPlein().isEmpty() ? "Disponible" : "Pleine";
            return new SimpleStringProperty(status);
        });

        // Exemple de données
        ObservableList<PoubelleIntelligente> poubelles = FXCollections.observableArrayList();
        poubelles.add(new PoubelleIntelligente(1, "Centre-ville", 48.85f, 2.35f));
        poubelles.add(new PoubelleIntelligente(2, "Nord", 48.86f, 2.37f));
        tablePoubelles.setItems(poubelles);

        lblStatusBar.setText("Chargé : " + poubelles.size() + " poubelles.");

        // Navigation boutons
        btnPoubelles.setOnAction(e -> openHistoriqueDepots());
        btnFaireDepot.setOnAction(e -> openDepots());
    }

    // Méthode pour ouvrir la page Historique
    private void openHistoriqueDepots() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/HistoriqueCompte.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Historique des Dépôts");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la page Dépôt
    private void openDepots() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Depot.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Faire un dépôt");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
