package ch.makery.tri.controller;

import ch.makery.tri.model.Contrat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ContratCommerceController {

    @FXML private Label dateDebutLabel;
    @FXML private Label dateFinLabel;
    @FXML private ListView<String> categoriesListView;

    /**
     * Cette méthode peut être appelée pour afficher un contrat existant.
     */
    public void afficherContrat(String dateDebut, String dateFin, List<String> categories) {
        dateDebutLabel.setText(dateDebut);
        dateFinLabel.setText(dateFin);
        categoriesListView.getItems().setAll(categories);
    }

    @FXML
    private void initialize() {
        Contrat contrat = new Contrat(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 7, 1),
                Arrays.asList("Électronique", "Textile", "Papier")
        );

        dateDebutLabel.setText(contrat.getDateDebut().toString());
        dateFinLabel.setText(contrat.getDateFin().toString());
        categoriesListView.getItems().setAll(contrat.getListeCatProduits());
    }
}
