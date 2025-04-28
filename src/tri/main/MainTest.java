/*
tester le placement et le retirage des poubelles par le centre de tri
tester la conversion d'un bon d'achat en points de fidélité
tester l'utilisation valide d'un bon d'achat
tester l'utilisation invalide d'un bon d'achat

tester le depot d'un depot conforme et l'attribution de points de fidélité
tester le depot d'un depot non conforme et l'attribution de points de fidélité
tester l'authentification d'un utilisateur à une poubelle (peut etre deja fais dans le test du depot)
tester un depot qui n'aurait pas de place dans le bac deja plein

 */


package tri.main;

import tri.dao.*;
import tri.logic.*;

import java.util.ArrayList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("=== Début des tests ===");
        testPlacementEtRetraitPoubelle();
        testConversionPointsEnBon();
        System.out.println("=== Fin des tests ===");
    }

    public static void testPlacementEtRetraitPoubelle() {
        System.out.println("\n[Test] Placement et Retrait de Poubelle");

        CentreTriDAO centreDAO = new CentreTriDAO();
        PoubelleIntelligenteDAO poubelleDAO = new PoubelleIntelligenteDAO();

        CentreTri centre = null;
        PoubelleIntelligente poubelle = null;

        try {
            // 1. Création du Centre
            centre = new CentreTri(1, "Centre Test", 10, "Rue Test", "Paris", 75001);
            centreDAO.insertCentreTri(centre);
            System.out.println("Centre créé avec succès. ID : " + centre.getIdCentreTri());

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du centre : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // 2. Création de la Poubelle
            poubelle = new PoubelleIntelligente(0, "Quartier Test", 48.8566F, 2.3522F);
            poubelleDAO.insertPoubelle(poubelle, centre.getIdCentreTri());
            System.out.println("Poubelle créée avec succès. ID : " + poubelle.getId());

            // 3. Liaison de la poubelle au centre
            centre.ajouterPoubelle(poubelle); // AJOUT côté Java

            System.out.println("Poubelle ajoutée au centre.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la création ou liaison de la poubelle : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // 4. Vérification : la poubelle est bien liée au centre
            List<PoubelleIntelligente> poubellesCentre = centre.getPoubelles();
            if (poubellesCentre.contains(poubelle)) {
                System.out.println("Lien entre centre et poubelle confirmé en mémoire.");
            } else {
                System.out.println("Erreur : Poubelle non trouvée dans la liste du centre !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification de la liaison : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // 5. Suppression de la poubelle du centre
            centre.retirerPoubelle(poubelle); // RETRAIT côté Java

            System.out.println("Poubelle retirée du centre.");

            // 6. Suppression en base de la Poubelle
            poubelleDAO.deletePoubelle(poubelle.getId());
            System.out.println("Poubelle supprimée de la base.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la poubelle : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testConversionPointsEnBon() {
        System.out.println("\n[Test] Conversion progressive de deux bons d'achat");

        CompteDAO compteDAO = new CompteDAO();
        BonAchatDAO bonAchatDAO = new BonAchatDAO();

        Compte compte = null;

        try {
            // 1. Créer un compte avec suffisamment de points
            compte = new Compte(0, "TestNom", "TestPrenom", 130); // 130 points pour 2 conversions possibles
            compteDAO.create(compte);
            System.out.println("Compte créé : ID " + compte.getId() + ", points : " + compte.getNbPointsFidelite());

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // -------- Première conversion --------
            compte.convertirEnBonAchat();

            if (compte.getListBonAchat().size() >= 1) {
                BonAchat premierBon = compte.getListBonAchat().get(0);
                bonAchatDAO.create(premierBon);
                System.out.println("Premier bon créé et inséré : ID " + premierBon.getId() + ", montant " + premierBon.getMontant() + "€");
            } else {
                System.out.println("Erreur : aucun bon généré après première conversion.");
            }

            // Mise à jour du compte après première conversion
            compteDAO.update(compte);

            // -------- Deuxième conversion --------
            // Recharger le compte pour simuler un nouvel accès utilisateur
            Compte compteRecharge = compteDAO.findById(compte.getId());

            // Simuler que l'utilisateur re-clique plus tard
            compteRecharge.convertirEnBonAchat();

            if (compteRecharge.getListBonAchat().size() >= 2) {
                BonAchat deuxiemeBon = compteRecharge.getListBonAchat().get(1);
                bonAchatDAO.create(deuxiemeBon);
                System.out.println("Deuxième bon créé et inséré : ID " + deuxiemeBon.getId() + ", montant " + deuxiemeBon.getMontant() + "€");
            } else {
                System.out.println("Erreur : pas de deuxième bon généré après la deuxième conversion.");
            }

            // Mise à jour finale du compte après deuxième conversion
            compteDAO.update(compteRecharge);

            // -------- Vérification finale --------
            Compte compteFinal = compteDAO.findById(compte.getId());
            if (compteFinal.getNbPointsFidelite() == 10) {
                System.out.println("Points fidélité corrects après deux conversions progressives : " + compteFinal.getNbPointsFidelite() + " points.");
            } else {
                System.out.println("Erreur : mauvais solde de points fidélité : " + compteFinal.getNbPointsFidelite());
            }

        } catch (Exception e) {
            System.out.println("Erreur lors des conversions progressives : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Nettoyage : suppression de tous les bons et du compte
            for (BonAchat bon : compte.getListBonAchat()) {
                bonAchatDAO.delete(bon);
            }
            compteDAO.delete(compte);
            System.out.println("Nettoyage terminé.");

        } catch (Exception e) {
            System.out.println("Erreur lors du nettoyage : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void testUtilisationValideBon() {
        System.out.println("\n[Test] Utilisation valide d'un bon d'achat");

        CompteDAO compteDAO = new CompteDAO();
        CommerceDAO commerceDAO = new CommerceDAO();
        ContratDAO contratDAO = new ContratDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        BonAchatDAO bonAchatDAO = new BonAchatDAO();

        Compte compte = null;
        Commerce commerce = null;
        BonAchat bon = null;

        try {
            // 1. Créer un compte avec 60 points pour 1 bon
            compte = new Compte(0, "ValideNom", "ValidePrenom", 70);
            compteDAO.create(compte);
            compte.convertirEnBonAchat();
            bon = compte.getListBonAchat().get(0);
            bonAchatDAO.create(bon);
            compteDAO.update(compte);
            System.out.println("Compte créé avec un bon disponible.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte ou du bon : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // 2. Créer un commerce
            commerce = new Commerce(0, "SuperCommerce");
            commerceDAO.create(commerce);

            // 3. Créer un contrat pour autoriser la catégorie "Alimentaire"
            List<String> categoriesEligibles = new ArrayList<>();
            categoriesEligibles.add("Alimentaire");
            Contrat contrat = new Contrat(LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), categoriesEligibles);
            contratDAO.create(contrat);
            commerce.ajouterContrat(contrat);
            commerceDAO.update(commerce);

            // 4. Créer un produit dans la catégorie "Alimentaire"
            Produit produit = new Produit(0, "Pâtes", "Alimentaire");
            produitDAO.create(produit);
            commerce.ajouterProduit(produit);
            commerceDAO.update(commerce);

            // 5. Préparer un panier contenant ce produit
            List<Produit> panier = new ArrayList<>();
            panier.add(produit);

            // 6. Utiliser le bon
            compte.utiliserBonAchat(bon, commerce, panier);

            // 7. Vérifier que le bon a été consommé
            if (!compte.getListBonAchat().contains(bon)) {
                System.out.println("Utilisation valide du bon confirmée : bon supprimé de la liste.");
            } else {
                System.out.println("Erreur : bon toujours présent après utilisation.");
            }

        } catch (Exception e) {
            System.out.println("Erreur pendant l'utilisation du bon : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Nettoyage
            if (bon != null) {
                bonAchatDAO.delete(bon);
            }
            compteDAO.delete(compte);
            commerceDAO.delete(commerce);
            System.out.println("Nettoyage terminé.");

        } catch (Exception e) {
            System.out.println("Erreur pendant le nettoyage : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testUtilisationValideBon() {
        System.out.println("\n[Test] Utilisation valide d'un bon d'achat");

        CompteDAO compteDAO = new CompteDAO();
        CommerceDAO commerceDAO = new CommerceDAO();
        ContratDAO contratDAO = new ContratDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        BonAchatDAO bonAchatDAO = new BonAchatDAO();

        Compte compte = null;
        Commerce commerce = null;
        BonAchat bon = null;

        try {
            // 1. Créer un compte avec 60 points pour 1 bon
            compte = new Compte(0, "ValideNom", "ValidePrenom", 70);
            compteDAO.create(compte);
            compte.convertirEnBonAchat();
            bon = compte.getListBonAchat().get(0);
            bonAchatDAO.create(bon);
            compteDAO.update(compte);
            System.out.println("Compte créé avec un bon disponible.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte ou du bon : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // 2. Créer un commerce
            commerce = new Commerce(0, "SuperCommerce");
            commerceDAO.create(commerce);

            // 3. Créer un contrat pour autoriser la catégorie "Alimentaire"
            List<String> categoriesEligibles = new ArrayList<>();
            categoriesEligibles.add("Alimentaire");
            Contrat contrat = new Contrat(LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), categoriesEligibles);
            contratDAO.create(contrat);
            commerce.ajouterContrat(contrat);
            commerceDAO.update(commerce);

            // 4. Créer un produit dans la catégorie "Alimentaire"
            Produit produit = new Produit(0, "Pâtes", "Alimentaire");
            produitDAO.create(produit);
            commerce.ajouterProduit(produit);
            commerceDAO.update(commerce);

            // 5. Préparer un panier contenant ce produit
            List<Produit> panier = new ArrayList<>();
            panier.add(produit);

            // 6. Utiliser le bon
            compte.utiliserBonAchat(bon, commerce, panier);

            // 7. Vérifier que le bon a été consommé
            if (!compte.getListBonAchat().contains(bon)) {
                System.out.println("Utilisation valide du bon confirmée : bon supprimé de la liste.");
            } else {
                System.out.println("Erreur : bon toujours présent après utilisation.");
            }

        } catch (Exception e) {
            System.out.println("Erreur pendant l'utilisation du bon : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Nettoyage
            if (bon != null) {
                bonAchatDAO.delete(bon);
            }
            compteDAO.delete(compte);
            commerceDAO.delete(commerce);
            System.out.println("Nettoyage terminé.");

        } catch (Exception e) {
            System.out.println("Erreur pendant le nettoyage : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testUtilisationInvalideBon() {
        System.out.println("\n[Test] Utilisation invalide d'un bon d'achat");

        CompteDAO compteDAO = new CompteDAO();
        CommerceDAO commerceDAO = new CommerceDAO();
        ContratDAO contratDAO = new ContratDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        BonAchatDAO bonAchatDAO = new BonAchatDAO();

        Compte compte = null;
        Commerce commerce = null;
        BonAchat bon = null;

        try {
            // 1. Créer un compte avec 60 points pour 1 bon
            compte = new Compte(0, "InvalideNom", "InvalidePrenom", 70);
            compteDAO.create(compte);
            compte.convertirEnBonAchat();
            bon = compte.getListBonAchat().get(0);
            bonAchatDAO.create(bon);
            compteDAO.update(compte);
            System.out.println("Compte créé avec un bon disponible.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du compte ou du bon : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            // 2. Créer un commerce
            commerce = new Commerce(0, "CommerceInvalide");
            commerceDAO.create(commerce);

            // 3. Créer un contrat autorisant uniquement "Hygiène"
            List<String> categoriesEligibles = new ArrayList<>();
            categoriesEligibles.add("Hygiène");
            Contrat contrat = new Contrat(LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), categoriesEligibles);
            contratDAO.create(contrat);
            commerce.ajouterContrat(contrat);
            commerceDAO.update(commerce);

            // 4. Créer un produit dans une autre catégorie (ex: "Alimentaire")
            Produit produit = new Produit(0, "Chocolat", "Alimentaire");
            produitDAO.create(produit);
            commerce.ajouterProduit(produit);
            commerceDAO.update(commerce);

            // 5. Préparer un panier contenant ce mauvais produit
            List<Produit> panier = new ArrayList<>();
            panier.add(produit);

            // 6. Tenter d'utiliser le bon
            compte.utiliserBonAchat(bon, commerce, panier);

            // 7. Vérifier que le bon est toujours présent
            if (compte.getListBonAchat().contains(bon)) {
                System.out.println("Utilisation invalide confirmée : le bon est toujours dans la liste.");
            } else {
                System.out.println("Erreur : le bon a été supprimé alors que l'utilisation était invalide.");
            }

        } catch (Exception e) {
            System.out.println("Erreur pendant l'utilisation invalide du bon : " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Nettoyage
            if (bon != null) {
                bonAchatDAO.delete(bon);
            }
            compteDAO.delete(compte);
            commerceDAO.delete(commerce);
            System.out.println("Nettoyage terminé.");

        } catch (Exception e) {
            System.out.println("Erreur lors du nettoyage : " + e.getMessage());
            e.printStackTrace();
        }
    }

}

