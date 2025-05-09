package tri.test.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestBonAchatInterface extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de l'interface FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ressources/fxml/BonAchatView.fxml"));
        Parent root = loader.load();
        
        // Configuration de la scène
        Scene scene = new Scene(root);
        
        // Configuration de la fenêtre
        primaryStage.setTitle("Test Interface Bon d'Achat");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}