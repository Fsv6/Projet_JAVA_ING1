package ch.makery.address.model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class DepotController {

    @FXML
    private TextField txtPoids; // Champ pour saisir le poids
    @FXML
    private ComboBox<String> comboTypeDechet; // ComboBox pour choisir le type de déchet
    @FXML
    private Button btnFaireDepot; // Bouton pour valider le dépôt
    @FXML
    private Label lblStatusBar; // Label pour la barre de statut

    // Initialisation de la ComboBox avec des types de déchets (peut être modifié selon tes besoins)
    @FXML
    public void initialize() {
        comboTypeDechet.getItems().addAll("Type 1", "Type 2", "Type 3", "Type 4"); // Exemple de types
    }

    // Méthode pour gérer l'événement du clic sur "Faire le dépôt"
    @FXML
    public void handleFaireDepot(ActionEvent event) {
        String poidsText = txtPoids.getText();
        String typeDechet = comboTypeDechet.getValue();

        // Vérifier que le poids et le type de déchet sont valides
        if (poidsText.isEmpty() || typeDechet == null) {
            lblStatusBar.setText("Veuillez remplir tous les champs.");
            lblStatusBar.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            double poids = Double.parseDouble(poidsText); // Conversion du poids en nombre
            // Logique pour gérer le dépôt (par exemple, enregistrer le dépôt dans une base de données)
            // Ici, on affiche juste un message de confirmation pour l'exemple
            lblStatusBar.setText("Dépôt de " + poids + " kg de " + typeDechet + " effectué.");
            lblStatusBar.setStyle("-fx-text-fill: green;");
        } catch (NumberFormatException e) {
            lblStatusBar.setText("Le poids doit être un nombre valide.");
            lblStatusBar.setStyle("-fx-text-fill: red;");
        }
    }
}

