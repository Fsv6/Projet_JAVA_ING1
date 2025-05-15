package tri.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import tri.dao.*;
import tri.logic.*;
import tri.services.BonAchatService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BonAchatController implements Initializable {

    // Éléments de l'interface - Communs
    @FXML private Label lblPointsDisponibles;
    @FXML private Label lblStatusBar;
    
    // Éléments de l'interface - Onglet 1 (Obtenir des bons)
    @FXML private Button btn5euros, btn15euros, btn30euros;
    @FXML private Text txtDescription;
    
    // Éléments de l'interface - Onglet 2 (Mes bons disponibles)
    @FXML private TableView<BonAchat> tableBonsAchat;
    @FXML private TableColumn<BonAchat, Integer> colId;
    @FXML private TableColumn<BonAchat, Integer> colMontant;
    @FXML private TableColumn<BonAchat, String> colDateCreation;
    @FXML private TableColumn<BonAchat, String> colStatus;
    
    // Éléments de l'interface - Onglet 3 (Historique)
    @FXML private TableView<HistoriqueUtilisation> tableHistorique;
    @FXML private TableColumn<HistoriqueUtilisation, String> colHistId;
    @FXML private TableColumn<HistoriqueUtilisation, String> colHistMontant;
    @FXML private TableColumn<HistoriqueUtilisation, String> colHistCommerce;
    @FXML private TableColumn<HistoriqueUtilisation, String> colHistDate;
    @FXML private TableColumn<HistoriqueUtilisation, Void> colHistDetail;
    @FXML private ComboBox<String> cmbFiltrePeriode;
    @FXML private ComboBox<String> cmbFiltreCommerce;
    @FXML private Button btnExportHistorique;
    
    // Boutons du menu latéral
    @FXML private Button btnAccueil, btnPoubelles, btnDepots, btnBonsAchat, btnCommerces, btnDeconnexion;
    
    // Services et DAOs
    private final BonAchatService bonAchatService = new BonAchatService();
    private final BonAchatDAO bonAchatDAO = new BonAchatDAO();
    private final CompteDAO compteDAO = new CompteDAO();
    private final CommerceDAO commerceDAO = new CommerceDAO();
    private final ConvertirDAO convertirDAO = new ConvertirDAO();
    private final UtiliserDAO utiliserDAO = new UtiliserDAO();
    
    // Variables de données
    private Compte compteCourant;
    private ObservableList<BonAchat> bonsAchatList;
    private ObservableList<HistoriqueUtilisation> historiqueList;
    private FilteredList<HistoriqueUtilisation> historiqueFiltre;
    
    // Constantes pour les valeurs des bons et coûts
    private static final int COUT_BON_5_EUROS = 500;
    private static final int COUT_BON_15_EUROS = 1400;
    private static final int COUT_BON_30_EUROS = 2700;
    
    private static final String[] PERIODES = {
        "Toutes les périodes",
        "Aujourd'hui",
        "Cette semaine",
        "Ce mois-ci",
        "Cette année"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents();
        loadCompteCourant();
        setupUI();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        bonsAchatList = FXCollections.observableArrayList();
        historiqueList = FXCollections.observableArrayList();
    }
    
    private void loadCompteCourant() {
        // TODO: Remplacer par une vraie session utilisateur
        compteCourant = getCompteConnecte();
        lblStatusBar.setText("Connecté en tant que: " + compteCourant.getNom() + " " + compteCourant.getPrenom());
    }
    
    private void setupUI() {
        updatePointsAffichage();
        setupTables();
        setupFilters();
    }
    
    private void setupEventHandlers() {
        // Boutons de création de bons
        btn5euros.setOnAction(e -> creerBonAchat(5, COUT_BON_5_EUROS));
        btn15euros.setOnAction(e -> creerBonAchat(15, COUT_BON_15_EUROS));
        btn30euros.setOnAction(e -> creerBonAchat(30, COUT_BON_30_EUROS));
        
        // Autres boutons
        btnExportHistorique.setOnAction(e -> exporterHistorique());
        
        // Navigation
        btnAccueil.setOnAction(e -> naviguerVers("Accueil"));
        btnPoubelles.setOnAction(e -> naviguerVers("Poubelles"));
        btnDepots.setOnAction(e -> naviguerVers("Depots"));
        btnCommerces.setOnAction(e -> naviguerVers("Commerces"));
        btnDeconnexion.setOnAction(e -> deconnecter());
    }
    
    private void loadData() {
        chargerBonsAchat();
        chargerHistorique();
    }
    
    private void refreshData() {
        chargerBonsAchat();
        updatePointsAffichage();
        chargerHistorique();
    }
    
    private Compte getCompteConnecte() {
        // TODO: Implémenter un vrai système de session
        try {
            return compteDAO.getCompteById(1);
        } catch (Exception e) {
            return new Compte(1, "Dupont", "Jean", 1500);
        }
    }
    
    private void updatePointsAffichage() {
        int points = compteCourant.getNbPointsFidelite();
        lblPointsDisponibles.setText(String.valueOf(points));
        
        btn5euros.setDisable(points < COUT_BON_5_EUROS);
        btn15euros.setDisable(points < COUT_BON_15_EUROS);
        btn30euros.setDisable(points < COUT_BON_30_EUROS);
    }
    
    private void setupTables() {
        setupBonsAchatTable();
        setupHistoriqueTable();
    }
    
    private void setupBonsAchatTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDateCreation.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colStatus.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("Disponible"));
        
        tableBonsAchat.setItems(bonsAchatList);
    }
    
    private void setupHistoriqueTable() {
        // Utiliser des chaînes de caractères au lieu d'entiers pour éviter les problèmes de conversion
        colHistId.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null) {
                int id = cellData.getValue().getIdBon();
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(id));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colHistMontant.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null) {
                int montant = cellData.getValue().getMontant();
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(montant) + "€");
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colHistCommerce.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null) {
                String commerce = cellData.getValue().getCommerce();
                return new javafx.beans.property.SimpleStringProperty(commerce);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colHistDate.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getDateUtilisation() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateUtilisationFormatted());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        colHistDetail.setCellFactory(createDetailButtonCellFactory());
        
        historiqueFiltre = new FilteredList<>(historiqueList, p -> true);
        tableHistorique.setItems(historiqueFiltre);
    }
    
    private void setupFilters() {
        // Configuration des filtres
        cmbFiltrePeriode.setItems(FXCollections.observableArrayList(PERIODES));
        cmbFiltrePeriode.getSelectionModel().selectFirst();
        
        // Chargement des commerces disponibles
        List<String> commerceNames = chargerCommerces();
        cmbFiltreCommerce.setItems(FXCollections.observableArrayList(commerceNames));
        cmbFiltreCommerce.getSelectionModel().selectFirst();
        
        // Listeners pour les filtres
        cmbFiltrePeriode.setOnAction(e -> appliquerFiltres());
        cmbFiltreCommerce.setOnAction(e -> appliquerFiltres());
    }
    
    private List<String> chargerCommerces() {
        List<String> commerceNames = new ArrayList<>();
        commerceNames.add("Tous les commerces");
        
        try {
            // Récupération des commerces depuis l'historique
            List<HistoriqueUtilisation> historique = chargerHistoriqueBD();
            for (HistoriqueUtilisation hist : historique) {
                if (hist != null && hist.getCommerce() != null) {
                    String commerce = hist.getCommerce();
                    if (!commerceNames.contains(commerce)) {
                        commerceNames.add(commerce);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des commerces : " + e.getMessage());
        }
        
        // Ajout de quelques commerces par défaut si la liste est vide
        if (commerceNames.size() <= 1) {
            commerceNames.add("Carrefour");
            commerceNames.add("Auchan");
            commerceNames.add("Leclerc");
            commerceNames.add("Boutique Bio");
        }
        
        return commerceNames;
    }
    
    private void appliquerFiltres() {
        String periode = cmbFiltrePeriode.getValue();
        String commerce = cmbFiltreCommerce.getValue();
        
        historiqueFiltre.setPredicate(historique -> {
            if (historique == null) return false;
            
            boolean matchPeriode = filtrerParPeriode(historique, periode);
            boolean matchCommerce = filtrerParCommerce(historique, commerce);
            return matchPeriode && matchCommerce;
        });
    }
    
    private boolean filtrerParPeriode(HistoriqueUtilisation historique, String periode) {
        if ("Toutes les périodes".equals(periode)) {
            return true;
        }
        
        if (historique.getDateUtilisation() == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = historique.getDateUtilisation();
        
        switch (periode) {
            case "Aujourd'hui":
                return date.toLocalDate().equals(now.toLocalDate());
            case "Cette semaine":
                return date.isAfter(now.minusDays(7));
            case "Ce mois-ci":
                return date.getMonth() == now.getMonth() && date.getYear() == now.getYear();
            case "Cette année":
                return date.getYear() == now.getYear();
            default:
                return true;
        }
    }
    
    private boolean filtrerParCommerce(HistoriqueUtilisation historique, String commerce) {
        if (commerce == null || "Tous les commerces".equals(commerce)) {
            return true;
        }
        
        return historique.getCommerce() != null && historique.getCommerce().equals(commerce);
    }
    
    private void creerBonAchat(int montant, int coutPoints) {
        if (!verifierPointsSuffisants(coutPoints)) {
            return;
        }
        
        if (!confirmerCreation(montant, coutPoints)) {
            return;
        }
        
        try {
            BonAchat nouveauBon = new BonAchat(montant);
            bonAchatDAO.insertBonAchat(nouveauBon);
            convertirDAO.insertConversion(compteCourant.getId(), nouveauBon.getId());
            
            mettreAJourPoints(coutPoints);
            refreshData(); // Refresh both bons and historique
            
            showSuccess("Bon créé avec succès", 
                "Votre bon d'achat de " + montant + "€ a été créé avec succès (ID: " + nouveauBon.getId() + ").");
        } catch (Exception e) {
            showError("Erreur", "Une erreur est survenue lors de la création du bon d'achat : " + e.getMessage());
        }
    }
    
    private boolean verifierPointsSuffisants(int coutPoints) {
        if (compteCourant.getNbPointsFidelite() < coutPoints) {
            showWarning("Points insuffisants", 
                "Vous n'avez pas assez de points pour créer ce bon d'achat.");
            return false;
        }
        return true;
    }
    
    private boolean confirmerCreation(int montant, int coutPoints) {
        Optional<ButtonType> result = showConfirmation(
            "Créer un bon d'achat",
            "Vous êtes sur le point de créer un bon d'achat de " + montant + "€ pour " + coutPoints + " points.",
            "Voulez-vous continuer ?"
        );
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    private void mettreAJourPoints(int coutPoints) {
        int nouveauxPoints = compteCourant.getNbPointsFidelite() - coutPoints;
        compteDAO.updatePointsFidelite(compteCourant.getId(), nouveauxPoints);
        compteCourant.setNbPointsFidelite(nouveauxPoints);
    }
    
    private void chargerBonsAchat() {
        try {
            List<Integer> bonIds = convertirDAO.getBonAchatsByCompte(compteCourant.getId());
            List<BonAchat> bons = new ArrayList<>();
            
            for (Integer id : bonIds) {
                try {
                    BonAchat bon = bonAchatDAO.getBonAchatById(id);
                    bons.add(bon);
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du bon " + id + " : " + e.getMessage());
                }
            }
            
            bonsAchatList.clear();
            bonsAchatList.addAll(bons);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des bons d'achat : " + e.getMessage());
            // Ne pas afficher d'alerte pour éviter de bloquer l'interface
        }
    }
    
    private void chargerHistorique() {
        try {
            historiqueList.clear();
            
            // Chargement de l'historique
            List<HistoriqueUtilisation> historique = chargerHistoriqueBD();
            historiqueList.addAll(historique);
            
            appliquerFiltres();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'historique : " + e.getMessage());
            // Ne pas afficher d'alerte pour éviter de bloquer l'interface
        }
    }
    
    private List<HistoriqueUtilisation> chargerHistoriqueBD() {
        List<HistoriqueUtilisation> historique = new ArrayList<>();
        
        try {
            // Essayons de créer la table 'utiliser' s'il n'existe pas encore
            // Ceci est une mesure temporaire - en production, les tables devraient être créées par un script SQL
            try {
                createUtiliserTableIfNotExists();
            } catch (Exception e) {
                System.err.println("Attention: Impossible de créer la table 'utiliser': " + e.getMessage());
            }
            
            // Parcourir tous les bons d'achat
            List<BonAchat> tousBons = bonAchatDAO.getAllBonAchats();
            
            for (BonAchat bon : tousBons) {
                List<Integer> commerceIds = new ArrayList<>();
                try {
                    // Si la table 'utiliser' n'existe pas encore, cette opération échouera silencieusement
                    commerceIds = utiliserDAO.getCommercesByBonAchat(bon.getId());
                } catch (Exception e) {
                    // Ignorer l'erreur et continuer
                    System.err.println("Erreur lors de la récupération des commerces pour le bon " + bon.getId() + ": " + e.getMessage());
                }
                
                for (Integer commerceId : commerceIds) {
                    try {
                        // Récupérer le commerce
                        Commerce commerce = commerceDAO.getCommerceById(commerceId);
                        
                        // Créer l'entrée historique
                        HistoriqueUtilisation hist = new HistoriqueUtilisation(
                            bon.getId(),
                            bon.getMontant(),
                            commerce.getNom(),
                            LocalDateTime.now().minusDays((int)(Math.random() * 30)) // Date fictive - à remplacer par la vraie date
                        );
                        historique.add(hist);
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la récupération du commerce " + commerceId + " : " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'historique : " + e.getMessage());
        }
        
        // Si aucun historique n'a été trouvé, ajouter des exemples
        if (historique.isEmpty()) {
            historique.add(new HistoriqueUtilisation(1, 5, "Carrefour", LocalDateTime.now().minusDays(2)));
            historique.add(new HistoriqueUtilisation(2, 15, "Auchan", LocalDateTime.now().minusDays(5)));
            historique.add(new HistoriqueUtilisation(3, 30, "Leclerc", LocalDateTime.now().minusDays(10)));
        }
        
        return historique;
    }
    
    // Méthode pour créer la table 'utiliser' si elle n'existe pas
    private void createUtiliserTableIfNotExists() throws Exception {
        try (java.sql.Connection conn = tri.utils.DatabaseConnection.getConnection();
             java.sql.Statement st = conn.createStatement()) {
            
            // Vérifier si la table existe déjà
            try {
                st.executeQuery("SELECT 1 FROM utiliser LIMIT 1");
                // Si on arrive ici, la table existe déjà
                return;
            } catch (Exception e) {
                // La table n'existe pas, on va la créer
            }
            
            // Création de la table
            String sql = "CREATE TABLE utiliser (" +
                         "idBonAchat INT NOT NULL, " +
                         "idCommerce INT NOT NULL, " +
                         "dateUtilisation DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                         "PRIMARY KEY (idBonAchat, idCommerce))";
            st.executeUpdate(sql);
            
            System.out.println("Table 'utiliser' créée avec succès.");
        }
    }
    
    private Callback<TableColumn<HistoriqueUtilisation, Void>, TableCell<HistoriqueUtilisation, Void>> createDetailButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<HistoriqueUtilisation, Void> call(final TableColumn<HistoriqueUtilisation, Void> param) {
                return new TableCell<>() {
                    private final Button btnDetails = new Button("Détails");
                    
                    {
                        btnDetails.setOnAction(event -> {
                            HistoriqueUtilisation historique = getTableView().getItems().get(getIndex());
                            if (historique != null) {
                                ouvrirFenetreDetails(historique);
                            }
                        });
                        btnDetails.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HistoriqueUtilisation historique = getTableView().getItems().get(getIndex());
                            setGraphic(historique != null ? btnDetails : null);
                        }
                    }
                };
            }
        };
    }
    
    private void ouvrirFenetreDetails(HistoriqueUtilisation historique) {
        try {
            // Chargement du FXML pour la fenêtre de détails
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/HistoriqueDetails.fxml"));
            javafx.scene.control.DialogPane dialogPane = loader.load();
            
            // Récupération du controller et configuration des données
            HistoriqueDetailsController controller = loader.getController();
            controller.setHistorique(historique);
            
            // Création et affichage du dialogue
            javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Détails de l'utilisation");
            dialog.setDialogPane(dialogPane);
            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre de détails : " + e.getMessage());
            // Fallback sur une simple alerte
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails de l'utilisation");
            alert.setHeaderText("Bon d'achat #" + historique.getIdBon() + " - " + historique.getMontant() + "€");
            alert.setContentText("Commerce: " + historique.getCommerce() + "\nDate: " + historique.getDateUtilisationFormatted());
            alert.showAndWait();
        }
    }
    
    private void exporterHistorique() {
        if (historiqueList.isEmpty()) {
            showWarning("Historique vide", "Aucune donnée à exporter.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter l'historique");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        fileChooser.setInitialFileName("historique_bons_achat_" + 
                                      LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv");
        
        File file = fileChooser.showSaveDialog(btnExportHistorique.getScene().getWindow());
        
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("ID Bon;Montant;Commerce;Date d'utilisation\n");
                
                for (HistoriqueUtilisation historique : tableHistorique.getItems()) {
                    if (historique != null) {
                        writer.write(String.format("%d;%d;%s;%s\n",
                            historique.getIdBon(),
                            historique.getMontant(),
                            historique.getCommerce(),
                            historique.getDateUtilisationFormatted()
                        ));
                    }
                }
                
                showSuccess("Export réussi", 
                    "L'historique a été exporté avec succès dans le fichier :\n" + file.getAbsolutePath());
            } catch (IOException e) {
                showError("Erreur d'export", "Impossible d'exporter l'historique : " + e.getMessage());
            }
        }
    }
    
    private void naviguerVers(String destination) {
        // TODO: Implémenter la navigation
        System.out.println("Navigation vers: " + destination);
    }
    
    private void deconnecter() {
        // TODO: Implémenter la déconnexion
        System.out.println("Déconnexion...");
    }
    
    // Méthodes utilitaires pour les alertes
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.INFORMATION);
    }
    
    private void showError(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.ERROR);
    }
    
    private void showWarning(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.WARNING);
    }
    
    private void showInfo(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.INFORMATION);
    }
    
    private Optional<ButtonType> showConfirmation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        return alert.showAndWait();
    }
    
    // Classe interne pour l'historique
    public static class HistoriqueUtilisation {
        private int idBon;
        private int montant;
        private String commerce;
        private LocalDateTime dateUtilisation;
        
        public HistoriqueUtilisation(int idBon, int montant, String commerce, LocalDateTime dateUtilisation) {
            this.idBon = idBon;
            this.montant = montant;
            this.commerce = commerce;
            this.dateUtilisation = dateUtilisation;
        }
        
        public int getIdBon() { return idBon; }
        public int getMontant() { return montant; }
        public String getCommerce() { return commerce; }
        public LocalDateTime getDateUtilisation() { return dateUtilisation; }
        public String getDateUtilisationFormatted() {
            return dateUtilisation.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
    }
}