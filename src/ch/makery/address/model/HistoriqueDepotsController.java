package ch.makery.address.model;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ch.makery.address.view.Depot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoriqueDepotsController {

    @FXML private TableView<Depot> tableDepots;
    @FXML private TableColumn<Depot, String> colDepotDate;
    @FXML private TableColumn<Depot, String> colDepotType;

    @FXML private Label lblDate;
    @FXML private Label lblType;
    @FXML private Label lblQuantite;
    @FXML private Label lblPointsGagnes;
    @FXML private Label lblLieuDepot; // Tu peux mettre une valeur par défaut ou étendre ta classe Depot

    @FXML private Label lblStatusBar;

    private final ObservableList<Depot> depots = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Exemple de données fictives
        depots.addAll(createDepotExemple());

        // Associe les colonnes avec les valeurs
        colDepotDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(formatDate(cellData.getValue().getDateDepot())));
        colDepotType.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));

        tableDepots.setItems(depots);

        tableDepots.setOnMouseClicked(this::afficherDetailsDepot);
    }

    private void afficherDetailsDepot(MouseEvent event) {
        Depot depot = tableDepots.getSelectionModel().getSelectedItem();
        if (depot != null) {
            lblDate.setText("Date : " + formatDate(depot.getDateDepot()));
            lblType.setText("Type : " + depot.getType());
            lblQuantite.setText("Quantité : " + depot.getPoidsDechet() + " kg");
            lblPointsGagnes.setText("Points gagnés : " + depot.getPointsAttribues());
            lblLieuDepot.setText("Lieu : ExempleVille"); // À adapter si tu ajoutes le champ dans la classe Depot
        }
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private ObservableList<Depot> createDepotExemple() {
        ObservableList<Depot> exemple = FXCollections.observableArrayList();

        // Ajoute quelques données factices
        exemple.add(new Depot(1, 20, new Date(), new java.util.ArrayList<>()));
        exemple.add(new Depot(2, 35, new Date(), new java.util.ArrayList<>()));

        return exemple;
    }
}
