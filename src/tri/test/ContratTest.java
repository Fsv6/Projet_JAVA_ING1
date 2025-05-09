package tri.test;

import tri.logic.Contrat;
import tri.logic.Produit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ContratTest {
    public static void main(String[] args) {

        Contrat contrat = new Contrat(1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(5),
                new ArrayList<>(Arrays.asList("ALIMENTAIRE", "HYGIENE")));

        if (contrat.estActif()) {
            System.out.println("Le contrat est actif");
        } else {
            System.out.println("Erreur");
        }

        Produit produitEligible = new Produit("ALIMENTAIRE", "Banane", 1);
        Produit produitNonEligible = new Produit("ELECTRONIQUE", "Téléphone", 500);

        if (contrat.estProduitEligible(produitEligible) && !contrat.estProduitEligible(produitNonEligible)) {
            System.out.println("Test d'éligibilité des produits OK");
        } else {
            System.out.println("Erreur dans la vérification de l'éligibilité des produits");
        }
    }
}
