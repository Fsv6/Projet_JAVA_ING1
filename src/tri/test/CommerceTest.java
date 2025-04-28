package tri.test;

import tri.logic.Commerce;
import tri.logic.Produit;
import tri.logic.Contrat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommerceTest {
    public static void main(String[] args) {
        System.out.println("=== Test de la classe Commerce ===");

        // Création d'un commerce
        Commerce commerce = new Commerce(1,"SuperMarché");

        // Ajout de produits
        Produit p1 = new Produit(1,"ALIMENTAIRE", "Pomme", 2);
        Produit p2 = new Produit(2,"ELECTRONIQUE", "Radio", 30);
        commerce.ajouterProduit(p1);
        commerce.ajouterProduit(p2);

        if (commerce.getProduits().size() == 2) {
            System.out.println("Ajout des produits OK");
        } else {
            System.out.println("Erreur dans l'ajout des produits");
        }

        // Création d'un contrat : autorise la catégorie ALIMENTAIRE
        Contrat contrat = new Contrat(LocalDate.now().minusDays(1), LocalDate.now().plusDays(30),
                new ArrayList<>(Arrays.asList("ALIMENTAIRE")));

        commerce.ajouterContrat(contrat);

        // Test de validité du bon pour un panier
        List<Produit> panier = new ArrayList<>();
        panier.add(p1); // Pomme, catégorie ALIMENTAIRE, donc éligible
        panier.add(p2); // Radio, non éligible

        if (commerce.verifierBonAchat(panier)) {
            System.out.println("✅ Le bon d'achat est valide pour au moins un produit du panier");
        } else {
            System.out.println("❌ Erreur dans la validation du bon d'achat pour le panier");
        }
    }
}
