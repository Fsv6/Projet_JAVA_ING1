package tri.main; // Mets ton vrai nom de package

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chemin vers ton fichier FXML (doit être dans resources ou src/main/resources si Maven/Gradle)
            Parent root = FXMLLoader.load(getClass().getResource("/accueil.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Centre de Tri - Application");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Démarre l'application JavaFX
    }
}
