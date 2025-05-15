package tri.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import tri.dao.*;
import tri.logic.*;
import tri.services.CommerceService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CommerceController implements Initializable {

    // Éléments de l'interface - Communs
    @FXML private Label lblStatusBar;
    @FXML private TabPane tabPane;
    
    // Éléments de l'interface - Onglet Commerces
    @FXML private GridPane gridCommerces;
    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> cmbCategorie;
    @FXML private Button btnActualiser;

    // Éléments de l'interface - Onglet Produits
    @FXML private Label lblCommerceProduits;
    @FXML private Text txtDescriptionProduits;
    @FXML private TextField txtRechercheProduit;
    @FXML private ComboBox<String> cmbCategorieProduit;
    @FXML private Button btnRetourCommerces;
    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, String> colNomProduit;
    @FXML private TableColumn<Produit, String> colCategorieProduit;
    @FXML private TableColumn<Produit, Double> colPrixProduit;
    @FXML private TableColumn<Produit, Void> colActionsProduit;
    @FXML private Label lblBonsDisponibles;
    @FXML private Button btnVoirPanier;

    // Éléments de l'interface - Onglet Panier
    @FXML private TableView<Produit> tablePanier;
    @FXML private TableColumn<Produit, String> colNomProduitPanier;
    @FXML private TableColumn<Produit, String> colCategorieProduitPanier;
    @FXML private TableColumn<Produit, Double> colPrixProduitPanier;
    @FXML private TableColumn<Produit, Void> colActionsProduitPanier;
    @FXML private Label lblTotalPanier;
    @FXML private Label lblBonsUtilises;
    @FXML private Label lblTotalAPayer;
    @FXML private ComboBox<BonAchat> cmbBonAchat;
    @FXML private Button btnAppliquerBon;
    @FXML private Button btnPayer;

    // Boutons du menu latéral
    @FXML private Button btnAccueil, btnPoubelles, btnDepots, btnBonsAchat, btnCommerces, btnDeconnexion;
    
    // Services et DAOs
    private final CommerceService commerceService = new CommerceService();
    private final BonAchatDAO bonAchatDAO = new BonAchatDAO();
    private final CompteDAO compteDAO = new CompteDAO();
    private final ConvertirDAO convertirDAO = new ConvertirDAO();
    private final UtiliserDAO utiliserDAO = new UtiliserDAO();
    
    // Variables de données
    private Compte compteCourant;
    private List<Commerce> commercesList;
    private Commerce commerceSelectionne;
    private ObservableList<Produit> produitsList;
    private ObservableList<Produit> panierList;
    private ObservableList<BonAchat> bonsAchatList;
    private BonAchat bonAchatSelectionne;
    private double totalPanier = 0.0;
    private double bonsUtilises = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents();
        loadCompte();
        setupUI();
        setupEventHandlers();
        loadData();
    }
    
    private void initializeComponents() {
        produitsList = FXCollections.observableArrayList();
        panierList = FXCollections.observableArrayList();
        bonsAchatList = FXCollections.observableArrayList();
    }
    
    private void loadCompte() {
        compteCourant = getCompteConnecte();
        lblStatusBar.setText("Connecté en tant que: " + compteCourant.getNom() + " " + compteCourant.getPrenom());
    }
    
    private void setupUI() {
        setupTables();
        setupComboBoxes();
        updatePanierInfo();
    }
    
    private void setupEventHandlers() {
        // Recherche
        txtRecherche.textProperty().addListener((obs, oldValue, newValue) -> filtrerCommerces());
        txtRechercheProduit.textProperty().addListener((obs, oldValue, newValue) -> filtrerProduits());
        
        // Navigation
        btnRetourCommerces.setOnAction(e -> tabPane.getSelectionModel().select(0));
        btnVoirPanier.setOnAction(e -> tabPane.getSelectionModel().select(2));
        
        // Actions
        btnActualiser.setOnAction(e -> chargerCommerces());
        btnAppliquerBon.setOnAction(e -> appliquerBonAchat());
        btnPayer.setOnAction(e -> effectuerPaiement());
        
        // Navigation menu latéral
        btnAccueil.setOnAction(e -> naviguerVers("Accueil"));
        btnPoubelles.setOnAction(e -> naviguerVers("Poubelles"));
        btnDepots.setOnAction(e -> naviguerVers("Depots"));
        btnBonsAchat.setOnAction(e -> naviguerVers("BonsAchat"));
        btnDeconnexion.setOnAction(e -> deconnecter());
    }
    
    private void loadData() {
        chargerCommerces();
        chargerBonsAchat();
    }
    
    private Compte getCompteConnecte() {
        // TODO: Remplacer par un vrai système de session
        try {
            return compteDAO.getCompteById(1);
        } catch (Exception e) {
            return new Compte(1, "Dupont", "Jean", 1500);
        }
    }
    
    private void setupTables() {
        // Configuration table produits
        colNomProduit.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorieProduit.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrixProduit.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colActionsProduit.setCellFactory(createAddToCartButtonCellFactory());
        tableProduits.setItems(produitsList);
        
        // Configuration table panier
        colNomProduitPanier.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorieProduitPanier.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrixProduitPanier.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colActionsProduitPanier.setCellFactory(createRemoveFromCartButtonCellFactory());
        tablePanier.setItems(panierList);
    }
    
    private void setupComboBoxes() {
        cmbBonAchat.setConverter(new javafx.util.StringConverter<BonAchat>() {
            @Override
            public String toString(BonAchat bonAchat) {
                return bonAchat == null ? "" : bonAchat.getMontant() + "€";
            }
            
            @Override
            public BonAchat fromString(String string) {
                return null;
            }
        });
        cmbBonAchat.setItems(bonsAchatList);
    }
    
    private void chargerCommerces() {
        try {
            commercesList = commerceService.getAllCommerces();
            afficherCommerces();
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les commerces : " + e.getMessage());
        }
    }
    
    private void afficherCommerces() {
        // Effacer la grille existante
        gridCommerces.getChildren().clear();
        gridCommerces.getRowConstraints().clear();
        
        // Ajouter des contraintes de ligne
        int nbLignes = (int) Math.ceil(commercesList.size() / 2.0);
        for (int i = 0; i < nbLignes; i++) {
            gridCommerces.getRowConstraints().add(new RowConstraints());
        }
        
        // Ajouter chaque commerce à la grille
        int row = 0;
        int col = 0;
        
        for (Commerce commerce : commercesList) {
            VBox commerceBox = createCommerceBox(commerce);
            gridCommerces.add(commerceBox, col, row);
            
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }
    
    private VBox createCommerceBox(Commerce commerce) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5px;");
        
        // Nom du commerce
        Label lblNom = new Label(commerce.getNom());
        lblNom.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Information sur les produits éligibles
        List<String> categoriesEligibles = getCategoriesToligibles(commerce);
        int nbProduitsEligibles = countProduitsEligibles(commerce, categoriesEligibles);
        
        // Texte d'information
        Label lblInfos = createInfoLabel(nbProduitsEligibles, categoriesEligibles);
        Label lblCategories = new Label("Catégories éligibles: " + String.join(", ", categoriesEligibles));
        lblCategories.setStyle("-fx-font-size: 12px;");
        
        // Bouton d'action
        Button btnVoirProduits = new Button("Voir les produits");
        btnVoirProduits.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
        btnVoirProduits.setOnAction(e -> afficherProduits(commerce));
        
        // Effet de survol
        setupBoxHoverEffect(box);
        
        box.getChildren().addAll(lblNom, lblInfos, lblCategories, new Separator(), btnVoirProduits);
        
        return box;
    }
    
    private List<String> getCategoriesToligibles(Commerce commerce) {
        List<String> categoriesEligibles = new ArrayList<>();
        for (Contrat contrat : commerce.getContrats()) {
            if (contrat.estActif()) {
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        return categoriesEligibles;
    }
    
    private int countProduitsEligibles(Commerce commerce, List<String> categoriesEligibles) {
        return (int) commerce.getProduits().stream()
                              .filter(p -> categoriesEligibles.contains(p.getCategorie()))
                              .count();
    }
    
    private Label createInfoLabel(int nbProduitsEligibles, List<String> categoriesEligibles) {
        Label lblInfos;
        if (!categoriesEligibles.isEmpty() && nbProduitsEligibles > 0) {
            lblInfos = new Label(nbProduitsEligibles + " produits éligibles aux bons d'achat");
            lblInfos.setStyle("-fx-text-fill: #27AE60;");
        } else {
            lblInfos = new Label("Aucun produit éligible aux bons d'achat");
            lblInfos.setStyle("-fx-text-fill: #E74C3C;");
        }
        return lblInfos;
    }
    
    private void setupBoxHoverEffect(VBox box) {
        box.setOnMouseEntered(e -> 
            box.setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #3498DB; -fx-border-radius: 5px;"));
        box.setOnMouseExited(e -> 
            box.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5px;"));
    }
    
    private void afficherProduits(Commerce commerce) {
        commerceSelectionne = commerce;
        
        // Mise à jour des textes
        lblCommerceProduits.setText("Produits éligibles chez " + commerce.getNom());
        txtDescriptionProduits.setText("Voici les produits éligibles aux bons d'achat chez " + commerce.getNom() + 
                                       ". Ces produits appartiennent aux catégories définies dans le contrat de partenariat.");
        
        // Charger les produits éligibles
        List<String> categoriesEligibles = getCategoriesToligibles(commerce);
        List<Produit> produitsEligibles = commerce.getProduits().stream()
                                                .filter(p -> categoriesEligibles.contains(p.getCategorie()))
                                                .collect(Collectors.toList());
        
        // Mettre à jour la liste
        produitsList.clear();
        produitsList.addAll(produitsEligibles);
        
        // Configurer le filtre par catégorie
        setupCategorieFilter(categoriesEligibles);
        
        // Afficher l'onglet des produits
        tabPane.getSelectionModel().select(1);
    }
    
    private void setupCategorieFilter(List<String> categoriesEligibles) {
        cmbCategorieProduit.getItems().clear();
        cmbCategorieProduit.getItems().add("Toutes les catégories");
        cmbCategorieProduit.getItems().addAll(categoriesEligibles);
        cmbCategorieProduit.getSelectionModel().selectFirst();
        cmbCategorieProduit.setOnAction(e -> filtrerProduits());
    }
    
    private void filtrerCommerces() {
        String recherche = txtRecherche.getText().toLowerCase();
        List<Commerce> commercesFiltres = commerceService.rechercherCommerces(recherche);
        commercesList = commercesFiltres;
        afficherCommerces();
    }
    
    private void filtrerProduits() {
        if (commerceSelectionne == null) return;
        
        String recherche = txtRechercheProduit.getText().toLowerCase();
        String categorie = cmbCategorieProduit.getValue();
        
        List<String> categoriesEligibles = getCategoriesToligibles(commerceSelectionne);
        
        List<Produit> produitsFiltres = commerceSelectionne.getProduits().stream()
            .filter(p -> categoriesEligibles.contains(p.getCategorie()))
            .filter(p -> p.getNom().toLowerCase().contains(recherche))
            .filter(p -> "Toutes les catégories".equals(categorie) || p.getCategorie().equals(categorie))
            .collect(Collectors.toList());
        
        produitsList.clear();
        produitsList.addAll(produitsFiltres);
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
            
            double totalBons = bons.stream().mapToDouble(BonAchat::getMontant).sum();
            lblBonsDisponibles.setText(String.format("%.2f €", totalBons));
            
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les bons d'achat : " + e.getMessage());
        }
    }
    
    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createAddToCartButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Produit, Void> call(TableColumn<Produit, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Ajouter au panier");
                    
                    {
                        btn.setOnAction(e -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            ajouterAuPanier(produit);
                        });
                        btn.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
    }
    
    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createRemoveFromCartButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Produit, Void> call(TableColumn<Produit, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Retirer");
                    
                    {
                        btn.setOnAction(e -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            retirerDuPanier(produit);
                        });
                        btn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
    }
    
    private void ajouterAuPanier(Produit produit) {
        if (panierList.contains(produit)) {
            showInfo("Produit déjà ajouté", "Ce produit est déjà dans votre panier.");
            return;
        }
        
        List<String> categoriesEligibles = getCategoriesToligibles(commerceSelectionne);
        if (!categoriesEligibles.contains(produit.getCategorie())) {
            showWarning("Produit non éligible", "Ce produit n'est pas éligible aux bons d'achat.");
            return;
        }
        
        panierList.add(produit);
        updatePanierInfo();
        showInfo("Produit ajouté", "Le produit " + produit.getNom() + " a été ajouté à votre panier.");
    }
    
    private void retirerDuPanier(Produit produit) {
        panierList.remove(produit);
        updatePanierInfo();
    }
    
    private void updatePanierInfo() {
        totalPanier = panierList.stream().mapToDouble(Produit::getPrix).sum();
        lblTotalPanier.setText(String.format("%.2f €", totalPanier));
        
        double totalAPayer = Math.max(0, totalPanier - bonsUtilises);
        lblTotalAPayer.setText(String.format("%.2f €", totalAPayer));
    }
    
    private void appliquerBonAchat() {
        BonAchat bonAchat = cmbBonAchat.getValue();
        
        if (bonAchat == null) {
            showWarning("Aucun bon sélectionné", "Veuillez sélectionner un bon d'achat à appliquer.");
            return;
        }
        
        if (commerceSelectionne == null || panierList.isEmpty()) {
            showWarning("Panier vide", "Veuillez d'abord ajouter des produits au panier.");
            return;
        }
        
        if (!commerceService.verifierAcceptationBonAchat(commerceSelectionne, new ArrayList<>(panierList))) {
            showWarning("Panier non éligible", "Votre panier ne contient aucun produit éligible aux bons d'achat.");
            return;
        }
        
        bonAchatSelectionne = bonAchat;
        bonsUtilises = bonAchat.getMontant();
        lblBonsUtilises.setText(String.format("%.2f €", bonsUtilises));
        updatePanierInfo();
        
        showInfo("Bon appliqué", "Le bon d'achat de " + bonAchat.getMontant() + "€ a été appliqué à votre panier.");
    }
    
    private void effectuerPaiement() {
        if (panierList.isEmpty()) {
            showWarning("Panier vide", "Votre panier est vide. Veuillez ajouter des produits avant de procéder au paiement.");
            return;
        }
        
        if (bonAchatSelectionne != null && 
            !commerceService.verifierAcceptationBonAchat(commerceSelectionne, new ArrayList<>(panierList))) {
            showWarning("Panier non éligible", "Votre panier ne contient aucun produit éligible aux bons d'achat.");
            return;
        }
        
        double montantAPayer = Math.max(0, totalPanier - bonsUtilises);
        
        Optional<ButtonType> result = showConfirmation(
            "Confirmation de paiement",
            "Êtes-vous sûr de vouloir procéder au paiement ?",
            "Montant total : " + String.format("%.2f €", totalPanier) + "\n" +
            "Bons utilisés : " + String.format("%.2f €", bonsUtilises) + "\n" +
            "Montant à payer : " + String.format("%.2f €", montantAPayer)
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            procederPaiement();
        }
    }
    
    private void procederPaiement() {
        try {
            // Si un bon d'achat a été utilisé, le traiter
            if (bonAchatSelectionne != null) {
                convertirDAO.deleteConversion(compteCourant.getId(), bonAchatSelectionne.getId());
                utiliserDAO.insertUtilisation(bonAchatSelectionne.getId(), commerceSelectionne.getIdCommerce());
                bonsAchatList.remove(bonAchatSelectionne);
                
                double totalBons = bonsAchatList.stream().mapToDouble(BonAchat::getMontant).sum();
                lblBonsDisponibles.setText(String.format("%.2f €", totalBons));
            }
            
            // Réinitialiser
            panierList.clear();
            bonAchatSelectionne = null;
            bonsUtilises = 0.0;
            lblBonsUtilises.setText("0.00 €");
            updatePanierInfo();
            
            tabPane.getSelectionModel().select(0);
            showInfo("Paiement effectué", "Votre paiement a été effectué avec succès.");
            
        } catch (Exception e) {
            showError("Erreur de paiement", "Une erreur est survenue lors du paiement : " + e.getMessage());
        }
    }
    //A MODIFIER
    private void naviguerVers(String destination) {
        System.out.println("Navigation vers: " + destination);
        // TODO: Implémenter la navigation
    }
    //A MODIFIER
    private void deconnecter() {
        System.out.println("Déconnexion...");
        // TODO: Implémenter la déconnexion
    }
    
    // Méthodes utilitaires pour les alertes
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.INFORMATION);
    }
    
    private void showError(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.ERROR);
    }
    
    private void showWarning(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.WARNING);
    }
    
    private Optional<ButtonType> showConfirmation(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}