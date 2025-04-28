package tri.test;

import tri.logic.BonAchat;

public class BonAchatTest {

    public static void main(String[] args) {
        // Test de création de bons d'achat
        BonAchat bon1 = new BonAchat(5);
        BonAchat bon2 = new BonAchat(5);

        System.out.println("Test 1 - Vérification montant : ");
        System.out.println(bon1.getMontant() == 5 && bon2.getMontant() == 5 ? "OK" : "Erreur");

        System.out.println("Test 2 - Vérification ID unique : ");
        System.out.println(bon1.getId() != bon2.getId() ? "OK" : "Erreur");

        System.out.println("Test 3 - Affichage toString : ");
        System.out.println((bon1.toString() != null && bon2.toString() != null) ? "OK" : "Erreur");

        System.out.println("Test 4 - Vérification de l'égalité entre les bons : ");
        System.out.println((!bon1.equals(bon2)) ? "OK" : "Erreur");

        System.out.println("Bon 1 : " + bon1);
        System.out.println("Bon 2 : " + bon2);
    }
}