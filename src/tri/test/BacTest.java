package tri.test;

import tri.logic.Bac;
import tri.logic.TypeDechet;
import tri.exception.BacPleinException;
import java.util.Arrays;

public class BacTest {

    public static void main(String[] args) {
        System.out.println("\n=== Test de la classe Bac ===");

        Bac bac = new Bac(1, 100, Arrays.asList(TypeDechet.METAL, TypeDechet.PLASTIQUE));
        bac.setPoidsActuel(0);

        System.out.println("Test 1 - Ajouter déchet normal : ");
        bac.ajouterDechet(30);
        System.out.println(bac.getPoidsActuel() == 30 ? "OK" : "Erreur");

        System.out.println("Test 2 - Ajouter déchet dépasse capacité : ");
        bac.setPoidsActuel(90);
        try {
            bac.ajouterDechet(20);
            System.out.println("Erreur : Déchet ajouté malgré la capacité maximale.");
        } catch (BacPleinException e) {
            System.out.println("OK");
        }

        System.out.println("Test 3 - Est plein (faux) : ");
        bac.setPoidsActuel(50);
        System.out.println(!bac.estPlein() ? "OK" : "Erreur");

        System.out.println("Test 4 - Est plein (vrai) : ");
        bac.setPoidsActuel(100);
        System.out.println(bac.estPlein() ? "OK" : "Erreur");

        System.out.println("Test 5 - Set/Get poids actuel : ");
        bac.setPoidsActuel(60);
        System.out.println(bac.getPoidsActuel() == 60 ? "OK" : "Erreur");

        System.out.println("Test 6 - Set/Get capacité max : ");
        bac.setCapaciteMax(150);
        System.out.println(bac.getCapaciteMax() == 150 ? "OK" : "Erreur");

        System.out.println("Test 7 - Set/Get types de déchets : ");
        System.out.println(bac.getTypesDechets().contains(TypeDechet.PAPIER) && bac.getTypesDechets().contains(TypeDechet.PLASTIQUE) ? "OK" : "Erreur");

        System.out.println("Test 8 - toString : ");
        System.out.println(bac.toString() != null ? "OK" : "Erreur");
    }
}
