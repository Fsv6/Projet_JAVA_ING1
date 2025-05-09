package tri.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import tri.dao.*;
import tri.logic.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CommerceController implements Initializable {

    // Éléments de l'interface - Communs
    @FXML
    private Label lblStatusBar;
    
    @FXML
    private TabPane tabPane;
    
    // Éléments de l'interface - Onglet Commerces
    @FXML
    private GridPane gridCommerces;
    
    @FXML
    private TextField txtRecherche;
    
    @FXML
    private ComboBox<String> cmbCategorie;
    
    @FXML
    private Button btnActualiser;

    // Éléments de l'interface - Onglet Produits
    @FXML
    private Label lblCommerceProduits;
    
    @FXML
    private Text txtDescriptionProduits;
    
    @FXML
    private TextField txtRechercheProduit;
    
    @FXML
    private ComboBox<String> cmbCategorieProduit;
    
    @FXML
    private Button btnRetourCommerces;
    
    @FXML
    private TableView<Produit> tableProduits;
    
    @FXML
    private TableColumn<Produit, String> colNomProduit;
    
    @FXML
    private TableColumn<Produit, String> colCategorieProduit;
    
    @FXML
    private TableColumn<Produit, Double> colPrixProduit;
    
    @FXML
    private TableColumn<Produit, Void> colActionsProduit;
    
    @FXML
    private Label lblBonsDisponibles;
    
    @FXML
    private Button btnVoirPanier;

    // Éléments de l'interface - Onglet Panier
    @FXML
    private TableView<Produit> tablePanier;
    
    @FXML
    private TableColumn<Produit, String> colNomProduitPanier;
    
    @FXML
    private TableColumn<Produit, String> colCategorieProduitPanier;
    
    @FXML
    private TableColumn<Produit, Double> colPrixProduitPanier;
    
    @FXML
    private TableColumn<Produit, Void> colActionsProduitPanier;
    
    @FXML
    private Label lblTotalPanier;
    
    @FXML
    private Label lblBonsUtilises;
    
    @FXML
    private Label lblTotalAPayer;
    
    @FXML
    private ComboBox<BonAchat> cmbBonAchat;
    
    @FXML
    private Button btnAppliquerBon;
    
    @FXML
    private Button btnPayer;

    // Boutons du menu latéral
    @FXML
    private Button btnAccueil, btnPoubelles, btnDepots, btnBonsAchat, btnCommerces, btnDeconnexion;
    
    // Services et DAOs
    private CommerceDAO commerceDAO;
    private ProduitDAO produitDAO;
    private ContratDAO contratDAO;
    private BonAchatDAO bonAchatDAO;
    private CompteDAO compteDAO;
    private ConvertirDAO convertirDAO;
    
    // Variables de données
    private Compte compteCourant;
    private List<Commerce> commercesList;
    private Commerce commerceSelectionne;
    private ObservableList<Produit> produitsList;      // Produits éligibles uniquement
    private ObservableList<Produit> tousProduitsListe; // Tous les produits (pour référence)
    private ObservableList<Produit> panierList;
    private ObservableList<BonAchat> bonsAchatList;
    private BonAchat bonAchatSelectionne;
    private double totalPanier = 0.0;
    private double bonsUtilises = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des DAOs et services
        initializeServices();
        
        // Récupération du compte courant
        compteCourant = getCompteConnecte();
        lblStatusBar.setText("Connecté en tant que: " + compteCourant.getNom() + " " + compteCourant.getPrenom());
        
        // Initialisation des listes
        produitsList = FXCollections.observableArrayList();
        tousProduitsListe = FXCollections.observableArrayList(); 
        panierList = FXCollections.observableArrayList();
        bonsAchatList = FXCollections.observableArrayList();
        
        // Configuration des tableaux
        setupTableProduits();
        setupTablePanier();
        
        // Configuration de la sélection des bons d'achat
        setupBonsAchatComboBox();
        
        // Chargement des données
        chargerCommerces();
        chargerBonsAchat();
        
        // Configuration des événements
        setupEventHandlers();
        
        // Mise à jour de l'affichage
        updateTotalPanier();
    }
    
    private void initializeServices() {
        commerceDAO = new CommerceDAO();
        produitDAO = new ProduitDAO();
        contratDAO = new ContratDAO();
        bonAchatDAO = new BonAchatDAO();
        compteDAO = new CompteDAO();
        convertirDAO = new ConvertirDAO();
    }
    
    private Compte getCompteConnecte() {
        // Dans un cas réel, récupérez le compte depuis une session
        try {
            return compteDAO.getCompteById(1); // ID du compte connecté (à remplacer par un système de session)
        } catch (Exception e) {
            // Si le compte n'existe pas, on crée un compte par défaut
            return new Compte(1, "Dupont", "Jean", 1500);
        }
    }
    
    private void setupTableProduits() {
        colNomProduit.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorieProduit.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrixProduit.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colActionsProduit.setCellFactory(createAddToCartButtonCellFactory());
        
        tableProduits.setItems(produitsList);
    }
    
    private void setupTablePanier() {
        colNomProduitPanier.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorieProduitPanier.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrixProduitPanier.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colActionsProduitPanier.setCellFactory(createRemoveFromCartButtonCellFactory());
        
        tablePanier.setItems(panierList);
    }
    
    private void setupBonsAchatComboBox() {
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
    
    private void setupEventHandlers() {
        // Recherche de commerces
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerCommerces();
        });
        
        // Recherche de produits
        txtRechercheProduit.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerProduits();
        });
        
        // Bouton retour aux commerces
        btnRetourCommerces.setOnAction(event -> {
            tabPane.getSelectionModel().select(0); // Sélectionner l'onglet Commerces
        });
        
        // Bouton voir panier
        btnVoirPanier.setOnAction(event -> {
            tabPane.getSelectionModel().select(2); // Sélectionner l'onglet Panier
        });
        
        // Bouton appliquer bon
        btnAppliquerBon.setOnAction(event -> {
            appliquerBonAchat();
        });
        
        // Bouton payer
        btnPayer.setOnAction(event -> {
            effectuerPaiement();
        });
        
        // Bouton actualiser
        btnActualiser.setOnAction(event -> {
            chargerCommerces();
        });
        
        // Navigation menu latéral
        btnAccueil.setOnAction(event -> naviguerVers("Accueil"));
        btnPoubelles.setOnAction(event -> naviguerVers("Poubelles"));
        btnDepots.setOnAction(event -> naviguerVers("Depots"));
        btnBonsAchat.setOnAction(event -> naviguerVers("BonsAchat"));
        btnDeconnexion.setOnAction(event -> deconnecter());
    }
    
    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createAddToCartButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Produit, Void> call(final TableColumn<Produit, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Ajouter au panier");
                    
                    {
                        btn.setOnAction(event -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            ajouterAuPanier(produit);
                        });
                        
                        btn.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }
    
    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createRemoveFromCartButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Produit, Void> call(final TableColumn<Produit, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Retirer");
                    
                    {
                        btn.setOnAction(event -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            retirerDuPanier(produit);
                        });
                        
                        btn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }
    
    private void chargerCommerces() {
        try {
            // Dans une implémentation réelle, vous chargeriez les commerces depuis la base de données
            // Par exemple avec une méthode comme commerceDAO.getAllCommerces()
            
            // Pour le moment, on crée quelques commerces statiques
            commercesList = new ArrayList<>();
            commercesList.add(new Commerce(1, "Carrefour"));
            commercesList.add(new Commerce(2, "Auchan"));
            commercesList.add(new Commerce(3, "Leclerc"));
            commercesList.add(new Commerce(4, "Boutique Bio"));
            commercesList.add(new Commerce(5, "Intermarché"));
            commercesList.add(new Commerce(6, "Magasin Vert"));
            
            // Ajout de produits dans chaque commerce pour les tests
            for (Commerce commerce : commercesList) {
                ajouterProduitsPourCommerce(commerce);
                ajouterContratPourCommerce(commerce);
            }
            
            // Affichage des commerces dans le gridPane
            afficherCommerces();
            
        } catch (Exception e) {
            afficherAlerte(
                "Erreur de chargement", 
                "Impossible de charger les commerces : " + e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void ajouterProduitsPourCommerce(Commerce commerce) {
        // Simuler l'ajout de produits dans le commerce
        List<String> categories = List.of("Alimentaire", "Électronique", "Vêtements", "Maison", "Loisirs");
        
        // Nombre aléatoire de produits entre 5 et 15
        int nbProduits = 5 + (int)(Math.random() * 10);
        
        for (int i = 0; i < nbProduits; i++) {
            String categorie = categories.get((int)(Math.random() * categories.size()));
            String nom = "Produit " + (i + 1) + " de " + commerce.getNom();
            double prix = Math.round((5 + Math.random() * 95) * 100) / 100.0;
            
            Produit produit = new Produit(100 * commerce.getIdCommerce() + i, categorie, nom, prix);
            commerce.ajouterProduit(produit);
        }
    }
    
    private void ajouterContratPourCommerce(Commerce commerce) {
        // Créer un contrat avec des catégories éligibles pour les bons d'achat
        // Pour les besoins de test, on choisit aléatoirement 2 catégories parmi les 5 disponibles
        
        List<String> toutesCategories = List.of("Alimentaire", "Électronique", "Vêtements", "Maison", "Loisirs");
        List<String> categoriesEligibles = new ArrayList<>();
        
        // Sélection aléatoire de 2 catégories
        categoriesEligibles.add(toutesCategories.get((int)(Math.random() * toutesCategories.size())));
        
        // S'assurer que la deuxième catégorie est différente de la première
        String deuxiemeCategorie;
        do {
            deuxiemeCategorie = toutesCategories.get((int)(Math.random() * toutesCategories.size()));
        } while (categoriesEligibles.contains(deuxiemeCategorie));
        
        categoriesEligibles.add(deuxiemeCategorie);
        
        // Création du contrat avec dates de début et fin
        Contrat contrat = new Contrat(
            java.time.LocalDate.now().minusMonths(1),  // Date de début (1 mois dans le passé)
            java.time.LocalDate.now().plusMonths(6),   // Date de fin (6 mois dans le futur)
            categoriesEligibles
        );
        
        // Ajout du contrat au commerce
        commerce.ajouterContrat(contrat);
        
        System.out.println("Commerce: " + commerce.getNom() + ", Catégories éligibles: " + categoriesEligibles);
    }
    
    private void afficherCommerces() {
        // Effacer la grille existante
        gridCommerces.getChildren().clear();
        gridCommerces.getRowConstraints().clear();
        
        // Ajouter des contraintes de ligne au besoin
        int nbLignes = (int) Math.ceil(commercesList.size() / 2.0);
        for (int i = 0; i < nbLignes; i++) {
            gridCommerces.getRowConstraints().add(new RowConstraints());
        }
        
        // Ajouter chaque commerce à la grille
        int row = 0;
        int col = 0;
        
        for (Commerce commerce : commercesList) {
            // Créer un panneau pour le commerce
            VBox commerceBox = createCommerceBox(commerce);
            
            // Ajouter à la grille
            gridCommerces.add(commerceBox, col, row);
            
            // Incrémenter les indices
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
        
        // Information sur les contrats et produits éligibles
        List<String> categoriesEligibles = new ArrayList<>();
        boolean contratActif = false;
        
        for (Contrat contrat : commerce.getContrats()) {
            if (contrat.estActif()) {
                contratActif = true;
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        
        // Compter les produits éligibles
        int nbProduitsEligibles = 0;
        for (Produit produit : commerce.getProduits()) {
            if (categoriesEligibles.contains(produit.getCategorie())) {
                nbProduitsEligibles++;
            }
        }
        
        // Texte d'information
        Label lblInfos;
        if (contratActif && nbProduitsEligibles > 0) {
            lblInfos = new Label(nbProduitsEligibles + " produits éligibles aux bons d'achat");
            lblInfos.setStyle("-fx-text-fill: #27AE60;"); // Vert pour indiquer des produits éligibles
        } else {
            lblInfos = new Label("Aucun produit éligible aux bons d'achat");
            lblInfos.setStyle("-fx-text-fill: #E74C3C;"); // Rouge pour indiquer aucun produit éligible
        }
        
        // Catégories éligibles
        Label lblCategories = new Label("Catégories éligibles: " + String.join(", ", categoriesEligibles));
        lblCategories.setStyle("-fx-font-size: 12px;");
        
        // Boutons d'action
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Button btnVoirProduits = new Button("Voir les produits");
        btnVoirProduits.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
        btnVoirProduits.setOnAction(event -> {
            afficherProduits(commerce);
        });
        
        // Effet de survol
        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #3498DB; -fx-border-radius: 5px;");
        });
        
        box.setOnMouseExited(e -> {
            box.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5px;");
        });
        
        buttonsBox.getChildren().add(btnVoirProduits);
        
        box.getChildren().addAll(lblNom, lblInfos, lblCategories, new Separator(), buttonsBox);
        
        return box;
    }
    
    private void afficherProduits(Commerce commerce) {
        // Mémoriser le commerce sélectionné
        commerceSelectionne = commerce;
        
        // Mettre à jour le titre et la description
        lblCommerceProduits.setText("Produits éligibles chez " + commerce.getNom());
        txtDescriptionProduits.setText("Voici les produits éligibles aux bons d'achat chez " + commerce.getNom() + 
                                       ". Ces produits appartiennent aux catégories définies dans le contrat de partenariat.");
        
        // Récupérer les catégories éligibles aux bons d'achat
        List<String> categoriesEligibles = new ArrayList<>();
        for (Contrat contrat : commerce.getContrats()) {
            if (contrat.estActif()) {
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        
        // Filtrer les produits éligibles
        List<Produit> produitsEligibles = new ArrayList<>();
        for (Produit produit : commerce.getProduits()) {
            if (categoriesEligibles.contains(produit.getCategorie())) {
                produitsEligibles.add(produit);
            }
        }
        
        // Stocker tous les produits pour référence
        tousProduitsListe.clear();
        tousProduitsListe.addAll(commerce.getProduits());
        
        // Charger uniquement les produits éligibles dans la liste observable
        produitsList.clear();
        produitsList.addAll(produitsEligibles);
        
        // Si aucun produit éligible, afficher un message
        if (produitsEligibles.isEmpty()) {
            txtDescriptionProduits.setText("Aucun produit éligible aux bons d'achat n'est disponible chez " + commerce.getNom() + 
                                           ". Veuillez consulter un autre commerce.");
        }
        
        // Charger les catégories éligibles dans le combobox
        cmbCategorieProduit.getItems().clear();
        cmbCategorieProduit.getItems().add("Toutes les catégories");
        cmbCategorieProduit.getItems().addAll(categoriesEligibles);
        cmbCategorieProduit.getSelectionModel().selectFirst();
        
        // Ajouter l'écouteur pour le filtre par catégorie
        cmbCategorieProduit.setOnAction(event -> filtrerProduits());
        
        // Afficher l'onglet des produits
        tabPane.getSelectionModel().select(1);
    }
    
    private void filtrerCommerces() {
        String recherche = txtRecherche.getText().toLowerCase();
        
        // Si la recherche est vide, afficher tous les commerces
        if (recherche.isEmpty()) {
            afficherCommerces();
            return;
        }
        
        // Filtrer les commerces selon la recherche
        List<Commerce> commercesFiltres = commercesList.stream()
                                                     .filter(c -> c.getNom().toLowerCase().contains(recherche))
                                                     .collect(Collectors.toList());
        
        // Mettre à jour l'affichage avec les commerces filtrés
        gridCommerces.getChildren().clear();
        gridCommerces.getRowConstraints().clear();
        
        int nbLignes = (int) Math.ceil(commercesFiltres.size() / 2.0);
        for (int i = 0; i < nbLignes; i++) {
            gridCommerces.getRowConstraints().add(new RowConstraints());
        }
        
        int row = 0;
        int col = 0;
        
        for (Commerce commerce : commercesFiltres) {
            VBox commerceBox = createCommerceBox(commerce);
            gridCommerces.add(commerceBox, col, row);
            
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }
    
    private void filtrerProduits() {
        if (commerceSelectionne == null) return;
        
        String recherche = txtRechercheProduit.getText().toLowerCase();
        String categorie = cmbCategorieProduit.getValue();
        
        // Récupérer les catégories éligibles
        List<String> categoriesEligibles = new ArrayList<>();
        for (Contrat contrat : commerceSelectionne.getContrats()) {
            if (contrat.estActif()) {
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        
        // Filtrer les produits éligibles selon la recherche et la catégorie
        List<Produit> produitsFiltres = commerceSelectionne.getProduits().stream()
            .filter(p -> categoriesEligibles.contains(p.getCategorie())) // Uniquement les produits éligibles
            .filter(p -> p.getNom().toLowerCase().contains(recherche))   // Filtre par nom
            .filter(p -> "Toutes les catégories".equals(categorie) || p.getCategorie().equals(categorie)) // Filtre par catégorie
            .collect(Collectors.toList());
        
        produitsList.clear();
        produitsList.addAll(produitsFiltres);
    }
    
    private void chargerBonsAchat() {
        try {
            // Récupérer les IDs des bons d'achat du compte
            List<Integer> bonIds = convertirDAO.getBonAchatsByCompte(compteCourant.getId());
            List<BonAchat> bons = new ArrayList<>();
            
            // Récupérer les détails de chaque bon d'achat
            for (Integer id : bonIds) {
                try {
                    BonAchat bon = bonAchatDAO.getBonAchatById(id);
                    bons.add(bon);
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du bon d'achat " + id + " : " + e.getMessage());
                }
            }
            
            // Mise à jour de la liste observable et de l'affichage
            bonsAchatList.clear();
            bonsAchatList.addAll(bons);
            
            // Mise à jour du montant total des bons disponibles
            double totalBons = bons.stream().mapToDouble(BonAchat::getMontant).sum();
            lblBonsDisponibles.setText(String.format("%.2f €", totalBons));
            
        } catch (Exception e) {
            afficherAlerte(
                "Erreur de chargement", 
                "Impossible de charger les bons d'achat : " + e.getMessage(),
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void ajouterAuPanier(Produit produit) {
        // Vérifier si le produit est déjà dans le panier
        if (panierList.contains(produit)) {
            afficherAlerte(
                "Produit déjà ajouté", 
                "Ce produit est déjà dans votre panier.",
                Alert.AlertType.INFORMATION
            );
            return;
        }
        
        // Vérifier si le produit est éligible aux bons d'achat
        List<String> categoriesEligibles = new ArrayList<>();
        for (Contrat contrat : commerceSelectionne.getContrats()) {
            if (contrat.estActif()) {
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        
        if (!categoriesEligibles.contains(produit.getCategorie())) {
            afficherAlerte(
                "Produit non éligible", 
                "Ce produit n'est pas éligible aux bons d'achat.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Ajouter le produit au panier
        panierList.add(produit);
        
        // Mettre à jour le total du panier
        updateTotalPanier();
        
        // Afficher confirmation
        afficherAlerte(
            "Produit ajouté", 
            "Le produit " + produit.getNom() + " a été ajouté à votre panier.",
            Alert.AlertType.INFORMATION
        );
    }
    
    private void retirerDuPanier(Produit produit) {
        // Retirer le produit du panier
        panierList.remove(produit);
        
        // Mettre à jour le total du panier
        updateTotalPanier();
    }
    
    private void updateTotalPanier() {
        // Calculer le total du panier
        totalPanier = panierList.stream().mapToDouble(Produit::getPrix).sum();
        lblTotalPanier.setText(String.format("%.2f €", totalPanier));
        
        // Calculer le total à payer
        double totalAPayer = Math.max(0, totalPanier - bonsUtilises);
        lblTotalAPayer.setText(String.format("%.2f €", totalAPayer));
    }
    
    private void appliquerBonAchat() {
        BonAchat bonAchat = cmbBonAchat.getValue();
        
        if (bonAchat == null) {
            afficherAlerte(
                "Aucun bon sélectionné", 
                "Veuillez sélectionner un bon d'achat à appliquer.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Vérifier si un commerce est sélectionné et si ce commerce accepte les bons d'achat
        if (commerceSelectionne == null) {
            afficherAlerte(
                "Aucun commerce sélectionné", 
                "Veuillez d'abord sélectionner un commerce et ajouter des produits au panier.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Vérifier si le panier contient des produits éligibles
        boolean panierEligible = false;
        List<String> categoriesEligibles = new ArrayList<>();
        
        // Récupérer les catégories éligibles de tous les contrats actifs
        for (Contrat contrat : commerceSelectionne.getContrats()) {
            if (contrat.estActif()) {
                categoriesEligibles.addAll(contrat.getListeCatProduits());
            }
        }
        
        // Vérifier si au moins un produit du panier est éligible
        for (Produit produit : panierList) {
            if (categoriesEligibles.contains(produit.getCategorie())) {
                panierEligible = true;
                break;
            }
        }
        
        if (!panierEligible) {
            afficherAlerte(
                "Panier non éligible", 
                "Votre panier ne contient aucun produit éligible aux bons d'achat.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Appliquer le bon d'achat
        bonAchatSelectionne = bonAchat;
        bonsUtilises = bonAchat.getMontant();
        lblBonsUtilises.setText(String.format("%.2f €", bonsUtilises));
        
        // Mettre à jour le total à payer
        updateTotalPanier();
        
        // Afficher confirmation
        afficherAlerte(
            "Bon appliqué", 
            "Le bon d'achat de " + bonAchat.getMontant() + "€ a été appliqué à votre panier.",
            Alert.AlertType.INFORMATION
        );
    }
    
    private void effectuerPaiement() {
        if (panierList.isEmpty()) {
            afficherAlerte(
                "Panier vide", 
                "Votre panier est vide. Veuillez ajouter des produits avant de procéder au paiement.",
                Alert.AlertType.WARNING
            );
            return;
        }
        
        // Vérifier si le panier contient des produits éligibles si un bon d'achat est utilisé
        if (bonAchatSelectionne != null) {
            boolean panierEligible = false;
            List<String> categoriesEligibles = new ArrayList<>();
            
            // Récupérer les catégories éligibles de tous les contrats actifs
            for (Contrat contrat : commerceSelectionne.getContrats()) {
                if (contrat.estActif()) {
                    categoriesEligibles.addAll(contrat.getListeCatProduits());
                }
            }
            
            // Vérifier si au moins un produit du panier est éligible
            for (Produit produit : panierList) {
                if (categoriesEligibles.contains(produit.getCategorie())) {
                    panierEligible = true;
                    break;
                }
            }
            
            if (!panierEligible) {
                afficherAlerte(
                    "Panier non éligible", 
                    "Votre panier ne contient aucun produit éligible aux bons d'achat.",
                    Alert.AlertType.WARNING
                );
                return;
            }
        }
        
        // Calculer le montant à payer
        double montantAPayer = Math.max(0, totalPanier - bonsUtilises);
        
        // Confirmation de paiement
        Optional<ButtonType> result = afficherConfirmation(
            "Confirmation de paiement",
            "Êtes-vous sûr de vouloir procéder au paiement ?",
            "Montant total : " + String.format("%.2f €", totalPanier) + "\n" +
            "Bons utilisés : " + String.format("%.2f €", bonsUtilises) + "\n" +
            "Montant à payer : " + String.format("%.2f €", montantAPayer)
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Traitement du paiement
            // Dans un cas réel, vous enregistreriez la vente dans la base de données
            
            // Si un bon d'achat a été utilisé, le supprimer
            if (bonAchatSelectionne != null) {
                try {
                    // Supprimer la relation entre le compte et le bon d'achat
                    convertirDAO.deleteConversion(compteCourant.getId(), bonAchatSelectionne.getId());
                    
                    // Enregistrer l'utilisation du bon d'achat
                    utiliserDAO.insertUtilisation(bonAchatSelectionne.getId(), commerceSelectionne.getIdCommerce());
                    
                    // Mettre à jour la liste des bons d'achat
                    bonsAchatList.remove(bonAchatSelectionne);
                    
                    // Mettre à jour l'affichage
                    double totalBons = bonsAchatList.stream().mapToDouble(BonAchat::getMontant).sum();
                    lblBonsDisponibles.setText(String.format("%.2f €", totalBons));
                    
                } catch (Exception e) {
                    System.err.println("Erreur lors de la suppression du bon d'achat : " + e.getMessage());
                }
            }
            
            // Vider le panier
            panierList.clear();
            
            // Réinitialiser les variables
            bonAchatSelectionne = null;
            bonsUtilises = 0.0;
            lblBonsUtilises.setText("0.00 €");
            
            // Mettre à jour l'affichage
            updateTotalPanier();
            
            // Retourner à l'onglet des commerces
            tabPane.getSelectionModel().select(0);
            
            // Afficher confirmation
            afficherAlerte(
                "Paiement effectué", 
                "Votre paiement a été effectué avec succès.",
                Alert.AlertType.INFORMATION
            );
        }
    }
    
    private void naviguerVers(String destination) {
        System.out.println("Navigation vers: " + destination);
        // À implémenter selon votre système de navigation
    }
    
    private void deconnecter() {
        System.out.println("Déconnexion...");
        // À implémenter selon votre système d'authentification
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