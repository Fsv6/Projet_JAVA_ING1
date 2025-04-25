package MainTestUnitaire;

import Poubelle.Bac;
import Poubelle.TypeDechet;

public class TestBac {
    public static void main(String[] args) {
        System.out.println("=== Test de la classe Bac ===");

        Bac bac = new Bac();
        bac.setCapaciteMax(100);
        bac.setPoidsActuel(0);
        bac.setTypeDechet(TypeDechet.AUTRE);

        System.out.println("Poids actuel : " + bac.getPoidsActuel()); // 0
        bac.ajouterDechet(50);
        System.out.println("Poids après ajout : " + bac.getPoidsActuel()); // 50

        System.out.println("Est plein ? " + bac.estPlein()); // false car on est a 50/100
        bac.ajouterDechet(60); // Devrait refuser car dépasse 100
        System.out.println("Poids actuel : " + bac.getPoidsActuel()); //doit garder 50 et pas 110

        bac.setPoidsActuel(100);
        System.out.println("Est plein ? " + bac.estPlein()); // true car forcément, 50 + 60 > 100
    }
}