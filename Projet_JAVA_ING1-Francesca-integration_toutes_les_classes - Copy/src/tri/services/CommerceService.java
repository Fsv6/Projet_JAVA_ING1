package tri.services;

import tri.dao.CommerceDAO;
import tri.dao.ContratDAO;
import tri.dao.ProduitDAO;
import tri.logic.Commerce;
import tri.logic.Contrat;
import tri.logic.Produit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les commerces et leurs produits
 */
public class CommerceService {

    private CommerceDAO commerceDAO;
    private ProduitDAO produitDAO;
    private ContratDAO contratDAO;
    
    /**
     * Constructeur du service
     * Initialise les DAO nécessaires
     */
    public CommerceService() {
        this.commerceDAO = new CommerceDAO();
        this.produitDAO = new ProduitDAO();
        this.contratDAO = new ContratDAO();
    }
    
    /**
     * Récupère tous les commerces avec leurs produits
     * @return Liste des commerces
     */
    public List<Commerce> getAllCommerces() {
        // Cette méthode devrait récupérer tous les commerces depuis la base de données
        // Puis pour chaque commerce, récupérer ses produits et contrats
        
        // Pour le moment, on crée des données statiques
        List<Commerce> commerces = new ArrayList<>();
        commerces.add(createCommerce(1, "Carrefour"));
        commerces.add(createCommerce(2, "Auchan"));
        commerces.add(createCommerce(3, "Leclerc"));
        commerces.add(createCommerce(4, "Boutique Bio"));
        commerces.add(createCommerce(5, "Intermarché"));
        commerces.add(createCommerce(6, "Magasin Vert"));
        
        return commerces;
    }
    
    /**
     * Récupère un commerce par son ID
     * @param idCommerce Identifiant du commerce
     * @return Le commerce avec ses produits et contrats
     */
    public Commerce getCommerceById(int idCommerce) {
        try {
            Commerce commerce = commerceDAO.getCommerceById(idCommerce);
            
            // Charger les produits du commerce
            // Dans une implémentation réelle, vous récupéreriez les produits depuis la base de données
            // Par exemple avec une méthode comme produitDAO.getProduitsByCommerce(idCommerce)
            
            // Pour le moment, on ajoute des produits statiques
            ajouterProduitsStatiques(commerce);
            
            // Charger les contrats du commerce
            // Dans une implémentation réelle, vous récupéreriez les contrats depuis la base de données
            // Par exemple avec une méthode comme contratDAO.getContratsByCommerce(idCommerce)
            
            // Pour le moment, on ajoute un contrat statique
            ajouterContratStatique(commerce);
            
            return commerce;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du commerce: " + e.getMessage(), e);
        }
    }
    
    /**
     * Recherche les commerces par nom
     * @param nom Nom ou partie du nom à rechercher
     * @return Liste des commerces correspondants
     */
    public List<Commerce> rechercherCommerces(String nom) {
        List<Commerce> tousCommerces = getAllCommerces();
        
        if (nom == null || nom.isEmpty()) {
            return tousCommerces;
        }
        
        String recherche = nom.toLowerCase();
        
        return tousCommerces.stream()
                          .filter(c -> c.getNom().toLowerCase().contains(recherche))
                          .collect(Collectors.toList());
    }
    
    /**
     * Vérifie si un commerce accepte un bon d'achat pour un panier de produits
     * @param commerce Le commerce concerné
     * @param panier Liste des produits dans le panier
     * @return true si le bon d'achat peut être utilisé, false sinon
     */
    public boolean verifierAcceptationBonAchat(Commerce commerce, List<Produit> panier) {
        // Vérifier si le commerce a au moins un contrat actif
        boolean aContratActif = commerce.getContrats().stream()
                                      .anyMatch(Contrat::estActif);
        
        if (!aContratActif) {
            return false;
        }
        
        // Vérifier si au moins un produit du panier est éligible selon les contrats
        return commerce.verifierBonAchat(panier);
    }
    
