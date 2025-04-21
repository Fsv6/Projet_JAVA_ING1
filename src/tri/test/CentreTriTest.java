package tri.test;

import tri.logic.Commerce;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CentreTriTest {
    public static void main(String[] args) {
        // Création du centre de tri
        CentreTri centre = new CentreTri();
        centre.setId(1);
        centre.setNom("Centre de Tri Nord");
        centre.setNumRue(12);
        centre.setNomRue("Rue des Écologistes");
        centre.setVille("Écopolis");
        centre.setCodePostal(75000);

        // Vérification des setters
        if (centre.getId() == 1 && centre.getNom().equals("Centre de Tri Nord") &&
            centre.getNumRue() == 12 && centre.getNomRue().equals("Rue des Écologistes") &&
            centre.getVille().equals("Écopolis") && centre.getCodePostal() == 75000) {
            System.out.println("Données du centre bien initialisées");
        } else {
            System.out.println("Problème dans les données");
        }

        // Ajout d'une poubelle
        centre.placerPoubelle("Quartier Vert", 45, 90);

        // Vérification que la poubelle a bien été ajoutée
        if (centre.getPoubelles().size() == 1) {
            System.out.println("Poubelle ajoutée");
        } else {
            System.out.println("Poubelle non ajoutée");
        }

        // Création d’un commerce fictif
        Commerce commerce = new Commerce(1001, "SuperBio", "12 Rue Verte", "Écopolis");

        // Liste de catégories
        List<String> categories = new ArrayList<>();
        categories.add("Plastique");
        categories.add("Papier");

        // Signature d’un contrat
        Date dateDebut = new Date(); // maintenant
        Date dateFin = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365); // dans 1 an

        centre.signerContrat(commerce, dateDebut, dateFin, categories);

        // Vérification de la signature du contrat
        if (centre.getContrats().size() == 1) {
            System.out.println("Contrat signé");
        } else {
            System.out.println("Problème lors de la signature");
        }

        // Affichage final des statistiques
        centre.faireStatistiques();
    }
}
