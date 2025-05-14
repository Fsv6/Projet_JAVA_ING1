package ch.makery.address;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class mainPoubelle extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
        	// Charger le fichier FXML de la page ProfilGeneral.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ProfilGeneral.fxml"));
            Parent root = loader.load();

            // Créer une scène avec le root (l'interface FXML) et l'afficher
            Scene scene = new Scene(root);

            // Définir le titre de la fenêtre
            primaryStage.setTitle("Profil Général");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

