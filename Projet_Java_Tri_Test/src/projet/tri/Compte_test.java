package projet.tri;

public class Compte_test {
    public static void main(String[] args) {
        Compte compte = new Compte();

        compte.setNom("Durand");
        compte.setPrenom("Lucie");
        compte.setCodeAcces(9876);
        compte.setId(1);
        compte.setNbPointsFidelite(25);

        boolean ok = true;
        if (!compte.getNom().equals("Arondeau")) ok = false;
        if (!compte.getPrenom().equals("Matheo")) ok = false;
        if (compte.getCodeAcces() != 9876) ok = false;
        if (compte.getId() != 1) ok = false;
        if (compte.getNbPointsFidelite() != 25) ok = false;

        if (ok) {
            System.out.println("Test Compte valide");
        } else {
            System.out.println("Test Compte non valide");
        }
    }
}
