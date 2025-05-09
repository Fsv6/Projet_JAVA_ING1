package tri.test;

import tri.logic.Produit;

public class ProduitTest {
    public static void main(String[] args) {

        Produit produit = new Produit(1, "ALIMENTAIRE", "Orange", 2);
        if (produit.getCategorie().equals("ALIMENTAIRE") && produit.getNom().equals("Orange") && produit.getPrix() == 2) {
            System.out.println("OK");
        } else {
            System.out.println("Erreur");
        }

        System.out.println("Produit: " + produit);
    }
}
