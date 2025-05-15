package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tri.logic.*;
import tri.dao.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class CentreTriController {

    // === BOUTONS PRINCIPAUX ===
    @FXML private Button deconnexion;
    @FXML private Button placerpoubelles;
    @FXML private Button créer_contrat;
    @FXML private Button stats;
    @FXML private Button poubellePleine;
    @FXML private Button btnRenouveler; // Correspond au fx:id dans votre FXML

    // === FORMULAIRES ===
    @FXML private VBox formPlacerPoubelle;
    @FXML private VBox formContrat;
    @FXML private VBox formStats;

    // === CHAMPS FORMULAIRES ===
    @FXML private TextField idPoubelle;
    @FXML private TextField latitude;
    @FXML private TextField longitude;
    @FXML private TextField nomQuartier;
    @FXML private TextField nomCommerce;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField fieldIdCentreTri;
    @FXML private TextField fieldIdCommerce;
    @FXML private DatePicker dateDebutContrat;
    @FXML private DatePicker dateFinContrat;
    @FXML private CheckBox catAlimentaire;
    @FXML private CheckBox catVetements;
    @FXML private CheckBox catElectronique;
    @FXML private CheckBox catCosmetiques;
    @FXML private CheckBox catMeubles;
    @FXML private CheckBox catJouets;
    @FXML private CheckBox catLivres;


    // === ZONE D'INFORMATION ===
    @FXML private TextArea zoneInfo;
    @FXML private TextArea statsArea;

    // === CONTENEURS DYNAMIQUES ===
    @FXML private VBox listePoubellesContainer;
    @FXML private VBox listeContratsContainer;

    // === DAO / LISTES ===
    private final PoubelleIntelligenteDAO poubelleDAO = new PoubelleIntelligenteDAO();
    private final ContratDAO contratDAO = new ContratDAO();
    private final BacDAO bacDAO = new BacDAO();
    private List<PoubelleIntelligente> poubelles = new ArrayList<>();

    // === INITIALISATION ===
 // Méthode pour initialiser l'interface
    @FXML
    private void initialize() {
        // Par exemple, initialiser avec une date d'expiration fictive
        LocalDate dateExpiration = LocalDate.of(2025, 5, 10); // Remplacez par la date réelle de votre contrat

        // Vérifiez si le contrat est expiré et mettez à jour le bouton
        if (estContratExpire(dateExpiration)) {
            // Si le contrat est expiré, rendez le bouton "Renouveler" rouge
            btnRenouveler.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        } else {
            // Si le contrat n'est pas expiré, rendez le bouton "Renouveler" grisé
            btnRenouveler.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        }
    }
    // === GESTION DE L'AFFICHAGE ===
    private void cacherTousLesPanneaux() {
        formPlacerPoubelle.setVisible(false);  // Masque le formulaire de placer une poubelle
        formContrat.setVisible(false);  // Masque le formulaire de création de contrat
        listePoubellesContainer.setVisible(false);  // Masque la liste des poubelles
        listeContratsContainer.setVisible(false);  // Masque la liste des contrats
        formStats.setVisible(false);  // Masque le formulaire de statistiques
    }


    // === ACTIONS DES BOUTONS ===
    @FXML
    private void handleDeconnexion() {
        zoneInfo.setText("Déconnexion en cours...");
        cacherTousLesPanneaux();
    }

    @FXML
    private void handlePlacerPoubelle() {
        cacherTousLesPanneaux();
        formPlacerPoubelle.setVisible(true);
        zoneInfo.clear();
    }
    
    @FXML
    private void handleAfficherFormContrat() {
        formContrat.setVisible(true);

        // Cacher les autres vues
        formPlacerPoubelle.setVisible(false);
        formStats.setVisible(false);
        listePoubellesContainer.setVisible(false);
        listeContratsContainer.setVisible(false);
    }

    @FXML
    private void handleCreerContrat() {
        try {
            String centreText = fieldIdCentreTri.getText().trim();
            String commerceText = fieldIdCommerce.getText().trim();

            if (centreText.isEmpty() || commerceText.isEmpty()) {
                zoneInfo.setText("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            if (!centreText.matches("\\d+") || !commerceText.matches("\\d+")) {
                zoneInfo.setText("Erreur : les IDs doivent être des entiers positifs.");
                return;
            }

            int idCentreTri = Integer.parseInt(centreText);
            int idCommerce = Integer.parseInt(commerceText);
            LocalDate dateDebut = dateDebutContrat.getValue();
            LocalDate dateFin = dateFinContrat.getValue();

            if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut)) {
                zoneInfo.setText("Erreur : dates invalides (début ou fin manquante ou incohérente).");
                return;
            }

            List<String> categories = new ArrayList<>();
            if (catAlimentaire.isSelected()) categories.add("Alimentaire");
            if (catCosmetiques.isSelected()) categories.add("Cosmétiques");
            if (catVetements.isSelected()) categories.add("Vêtements");
            if (catElectronique.isSelected()) categories.add("Électronique");
            if (catMeubles.isSelected()) categories.add("Meubles");
            if (catJouets.isSelected()) categories.add("Jouets");
            if (catLivres.isSelected()) categories.add("Livres");

            if (categories.isEmpty()) {
                zoneInfo.setText("Veuillez sélectionner au moins une catégorie de produits.");
                return;
            }

            Contrat contrat = new Contrat(idCentreTri, idCommerce, dateDebut, dateFin, categories);
            contratDAO.insertContrat(contrat, idCentreTri, idCommerce);

            zoneInfo.setText("Contrat créé avec succès !");
            formContrat.setVisible(false);

        } catch (Exception e) {
            zoneInfo.setText("Erreur lors de la création du contrat : " + e.getMessage());
        }
    }



    @FXML
    private void handleListePoubelles() {
        cacherTousLesPanneaux();
        chargerListePoubelles();
        listePoubellesContainer.setVisible(true);
        zoneInfo.setText("Liste des poubelles chargée.");
    }
    
    // Méthode d'action pour le bouton "Renouveler"
    @FXML
    private void handleRenouvelerContrat() {
        // Logique pour renouveler le contrat
        System.out.println("Le contrat a été renouvelé !");
    }
    
 // Méthode pour vérifier si le contrat est expiré
    private boolean estContratExpire(LocalDate dateExpiration) {
        LocalDate today = LocalDate.now();
        return dateExpiration.isBefore(today); // Si la date d'expiration est avant aujourd'hui, le contrat est expiré
    }

    @FXML
    private void handleListeContrats() {
        cacherTousLesPanneaux();
        chargerListeContrats();
        listeContratsContainer.setVisible(true);
        zoneInfo.setText("Liste des contrats chargée.");
    }

    // === FORMULAIRES ===
    @FXML
    private void handleValiderPoubelle() {
        try {
            String idText = idPoubelle.getText();
            String nomQuartierText = nomQuartier.getText();
            String latitudeText = latitude.getText();
            String longitudeText = longitude.getText();

            if (idText.isEmpty() || nomQuartierText.isEmpty() || latitudeText.isEmpty() || longitudeText.isEmpty()) {
                zoneInfo.setText("Veuillez remplir tous les champs.");
                return;
            }

            int id = Integer.parseInt(idText);
            float latitudeValue = Float.parseFloat(latitudeText);
            float longitudeValue = Float.parseFloat(longitudeText);

            // Créer l'objet poubelle
            PoubelleIntelligente poubelle = new PoubelleIntelligente(id, nomQuartierText, latitudeValue, longitudeValue);

            // 1. Insérer la poubelle dans la base
            poubelleDAO.insertPoubelle(poubelle, 1); // ID fixe du centre de tri

            // 2. Créer les 4 bacs associés à cette poubelle
            BacDAO bacDAO = new BacDAO();  // Assure-toi que ce DAO est instancié
            List<TypeDechet> types = Arrays.asList(
            	    TypeDechet.PAPIER,
            	    TypeDechet.VERRE,
            	    TypeDechet.METAL,
            	    TypeDechet.CARTON
            	);
            for (TypeDechet type : types) {
                Bac bac = new Bac(0, 100, types);  // Un seul type de déchet par bac
                bac.setPoidsActuel(0);

                bacDAO.insertBac(bac, id); // 'id' est l’ID de la poubelle intelligente
            }

            // Message de succès
            zoneInfo.setText("Poubelle et ses bacs ajoutés avec succès.");

            // Réinitialisation des champs
            idPoubelle.clear();
            nomQuartier.clear();
            latitude.clear();
            longitude.clear();

            formPlacerPoubelle.setVisible(false);

        } catch (NumberFormatException e) {
            zoneInfo.setText("Erreur : veuillez entrer des nombres valides pour l'ID, la latitude et la longitude.");
        } catch (Exception e) {
            zoneInfo.setText("Erreur lors de l'ajout de la poubelle : " + e.getMessage());
        }
    }



    @FXML
    private void handleValiderContrat() {
        String commerce = nomCommerce.getText();
        var debut = dateDebut.getValue();
        var fin = dateFin.getValue();

        if (commerce.isEmpty() || debut == null || fin == null) {
            zoneInfo.setText("Veuillez remplir tous les champs du contrat.");
            return;
        }
        
     // Vérifier que la date de début n'est pas dans le passé
        if (debut.isBefore(LocalDate.now())) {
            zoneInfo.setText("La date de début ne peut pas être antérieure à aujourd'hui.");
            return;
        }

        // Vérifier que la date de fin est après la date de début
        if (fin.isBefore(debut)) {
            zoneInfo.setText("La date de fin doit être postérieure à la date de début.");
            return;
        }

        // Enregistrer le contrat (ajoute logique ici)
        zoneInfo.setText("Contrat créé pour '" + commerce + "' du " + debut + " au " + fin);
        nomCommerce.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        formContrat.setVisible(false);
    }
    
    @FXML
    private void handleAfficherStats() {
        cacherTousLesPanneaux();  // Masque les autres panneaux
        formStats.setVisible(true);  // Affiche la section des statistiques
        statsArea.setText("Chargement des statistiques...\n\n");

        // Récupérer la liste des poubelles et des contrats
        List<PoubelleIntelligente> poubelles = poubelleDAO.getAllPoubelles();
        List<Contrat> contrats = contratDAO.getAllContrats();

        // Calculer le nombre total de poubelles
        int totalPoubelles = poubelles.size();

        // Calculer le nombre de poubelles pleines
        long poubellesPleinCount = poubelles.stream().filter(PoubelleIntelligente::estPleine).count();

        // Calculer le pourcentage de poubelles pleines
        double pourcentagePoubellesPleine = totalPoubelles > 0 ? (double) poubellesPleinCount / totalPoubelles * 100 : 0;

        

        // Afficher les résultats dans le TextArea
        String statistiques = "Statistiques du Centre de Tri\n\n";
        statistiques += "Nombre total de poubelles : " + totalPoubelles + "\n";
        statistiques += "Pourcentage de poubelles pleines : " + String.format("%.2f", pourcentagePoubellesPleine) + "%\n";
        

        statsArea.setText(statistiques);
    }


    // === AFFICHAGE DYNAMIQUE ===
    private void chargerListePoubelles() {
        listePoubellesContainer.getChildren().clear();
        List<PoubelleIntelligente> poubelles = poubelleDAO.getAllPoubelles();
        System.out.println("Nombre de poubelles récupérées : " + poubelles.size());
        for (PoubelleIntelligente p : poubelles) {
            VBox vbox = new VBox(5);
            vbox.setStyle("-fx-padding: 10; -fx-background-color: #ecf0f1;");

            Label label = new Label("Poubelle: " + p.getNomQuartier());

            Button supprimer = new Button("Supprimer");
            supprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            supprimer.setOnAction(e -> supprimerPoubelle(p));

            Button vider = new Button("Vider");
         // Style par défaut : gris et désactivé
            vider.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
            vider.setDisable(true); // désactivé
            
         // Mettons à jour dynamiquement selon l'état de la poubelle
            if (p.estPleine()) {
                vider.setDisable(false); // bouton activé
                vider.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"); // bleu
             // Ajouter l'action pour vider la poubelle
                vider.setOnAction(e -> viderPoubelle(p));
            } else {
                vider.setDisable(true); // bouton désactivé
                vider.setStyle("-fx-background-color: grey; -fx-text-fill: white;"); // gris
            }

            vbox.getChildren().addAll(label, supprimer, vider);
            listePoubellesContainer.getChildren().add(vbox);
        }
    }


    private void chargerListeContrats() {
        listeContratsContainer.getChildren().clear();
        List<Contrat> contrats = contratDAO.getAllContrats();

        for (Contrat c : contrats) {
            VBox vbox = new VBox(5);
            vbox.setStyle("-fx-padding: 10; -fx-background-color: #ecf0f1;");

            Label label = new Label("Contrat pour " + c.getIdCommerce() + " du " + c.getDateDebut() + " au " + c.getDateFin());

            Button supprimer = new Button("Supprimer");
            supprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            supprimer.setOnAction(e -> supprimerContrat(c));

            vbox.getChildren().addAll(label, supprimer);
            listeContratsContainer.getChildren().add(vbox);
            System.out.println("Suppression -> centreTri: " + c.getIdCentreTri() + ", commerce: " + c.getIdCommerce());

            System.out.println("Contrats après suppression: " + contrats.size());

        }
    }

   

    // === ACTIONS SPÉCIFIQUES ===
    private void viderPoubelle(PoubelleIntelligente p) {
        float poidsTotal = 0;

        System.out.println("=== Vidage de la poubelle ID: " + p.getId() + " ===");
        System.out.println("Liste des bacs associés :");

        for (Bac bac : p.getBacs()) {
            System.out.println(" - Bac ID: " + bac.getIdBac() + ", poids: " + bac.getPoidsActuel() + " kg");
            poidsTotal += bac.getPoidsActuel(); // cumul du poids de chaque bac
            p.vider(); // vide tous les bacs
            bacDAO.viderBac(bac.getIdBac()); // en base
        }

        
        chargerListePoubelles(); // rafraîchir l'IHM

        String message = "Poubelle #" + p.getId() + " vidée. Poids total avant vidage : " + poidsTotal + " kg.";
        System.out.println(message); // log console
        zoneInfo.setText(message);   // log IHM
    }


    private void supprimerPoubelle(PoubelleIntelligente p) {
        try {
            poubelleDAO.deletePoubelle(p.getId());
            chargerListePoubelles(); // Refresh
            zoneInfo.setText("Poubelle supprimée.");
        } catch (Exception e) {
            zoneInfo.setText("Erreur suppression : " + e.getMessage());
        }
    }

    private void supprimerContrat(Contrat c) {
        try {
            contratDAO.deleteContrat(c.getIdCentreTri(), c.getIdCommerce());
            chargerListeContrats(); // Refresh
            zoneInfo.setText("Contrat supprimé.");
        } catch (Exception e) {
            zoneInfo.setText("Erreur suppression contrat : " + e.getMessage());
        }
    }
    
    
}
