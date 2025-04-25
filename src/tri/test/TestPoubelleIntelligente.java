package MainTestUnitaire;

import Poubelle.Bac;
import Poubelle.Dechet;
import Poubelle.PoubelleIntelligente;
import Poubelle.TypeDechet;
import Poubelle.Depot;

public class TestPoubelleIntelligente {
    public static void main(String[] args) {
    System.out.println("\n=== Test de la classe PoubelleIntelligente ===");
    PoubelleIntelligente poubelle = new PoubelleIntelligente();

    Bac bac1 = new Bac();
    bac1.setCapaciteMax(100);
    bac1.setPoidsActuel(100);
    bac1.setTypeDechet(TypeDechet.VERRE);

    Bac bac2 = new Bac();
    bac2.setCapaciteMax(100);
    bac2.setPoidsActuel(20);
    bac2.setTypeDechet(TypeDechet.PAPIER);

 // Création de déchets
    Dechet d1 = new Dechet(TypeDechet.CARTON, 2);
    Dechet d2 = new Dechet(TypeDechet.PAPIER, 1);

    // Création du dépôt
    Depot depot1 = new Depot();
    Depot depot2 = new Depot();
    depot1.ajouterDechet(d1);
    depot2.ajouterDechet(d2);

    // Ajout du dépôt dans la poubelle intelligente
    poubelle.ajouterDepot(depot1);
    poubelle.ajouterDepot(depot2);

    poubelle.vérifierBacPlein(); // Doit afficher qu’un bac est plein

    // Test localisation 
    poubelle.setLatitudeEmplacement(46.519653f);
    poubelle.setLongitudeEmplacement(6.632273f);
    System.out.println("Latitude : " + poubelle.getLatitudeEmplacement()); // Attendu : 46.519653
    System.out.println("Longitude : " + poubelle.getLongitudeEmplacement()); // Attendu : 6.632273
	}
}

