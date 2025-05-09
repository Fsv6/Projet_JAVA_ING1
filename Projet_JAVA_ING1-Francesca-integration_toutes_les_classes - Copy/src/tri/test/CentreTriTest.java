package tri.test;

import tri.logic.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CentreTriTest {

    public static void main(String[] args) {
        System.out.println("\n=== Test de la classe CentreTri ===");


        CentreTri centre = new CentreTri(1, "Centre de Tri Nord", 12, "Rue des Écologistes", "Écopolis", 75000);


        System.out.println("Test 1 - Vérification des données du centre : ");
        System.out.println(centre.getIdCentreTri() == 1 && centre.getNomCentreTri().equals("Centre de Tri Nord") &&
                centre.getNumRue() == 12 && centre.getNomRue().equals("Rue des Écologistes") &&
                centre.getVille().equals("Écopolis") && centre.getCodePostal() == 75000 ? "OK" : "Erreur");

        PoubelleIntelligente poubelle = new PoubelleIntelligente(1, "Poubelle de Tri",48,45);

        System.out.println("Test 2 - Ajouter une poubelle : ");
        centre.ajouterPoubelle(poubelle);
        System.out.println(centre.getPoubelles().size() == 1 ? "OK" : "Erreur");

        System.out.println("Test 3 - Retirer une poubelle : ");
        centre.retirerPoubelle(poubelle);
        System.out.println(centre.getPoubelles().size() == 0 ? "OK" : "Erreur");

        Commerce commerce = new Commerce(1001, "SuperBio");

        List<String> categories = new ArrayList<>();
        categories.add("Plastique");
        categories.add("Papier");

        LocalDate dateDebut = LocalDate.now();
        LocalDate dateFin = LocalDate.now().plusYears(1);
        System.out.println("Test 4 - Signature d’un contrat : ");
        centre.signerContrat(commerce, dateDebut, dateFin, categories);
        System.out.println(centre.getContrats().size() == 1 ? "OK" : "Erreur");

        System.out.println("Test 5 - Renouvellement d’un contrat : ");
        centre.renouvelerContrat(1, LocalDate.now().plusYears(2));
        System.out.println(centre.getContrats().get(0).getDateFin().equals(LocalDate.now().plusYears(2)) ? "OK" : "Erreur");

        System.out.println("Test 6 - Résiliation d’un contrat : ");
        centre.resilierContrat(1);
        System.out.println(centre.getContrats().isEmpty() ? "OK" : "Erreur");

        Bac bac = new Bac(1,100, List.of(TypeDechet.PAPIER));
        poubelle.ajouterBac(bac);
        System.out.println("Test 7 - Vidage du bac : ");
        centre.viderBac(poubelle, bac);
        System.out.println(bac.getPoidsActuel() == 0 ? "OK" : "Erreur");

    }
}
