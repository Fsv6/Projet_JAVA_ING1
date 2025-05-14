package ch.makery.address.model;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ProfilGeneralController {

    @FXML
    private Button btnPoubelles;  // Bouton "Historique"
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnFaireDepot;
    @FXML
    private Button btnBonsAchat;
    @FXML
    private Button btnCommerces;
    @FXML
    private Button btnDeconnexion;

    @FXML
    public void initialize() {
        // Lier le bouton "Historique" à l'action qui ouvre la page HistoriqueDepots
        btnPoubelles.setOnAction(e -> openHistoriqueDepots());
        btnFaireDepot.setOnAction(e-> openDepots());

        // Si tu souhaites ajouter d'autres actions pour les autres boutons, tu peux les ajouter ici
        btnAccueil.setOnAction(e -> openPageAccueil());
        btnBonsAchat.setOnAction(e -> openBonsAchat());
        btnCommerces.setOnAction(e -> openCommerces());
        btnDeconnexion.setOnAction(e -> deconnexion());
    }

    // Méthode pour ouvrir la page HistoriqueDepots.fxml
    private void openHistoriqueDepots() {
        try {
            // Charger la page HistoriqueDepots.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/HistoriqueCompte.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et l'afficher dans une nouvelle fenêtre (stage)
            Stage stage = new Stage();
            stage.setTitle("Historique des Dépôts");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openDepots() {
        try {
            // Charger la page Depots.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/ListePoubelle.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et l'afficher dans une nouvelle fenêtre (stage)
            Stage stage = new Stage();
            stage.setTitle("Faire un depot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthodes pour ouvrir d'autres pages (ajoute-les selon tes besoins)

    private void openPageAccueil() {
        // Code pour ouvrir la page d'accueil
    }

    private void openBonsAchat() {
        // Code pour ouvrir la page des bons d'achat
    }

    private void openCommerces() {
        // Code pour ouvrir la page des commerces
    }

    private void deconnexion() {
        // Code pour gérer la déconnexion
    }
}

