package ch.makery.address.model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnexionController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    // Méthode appelée lors du clic sur le bouton "Se connecter"
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Ici tu peux insérer la logique de vérification réelle (ex: avec une base de données)
        if (email.equals("admin@example.com") && password.equals("password")) {
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue, " + email + " !");
            // Rediriger vers une autre page ici si nécessaire
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de la connexion", "Identifiants incorrects.");
        }
    }

    // Méthode appelée lors du clic sur le bouton "S'inscrire"
    @FXML
    private void handleSignUp(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Redirection", "Aller à la page d'inscription...");
        // Tu peux insérer ici un changement de scène (vers la page Register.fxml par exemple)
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

