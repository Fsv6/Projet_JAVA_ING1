package tri.test;

import tri.logic.Depot;
import tri.logic.Dechet;
import tri.logic.TypeDechet;

import java.util.ArrayList;
import java.util.Date;

public class DepotTest {
    public static void main(String[] args) {
        System.out.println("=== Test de la classe Depot ===");

        Depot depot = new Depot();
        depot.setDateDepot(new Date());

        ArrayList<Dechet> dechets = new ArrayList<>();
        dechets.add(new Dechet(TypeDechet.VERRE, 500));
        dechets.add(new Dechet(TypeDechet.VERRE, 300));

        // Ajout des déchets un par un pour tester la méthode ajouterDechet()
        for (Dechet d : dechets) {
            depot.ajouterDechet(d);
        }

        depot.setPointsAttribues(3);

        boolean testOK = true;

        if (depot.getListeDechet().size() != 2) {
            System.out.println("Mauvais nombre de déchets");
            testOK = false;
        }

        if (depot.getPoidsDechet() != 800) {
            System.out.println("Calcul du poids incorrect (attendu 800, obtenu " + depot.getPoidsDechet() + ")");
            testOK = false;
        }

        if (depot.getPointsAttribues() != 3) {
            System.out.println("Points attribués incorrects");
            testOK = false;
        }

        Date date = new Date();
        depot.setDateDepot(date);
        if (!depot.getDateDepot().equals(date)) {
            System.out.println("Date de dépôt incorrecte");
            testOK = false;
        }

        if (testOK) {
            System.out.println("Tous les tests du dépôt sont valides !");
        } else {
            System.out.println("Certains tests ont échoué.");
        }
    }
}
