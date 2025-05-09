package tri.test;

import tri.logic.Compte;
import tri.logic.Depot;
import tri.logic.PoubelleIntelligente;
import tri.logic.TypeDechet;

import java.util.ArrayList;
import java.util.Date;

public class CompteTest {
    public static void main(String[] args) {
        System.out.println("=== Test de la classe Compte ===");

        // Création d'un compte
        Compte compte = new Compte(1, "Dupont", "Jean", 1234, 0);
        if (compte.getNom().equals("Dupont") && compte.getNbPointsFidelite() == 0) {
            System.out.println("Création du compte OK");
        } else {
            System.out.println("Erreur lors de la création du compte");
        }

        // Création d'un dépôt simulé (sans validation de la poubelle ici)
        Depot depot = new Depot();
        depot.setDateDepot(new Date());
        depot.setPointsAttribues(3); // Simuler que la poubelle a attribué 3 points

        // Réaliser le dépôt
        // Ici, pour simplifier, nous ne testons que la logique d'ajout dans l'historique et d'augmentation des points
        compte.realiserDepot(depot, new PoubelleIntelligente("PoubelleTest"), TypeDechet.METAL);
        if (compte.getHistoriqueDepot().size() == 1 && compte.getNbPointsFidelite() == 3) {
            System.out.println("Réalisation du dépôt OK");
        } else {
            System.out.println("Erreur lors de la réalisation du dépôt");
        }

        // Test de conversion en bon d'achat (si le compte possède suffisamment de points, ici 3 < 60 => pas de conversion)
        compte.convertirEnBonAchat();
        if (compte.getListBonAchat().isEmpty()) {
            System.out.println("Conversion en bon d'achat (non déclenchée comme prévu) OK");
        } else {
            System.out.println("Erreur : conversion de bon alors que le solde est insuffisant");
        }

        Compte compte2 = new Compte();

        compte.setNom("Durand");
        compte.setPrenom("Lucie");
        compte.setId(1);
        compte.setNbPointsFidelite(25);

        boolean ok = true;
        if (!compte.getNom().equals("Arondeau")) ok = false;
        if (!compte.getPrenom().equals("Matheo")) ok = false;
        if (compte.getId() != 1) ok = false;
        if (compte.getNbPointsFidelite() != 25) ok = false;

        if (ok) {
            System.out.println("Test Compte valide");
        } else {
            System.out.println("Test Compte non valide");
        }
    }
}

