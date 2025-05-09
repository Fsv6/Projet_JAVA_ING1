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
import tri.dao.BonAchatDAO;
import tri.dao.CommerceDAO;
import tri.dao.CompteDAO;
import tri.dao.ConvertirDAO;
import tri.dao.UtiliserDAO;
import tri.logic.BonAchat;
import tri.logic.Commerce;
import tri.logic.Compte;
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
    @FXML
    private Label lblPointsDisponibles;
    
    @FXML
    private Label lblStatusBar;
    
    // Éléments de l'interface - Onglet 1 (Obtenir des bons)
    @FXML
    private Button btn5euros, btn15euros, btn30euros;
    
    @FXML
    private Text txtDescription;
    
    // Éléments de l'interface - Onglet 2 (Mes bons disponibles)
    @FXML
    private TableView<BonAchat> tableBonsAchat;
    
    @FXML
    private TableColumn<BonAchat, Integer> colId;
    
    @FXML
    private TableColumn<BonAchat, Integer> colMontant;
    
    @FXML
    private TableColumn<BonAchat, String> colDateCreation;
    
    @FXML
    private TableColumn<BonAchat, String> colStatus;
    
    @FXML
    private TableColumn<BonAchat, Void> colActions;
    
    @FXML
    private Button btnActualiser;
    
    // Éléments de l'interface - Onglet 3 (Historique)
    @FXML
    private TableView<HistoriqueUtilisation> tableHistorique;
    
    @FXML
    private TableColumn<HistoriqueUtilisation, Integer> colHistId;
    
    @FXML
    private TableColumn<HistoriqueUtilisation, Integer> colHistMontant;
    
    @FXML
    private TableColumn<HistoriqueUtilisation, String> colHistCommerce;
    
    @FXML
    private TableColumn<HistoriqueUtilisation, String> colHistDate;
    
    @FXML
    private TableColumn<HistoriqueUtilisation, Void> colHistDetail;
    
    @FXML
    private ComboBox<String> cmbFiltrePeriode;
    
    @FXML
    private ComboBox<String> cmbFiltreCommerce;
    
    @FXML
    private Button btnExportHistorique;
    
    // Boutons du menu latéral
    @FXML
    private Button btnAccueil, btnPoubelles, btnDepots, btnBonsAchat, btnCommerces, btnDeconnexion;
    
    // Services et DAOs
    private BonAchatService bonAchatService;
    private BonAchatDAO bonAchatDAO;
    private CompteDAO compteDAO;
    private CommerceDAO commerceDAO;
    private ConvertirDAO convertirDAO;
    private UtiliserDAO utiliserDAO;
    
    // Variables de données
    private Compte compteCourant;
    private ObservableList<BonAchat> bonsAchatList;
    private ObservableList<HistoriqueUtilisation> historiqueList;
    private FilteredList<HistoriqueUtilisation> historiqueFiltre;
    
    // Valeurs constantes
    private static final int COUT_BON_5_EUROS = 500;
    private static final int COUT_BON_15_EUROS = 1400;
    private static final int COUT_BON_30_EUROS = 2700;
    
    // Classe pour représenter l'historique d'utilisation
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des services et DAOs
        initializeServices();
        
        // Récupération de l'utilisateur courant (simulé pour le moment)
        compteCourant = getCompteConnecte();
        lblStatusBar.setText("Connecté en tant que: " + compteCourant.getNom() + " " + compteCourant.getPrenom());
        
        // Affichage des points de fidélité
        updatePointsAffichage();
        
        // Configuration des tableaux
        setupBonsAchatTable();
        setupHistoriqueTable();
        
        // Configuration des filtres d'historique
        setupHistoriqueFilters();
        
        // Chargement des données
        chargerBonsAchat();
        chargerHistorique();
        
        // Configuration des événements des boutons
        setupButtonActions();
    }
    
    private void initializeServices() {
        // Initialisation des services et DAOs
        bonAchatService = new BonAchatService();
        bonAchatDAO = new BonAchatDAO();
        compteDAO = new CompteDAO();
        commerceDAO = new CommerceDAO();
        convertirDAO = new ConvertirDAO();
        utiliserDAO = new UtiliserDAO();
        
        // Initialisation des listes observables
        bonsAchatList = FXCollections.observableArrayList();
        historiqueList = FXCollections.observableArrayList();
    }
    
    private Compte getCompteConnecte() {
        // Dans un cas réel, récupérez l'utilisateur depuis une session
        // Pour le moment, on simule un utilisateur pour les tests
        try {
            return compteDAO.getCompteById(1); // ID du compte connecté (à remplacer par un système de session)
        } catch (Exception e) {
            // Si le compte n'existe pas, on crée un compte par défaut
            return new Compte(1, "Dupont", "Jean", 1500);
        }
    }
    
    private void updatePointsAffichage() {
        int points = compteCourant.getNbPointsFidelite();
        lblPointsDisponibles.setText(String.valueOf(points));
        
        // Désactiver les boutons si pas assez de points
        btn5euros.setDisable(points < COUT_BON_5_EUROS);
        btn15euros.setDisable(points < COUT_BON_15_EUROS);
        btn30euros.setDisable(points < COUT_BON_30_EUROS);
    }
    
    private void setupBonsAchatTable() {
        // Configuration des colonnes du tableau des bons d'achat
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        
        // Date de création (simulée pour le moment, à adapter si vous ajoutez cette info dans votre DB)
        colDateCreation.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        // Statut 
        colStatus.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("Disponible"));
        
        // Configuration de la colonne d'actions
        colActions.setCellFactory(createActionButtonCellFactory());
        
        // Association de la liste observable au tableau
        tableBonsAchat.setItems(bonsAchatList);
    }
    
    private void setupHistoriqueTable() {
        // Configuration des colonnes du tableau d'historique
        colHistId.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdBon()).asObject());
        
        colHistMontant.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getMontant()).asObject());
        
        colHistCommerce.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCommerce()));
        
        colHistDate.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateUtilisationFormatted()));
        
        // Configuration de la colonne de détails
        colHistDetail.setCellFactory(createDetailButtonCellFactory());
        
        // Création de la liste filtrée et association au tableau
        historiqueFiltre = new FilteredList<>(historiqueList, p -> true);
        tableHistorique.setItems(historiqueFiltre);
    }
    
    private void setupHistoriqueFilters() {
        // Configuration du filtre par période
        cmbFiltrePeriode.setItems(FXCollections.observableArrayList(
            "Toutes les périodes",
            "Aujourd'hui",
            "Cette semaine",
            "Ce mois-ci",
            "Cette année"
        ));
        cmbFiltrePeriode.getSelectionModel().selectFirst();
        
        // Configuration du filtre par commerce
        List<String> commerceNames = new ArrayList<>();
        commerceNames.add("Tous les commerces");
        
        // Essayer de récupérer les noms des commerces depuis la base de données
        try {
            // Cette méthode devrait être ajoutée à votre CommerceDAO
            // Sinon, vous pouvez ajouter des valeurs statiques
            // List<Commerce> commerces = commerceDAO.getAllCommerces();
            // for (Commerce commerce : commerces) {
            //     commerceNames.add(commerce.getNom());
            // }
            
            // Pour le moment, on ajoute quelques valeurs statiques
            commerceNames.add("Carrefour");
            commerceNames.add("Auchan");
            commerceNames.add("Leclerc");
            commerceNames.add("Boutique Bio");
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des commerces : " + e.getMessage());
        }
        
        cmbFiltreCommerce.setItems(FXCollections.observableArrayList(commerceNames));
        cmbFiltreCommerce.getSelectionModel().selectFirst();
        
        // Écouteurs pour les changements de filtres
        cmbFiltrePeriode.setOnAction(e -> appliquerFiltres());
        cmbFiltreCommerce.setOnAction(e -> appliquerFiltres());
    }
    
    private void appliquerFiltres() {
        // Récupération des valeurs des filtres
        String periode = cmbFiltrePeriode.getValue();
        String commerce = cmbFiltreCommerce.getValue();
        
        // Application des filtres
        historiqueFiltre.setPredicate(historique -> {
            boolean matchPeriode = true;
            boolean matchCommerce = true;
            
            // Filtre par période
            if (!"Toutes les périodes".equals(periode)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime date = historique.getDateUtilisation();
                
                switch (periode) {
                    case "Aujourd'hui":
                        matchPeriode = date.toLocalDate().equals(now.toLocalDate());
                        break;
                    case "Cette semaine":
                        matchPeriode = date.isAfter(now.minusDays(7));
                        break;
                    case "Ce mois-ci":
                        matchPeriode = date.getMonth() == now.getMonth() && date.getYear() == now.getYear();
                        break;
                    case "Cette année":
                        matchPeriode = date.getYear() == now.getYear();
                        break;
                }
            }
            
            // Filtre par commerce
            if (!"Tous les commerces".equals(commerce)) {
                matchCommerce = historique.getCommerce().equals(commerce);
            }
            
            return matchPeriode && matchCommerce;
        });
    }
    
    private Callback<TableColumn<BonAchat, Void>, TableCell<BonAchat, Void>> createActionButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<BonAchat, Void> call(final TableColumn<BonAchat, Void> param) {
                return new TableCell<>() {
                    private final Button btnUtiliser = new Button("Utiliser");
                    
                    {
                        btnUtiliser.setOnAction(event -> {
                            BonAchat bonAchat = getTableView().getItems().get(getIndex());
                            ouvrirDialogUtilisation(bonAchat);
                        });
                        
                        btnUtiliser.getStyleClass().add("btn-obtenir");
                        btnUtiliser.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnUtiliser);
                        }
                    }
                };
            }
        };
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
                            afficherDetailsHistorique(historique);
                        });
                        
                        btnDetails.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDetails);
                        }
                    }
                };
            }
        };
    }
    
    private void setupButtonActions() {
        // Actions pour créer des bons d'achat
        btn5euros.setOnAction(event -> creerBonAchat(5, COUT_BON_5_EUROS));
        btn15euros.setOnAction(event -> creerBonAchat(15, COUT_BON_15_EUROS));
        btn30euros.setOnAction(event -> creerBonAchat(30, COUT_BON_30_EUROS));
        
        // Action pour actualiser la liste des bons d'achat
        btnActualiser.setOnAction(event -> {
            chargerBonsAchat();
            updatePointsAffichage();
        });
        
        // Action pour exporter l'historique
        btnExportHistorique.setOnAction(event -> exporterHistorique());
        
        // Actions pour la navigation dans le menu latéral
        btnAccueil.setOnAction(event -> naviguerVers("Accueil"));
        btnPoubelles.setOnAction(event -> naviguerVers("Poubelles"));
        btnDepots.setOnAction(event -> naviguerVers("Depots"));
        btnCommerces.setOnAction(event -> naviguerVers("Commerces"));
        btnDeconnexion.setOnAction(event -> deconnecter());
    }
    
    private void creerBonAchat(int montant, int coutPoints) {
        // Vérifier si l'utilisateur a assez de points
        if (compteCourant.getNbPointsFidelite() < coutPoints) {
            afficherAlerte(
                "Points insuffisants", 
                "Vous n'avez pas assez de points pour créer ce bon d'achat.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Confirmation de création
        Optional<ButtonType> result = afficherConfirmation(
            "Créer un bon d'achat",
            "Vous êtes sur le point de créer un bon d'achat de " + montant + "€ pour " + coutPoints + " points.",
            "Voulez-vous continuer ?"
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Création du bon d'achat
                BonAchat nouveauBon = new BonAchat(montant);
                
                // Enregistrement dans la base de données
                bonAchatDAO.insertBonAchat(nouveauBon);
                
                // Enregistrement de la relation entre le compte et le bon d'achat
                convertirDAO.insertConversion(compteCourant.getId(), nouveauBon.getId());
                
                // Mise à jour des points du compte
                int nouveauxPoints = compteCourant.getNbPointsFidelite() - coutPoints;
                compteDAO.updatePointsFidelite(compteCourant.getId(), nouveauxPoints);
                compteCourant.setNbPointsFidelite(nouveauxPoints);
                
                // Mise à jour de l'affichage
                updatePointsAffichage();
                chargerBonsAchat();
                
                // Afficher confirmation
                afficherAlerte(
                    "Bon créé avec succès", 
                    "Votre bon d'achat de " + montant + "€ a été créé avec succès (ID: " + nouveauBon.getId() + ").",
                    Alert.AlertType.INFORMATION
                );
            } catch (Exception e) {
                afficherAlerte(
                    "Erreur", 
                    "Une erreur est survenue lors de la création du bon d'achat : " + e.getMessage(),
                    Alert.AlertType.ERROR
                );
            }
        }
    }
    
    private void chargerBonsAchat() {
        try {
            // Récupération des bons d'achat du compte
            List<Integer> bonIds = convertirDAO.getBonAchatsByCompte(compteCourant.getId());
            List<BonAchat> bons = new ArrayList<>();
            
            // Récupération des détails de chaque bon d'achat
            for (Integer id : bonIds) {
                try {
                    BonAchat bon = bonAchatDAO.getBonAchatById(id);
                    bons.add(bon);
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du bon d'achat " + id + " : " + e.getMessage());
                }
            }
            
            // Mise à jour de la liste observable
            bonsAchatList.clear();
            bonsAchatList.addAll(bons);
        } catch (Exception e) {
            afficherAlerte(
                "Erreur de chargement", 
                "Impossible de charger les bons d'achat : " + e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void chargerHistorique() {
        try {
            // Récupération des bons utilisés
            // Cette partie nécessiterait une requête SQL complexe ou plusieurs requêtes
            // Pour le moment, on simule les données
            
            // Dans une implémentation réelle, vous pourriez faire quelque chose comme :
            // 1. Récupérer tous les bons d'achat du compte qui ont été utilisés
            // 2. Pour chaque bon, récupérer le commerce où il a été utilisé
            // 3. Créer un objet HistoriqueUtilisation pour chaque utilisation
            
            historiqueList.clear();
            
            // Simulation de données pour le moment
            historiqueList.add(new HistoriqueUtilisation(1, 5, "Carrefour", LocalDateTime.now().minusDays(2)));
            historiqueList.add(new HistoriqueUtilisation(2, 15, "Auchan", LocalDateTime.now().minusDays(5)));
            historiqueList.add(new HistoriqueUtilisation(3, 30, "Leclerc", LocalDateTime.now().minusDays(10)));
            
            // Réapplication des filtres
            appliquerFiltres();
        } catch (Exception e) {
            afficherAlerte(
                "Erreur de chargement", 
                "Impossible de charger l'historique : " + e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void ouvrirDialogUtilisation(BonAchat bonAchat) {
        try {
            // Récupération des commerces disponibles
            List<Commerce> commerces = new ArrayList<>();
            
            // À remplacer par une vraie méthode pour récupérer tous les commerces
            // Par exemple : commerces = commerceDAO.getAllCommerces();
            
            // Pour le moment, on ajoute quelques commerces statiques
            commerces.add(new Commerce(1, "Carrefour"));
            commerces.add(new Commerce(2, "Auchan"));
            commerces.add(new Commerce(3, "Leclerc"));
            commerces.add(new Commerce(4, "Boutique Bio"));
            
            if (commerces.isEmpty()) {
                afficherAlerte(
                    "Aucun commerce disponible", 
                    "Il n'y a actuellement aucun commerce partenaire disponible.",
                    Alert.AlertType.WARNING
                );
                return;
            }
            
            // Création du dialogue de sélection
            Dialog<Commerce> dialog = new Dialog<>();
            dialog.setTitle("Utiliser le bon d'achat #" + bonAchat.getId());
            dialog.setHeaderText("Choisissez un commerce où utiliser votre bon de " + bonAchat.getMontant() + "€");
            
            // Boutons
            ButtonType btnUtiliser = new ButtonType("Utiliser", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnUtiliser, ButtonType.CANCEL);
            
            // Liste des commerces
            ComboBox<Commerce> cmbCommerces = new ComboBox<>();
            cmbCommerces.setItems(FXCollections.observableArrayList(commerces));
            cmbCommerces.getSelectionModel().select(0);
            cmbCommerces.setPromptText("Sélectionnez un commerce");
            
            // Convertisseur pour l'affichage des commerces
            cmbCommerces.setConverter(new javafx.util.StringConverter<Commerce>() {
                @Override
                public String toString(Commerce commerce) {
                    return commerce.getNom();
                }

                @Override
                public Commerce fromString(String string) {
                    return null; // Pas nécessaire pour notre cas
                }
            });
            
            // Affichage du dialogue
            dialog.getDialogPane().setContent(cmbCommerces);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnUtiliser) {
                    return cmbCommerces.getValue();
                }
                return null;
            });
            
            // Traitement du résultat
            Optional<Commerce> result = dialog.showAndWait();
            result.ifPresent(commerce -> {
                try {
                    // Enregistrement de l'utilisation du bon d'achat avec le commerce sélectionné
                    utiliserDAO.insertUtilisation(bonAchat.getId(), commerce.getIdCommerce());
                    
                    // Suppression de la relation convertir (car le bon est utilisé)
                    convertirDAO.deleteConversion(compteCourant.getId(), bonAchat.getId());
                    
                    // Mise à jour de l'interface
                    bonsAchatList.remove(bonAchat);
                    
                    // Ajout à l'historique
                    historiqueList.add(new HistoriqueUtilisation(
                        bonAchat.getId(),
                        bonAchat.getMontant(),
                        commerce.getNom(),
                        LocalDateTime.now()
                    ));
                    
                    // Appliquer les filtres
                    appliquerFiltres();
                    
                    // Afficher confirmation
                    afficherAlerte(
                        "Bon utilisé avec succès", 
                        "Votre bon d'achat de " + bonAchat.getMontant() + "€ a été utilisé chez " + commerce.getNom() + ".",
                        Alert.AlertType.INFORMATION
                    );
                } catch (Exception e) {
                    afficherAlerte(
                        "Erreur", 
                        "Une erreur est survenue lors de l'utilisation du bon : " + e.getMessage(),
                        Alert.AlertType.ERROR
                    );
                }
            });
        } catch (Exception e) {
            afficherAlerte(
                "Erreur", 
                "Une erreur est survenue : " + e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void afficherDetailsHistorique(HistoriqueUtilisation historique) {
        // Création d'une alerte avec les détails
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisation");
        alert.setHeaderText("Bon d'achat #" + historique.getIdBon() + " - " + historique.getMontant() + "€");
        
        // Création d'un GridPane pour afficher les détails
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        // Ajout des détails
        grid.add(new Label("Identifiant:"), 0, 0);
        grid.add(new Label("" + historique.getIdBon()), 1, 0);
        
        grid.add(new Label("Montant:"), 0, 1);
        grid.add(new Label(historique.getMontant() + "€"), 1, 1);
        
        grid.add(new Label("Commerce:"), 0, 2);
        grid.add(new Label(historique.getCommerce()), 1, 2);
        
        grid.add(new Label("Date d'utilisation:"), 0, 3);
        grid.add(new Label(historique.getDateUtilisationFormatted()), 1, 3);
        
        alert.getDialogPane().setContent(grid);
        alert.showAndWait();
    }
    
    private void exporterHistorique() {
        // Configuration du FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter l'historique");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        fileChooser.setInitialFileName("historique_bons_achat_" + 
                                      LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv");
        
        // Affichage du dialogue de sauvegarde
        File file = fileChooser.showSaveDialog(btnExportHistorique.getScene().getWindow());
        
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Écriture de l'en-tête
                writer.write("ID Bon;Montant;Commerce;Date d'utilisation\n");
                
                // Écriture des données (selon les filtres actuels)
                for (HistoriqueUtilisation historique : tableHistorique.getItems()) {
                    writer.write(String.format("%d;%d;%s;%s\n",
                        historique.getIdBon(),
                        historique.getMontant(),
                        historique.getCommerce(),
                        historique.getDateUtilisationFormatted()
                    ));
                }
                
                // Confirmation à l'utilisateur
                afficherAlerte(
                    "Export réussi", 
                    "L'historique a été exporté avec succès dans le fichier :\n" + file.getAbsolutePath(),
                    Alert.AlertType.INFORMATION
                );
            } catch (IOException e) {
                afficherAlerte(
                    "Erreur d'export", 
                    "Impossible d'exporter l'historique : " + e.getMessage(),
                    Alert.AlertType.ERROR
                );
            }
        }
    }
    
    private void naviguerVers(String destination) {
        // À implémenter selon votre système de navigation
        // Par exemple, vous pourriez charger une nouvelle vue FXML
        System.out.println("Navigation vers: " + destination);
    }
    
    private void deconnecter() {
        // À implémenter selon votre système d'authentification
        System.out.println("Déconnexion...");
    }
    
    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private Optional<ButtonType> afficherConfirmation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}