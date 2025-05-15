package tri.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class HistoriqueDetailsController implements Initializable {

    @FXML private Label lblTitre;
    @FXML private Label lblEntete;
    @FXML private Label lblIdentifiant;
    @FXML private Label lblMontant;
    @FXML private Label lblCommerce;
    @FXML private Label lblDate;
    
    private BonAchatController.HistoriqueUtilisation historique;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization will be done when setHistorique is called
    }
    
    public void setHistorique(BonAchatController.HistoriqueUtilisation historique) {
        this.historique = historique;
        updateUI();
    }
    
    private void updateUI() {
        if (historique != null) {
            lblEntete.setText("Bon d'achat #" + historique.getIdBon() + " - " + historique.getMontant() + "€");
            lblIdentifiant.setText(String.valueOf(historique.getIdBon()));
            lblMontant.setText(historique.getMontant() + "€");
            lblCommerce.setText(historique.getCommerce());
            lblDate.setText(historique.getDateUtilisationFormatted());
        }
    }
}