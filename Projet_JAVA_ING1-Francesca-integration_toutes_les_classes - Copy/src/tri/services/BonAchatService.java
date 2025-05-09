package tri.services;

import tri.controllers.BonAchatController.HistoriqueUtilisation;
import tri.dao.BonAchatDAO;
import tri.dao.CommerceDAO;
import tri.dao.CompteDAO;
import tri.dao.ConvertirDAO;
import tri.dao.UtiliserDAO;
import tri.logic.BonAchat;
import tri.logic.Commerce;
import tri.logic.Compte;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les bons d'achat
 * Implémente la logique métier liée aux bons d'achat
 */
public class BonAchatService {

    private BonAchatDAO bonAchatDAO;
    private CompteDAO compteDAO;
    private CommerceDAO commerceDAO;
    private ConvertirDAO convertirDAO;
    private UtiliserDAO utiliserDAO;
    
    // Constantes pour les coûts de conversion des points
    private static final int COUT_BON_5_EUROS = 500;
    private static final int COUT_BON_15_EUROS = 1400;
    private static final int COUT_BON_30_EUROS = 2700;
    
    /**
     * Constructeur du service
     * Initialise les DAO nécessaires
     */
    public BonAchatService() {
        this.bonAchatDAO = new BonAchatDAO();
        this.compteDAO = new CompteDAO();
        this.commerceDAO = new CommerceDAO();
        this.convertirDAO = new ConvertirDAO();
        this.utiliserDAO = new UtiliserDAO();
    }
    
    /**
     * Crée un nouveau bon d'achat pour un compte
     * @param idCompte Identifiant du compte
     * @param montant Montant du bon d'achat
     * @return Le bon d'achat créé
     * @throws IllegalArgumentException Si le montant n'est pas valide ou si le compte n'a pas assez de points
     */
    public BonAchat creerBonAchat(int idCompte, int montant) throws IllegalArgumentException {
        // Vérification du montant du bon
        if (montant != 5 && montant != 15 && montant != 30) {
            throw new IllegalArgumentException("Le montant du bon d'achat doit être de 5€, 15€ ou 30€");
        }
        
        // Vérification de l'existence du compte
        Compte compte;
        try {
            compte = compteDAO.getCompteById(idCompte);
        } catch (Exception e) {
            throw new IllegalArgumentException("Compte non trouvé");
        }
        
        // Vérification des points de fidélité
        int coutPoints = calculerCoutPoints(montant);
        if (compte.getNbPointsFidelite() < coutPoints) {
            throw new IllegalArgumentException("Points de fidélité insuffisants");
        }
        
        // Création du bon d'achat
        BonAchat bonAchat = new BonAchat(montant);
        
        // Enregistrement en base de données
        bonAchatDAO.insertBonAchat(bonAchat);
        
        // Création de la relation entre le compte et le bon d'achat
        convertirDAO.insertConversion(idCompte, bonAchat.getId());
        
        // Mise à jour des points du compte
        int nouveauxPoints = compte.getNbPointsFidelite() - coutPoints;
        compteDAO.updatePointsFidelite(idCompte, nouveauxPoints);
        
        return bonAchat;
    }
    
    /**
     * Calcule le coût en points d'un bon d'achat
     * @param montant Montant du bon d'achat
     * @return Coût en points
     * @throws IllegalArgumentException Si le montant n'est pas valide
     */
    public int calculerCoutPoints(int montant) throws IllegalArgumentException {
        switch (montant) {
            case 5:
                return COUT_BON_5_EUROS;
            case 15:
                return COUT_BON_15_EUROS;
            case 30:
                return COUT_BON_30_EUROS;
            default:
                throw new IllegalArgumentException("Montant non valide");
        }
    }
    
    /**
     * Récupère tous les bons d'achat d'un compte
     * @param idCompte Identifiant du compte
     * @return Liste des bons d'achat
     */
    public List<BonAchat> getBonsAchatCompte(int idCompte) {
        List<Integer> bonIds = convertirDAO.getBonAchatsByCompte(idCompte);
        List<BonAchat> bons = new ArrayList<>();
        
        for (Integer id : bonIds) {
            try {
                BonAchat bon = bonAchatDAO.getBonAchatById(id);
                bons.add(bon);
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du bon d'achat " + id + " : " + e.getMessage());
            }
        }
        
        return bons;
    }
    
    /**
     * Utilise un bon d'achat dans un commerce spécifique
     * @param idBon Identifiant du bon d'achat
     * @param idCompte Identifiant du compte
     * @param idCommerce Identifiant du commerce
     * @return true si l'opération a réussi, false sinon
     */
    public boolean utiliserBonAchat(int idBon, int idCompte, int idCommerce) {
        try {
            // Vérification de l'existence du bon d'achat
            BonAchat bonAchat = bonAchatDAO.getBonAchatById(idBon);
            
            // Vérification de l'existence du commerce
            Commerce commerce = commerceDAO.getCommerceById(idCommerce);
            
            // Enregistrement de l'utilisation
            utiliserDAO.insertUtilisation(idBon, idCommerce);
            
            // Suppression de la relation convertir (car le bon est utilisé)
            convertirDAO.deleteConversion(idCompte, idBon);
            
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'utilisation du bon: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Vérifie si un compte a suffisamment de points pour un bon d'achat
     * @param idCompte Identifiant du compte
     * @param montant Montant du bon d'achat
     * @return true si le compte a suffisamment de points, false sinon
     */
    public boolean verifierPointsSuffisants(int idCompte, int montant) {
        try {
            Compte compte = compteDAO.getCompteById(idCompte);
            int coutPoints = calculerCoutPoints(montant);
            return compte.getNbPointsFidelite() >= coutPoints;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Récupère l'historique d'utilisation des bons d'achat
     * @param idCompte Identifiant du compte
     * @return Liste de l'historique d'utilisation
     */
    public List<HistoriqueUtilisation> getHistoriqueUtilisation(int idCompte) {
        // Cette méthode nécessiterait une requête SQL complexe ou plusieurs requêtes
        // Pour le moment, on simule les données
        
        List<HistoriqueUtilisation> historique = new ArrayList<>();
        
        // En implémentation réelle, vous feriez quelque chose comme :
        // 1. Récupérer tous les bons d'achat du compte qui ont été utilisés
        // 2. Pour chaque bon, récupérer le commerce où il a été utilisé
        // 3. Créer un objet HistoriqueUtilisation pour chaque utilisation
        
        // Simulation de données pour le moment
        historique.add(new HistoriqueUtilisation(1, 5, "Carrefour", LocalDateTime.now().minusDays(2)));
        historique.add(new HistoriqueUtilisation(2, 15, "Auchan", LocalDateTime.now().minusDays(5)));
        historique.add(new HistoriqueUtilisation(3, 30, "Leclerc", LocalDateTime.now().minusDays(10)));
        
        return historique;
    }
    
    /**
     * Récupère la liste des commerces disponibles
     * @return Liste des commerces
     */
    public List<Commerce> getCommerces() {
        // À implémenter avec CommerceDAO
        // Pour le moment, on retourne une liste statique
        List<Commerce> commerces = new ArrayList<>();
        commerces.add(new Commerce(1, "Carrefour"));
        commerces.add(new Commerce(2, "Auchan"));
        commerces.add(new Commerce(3, "Leclerc"));
        commerces.add(new Commerce(4, "Boutique Bio"));
        return commerces;
    }
}