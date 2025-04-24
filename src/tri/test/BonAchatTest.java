package tri.test;
import tri.logic.BonAchat;

public class BonAchatTest {
    public static void main(String[] args) {

        BonAchat bon1 = new BonAchat(5);
        BonAchat bon2 = new BonAchat(5);

        if (bon1.getMontant() == 5 && bon2.getMontant() == 5 && bon1.getId() != bon2.getId()) {
            System.out.println("✅ Création des bons d'achat OK");
        } else {
            System.out.println("❌ Erreur dans la création des bons d'achat");
        }

        System.out.println("Bon 1 : " + bon1);
        System.out.println("Bon 2 : " + bon2);
    }
}