    /**
     * Récupère les catégories de produits disponibles pour un commerce
     * @param commerce Le commerce concerné
     * @return Liste des catégories de produits
     */
    public List<String> getCategoriesProduits(Commerce commerce) {
        return commerce.getProduits().stream()
                     .map(Produit::getCategorie)
                     .distinct()
                     .collect(Collectors.toList());
    }
    
    // Méthodes utilitaires pour créer des données statiques
    
    private Commerce createCommerce(int id, String nom) {
        Commerce commerce = new Commerce(id, nom);
        ajouterProduitsStatiques(commerce);
        ajouterContratStatique(commerce);
        return commerce;
    }
    
    private void ajouterProduitsStatiques(Commerce commerce) {
        // Liste des catégories possibles
        List<String> categories = Arrays.asList("Alimentaire", "Électronique", "Vêtements", "Maison", "Loisirs");
        
        // Noms de produits par catégorie
        List<String> alimentaire = Arrays.asList("Pain", "Lait", "Fromage", "Yaourt", "Jus d'orange", "Café", "Thé", "Céréales");
        List<String> electronique = Arrays.asList("Smartphone", "Tablette", "Écouteurs", "Chargeur", "Clé USB", "Batterie externe");
        List<String> vetements = Arrays.asList("T-shirt", "Pantalon", "Chaussettes", "Pull", "Veste", "Écharpe", "Gants");
        List<String> maison = Arrays.asList("Assiette", "Verre", "Couteau", "Serviette", "Coussin", "Cadre photo", "Vase");
        List<String> loisirs = Arrays.asList("Livre", "Jeu de société", "Ballon", "Raquette", "DVD", "Puzzle", "Carte");
        
        // Nombre de produits par commerce (entre 5 et 15)
        int nbProduits = 5 + (int)(Math.random() * 10);
        
        for (int i = 0; i < nbProduits; i++) {
            // Sélection aléatoire d'une catégorie
            String categorie = categories.get((int)(Math.random() * categories.size()));
            
            // Sélection d'un nom de produit en fonction de la catégorie
            String nom;
            switch (categorie) {
                case "Alimentaire":
                    nom = alimentaire.get((int)(Math.random() * alimentaire.size())) + " " + commerce.getNom();
                    break;
                case "Électronique":
                    nom = electronique.get((int)(Math.random() * electronique.size())) + " " + commerce.getNom();
                    break;
                case "Vêtements":
                    nom = vetements.get((int)(Math.random() * vetements.size())) + " " + commerce.getNom();
                    break;
                case "Maison":
                    nom = maison.get((int)(Math.random() * maison.size())) + " " + commerce.getNom();
                    break;
                case "Loisirs":
                    nom = loisirs.get((int)(Math.random() * loisirs.size())) + " " + commerce.getNom();
                    break;
                default:
                    nom = "Produit " + (i + 1) + " " + commerce.getNom();
            }
            
            // Prix aléatoire entre 5€ et 100€
            double prix = Math.round((5 + Math.random() * 95) * 100) / 100.0;
            
            // Création du produit et ajout au commerce
            Produit produit = new Produit(100 * commerce.getIdCommerce() + i, categorie, nom, prix);
            commerce.ajouterProduit(produit);
        }
    }
    
    private void ajouterContratStatique(Commerce commerce) {
        // Création d'un contrat valide pour les prochains mois
        LocalDate debut = LocalDate.now().minusMonths(1);
        LocalDate fin = LocalDate.now().plusMonths(6);
        
        // Catégories de produits éligibles (2 ou 3 catégories aléatoires)
        List<String> categories = Arrays.asList("Alimentaire", "Électronique", "Vêtements", "Maison", "Loisirs");
        Collections.shuffle(categories);
        List<String> categoriesEligibles = categories.subList(0, 2 + (int)(Math.random() * 2));
        
        // Création du contrat
        Contrat contrat = new Contrat(debut, fin, categoriesEligibles);
        commerce.ajouterContrat(contrat);
    }
}