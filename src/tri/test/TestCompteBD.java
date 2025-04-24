package tri.test;

import tri.dao.CompteDAO;
import tri.logic.Compte;

public class TestCompteBD {
    public static void main(String[] args) {
        CompteDAO dao = new CompteDAO();

        Compte nouveau = new Compte(0, "Lina", "Durand", 123456, 100);
        boolean success = dao.insertCompte(nouveau);

        if (success) {
            System.out.println("✅ Compte inséré, ID = " + nouveau.getId());
        }

        Compte recup = dao.getById(nouveau.getId());
        if (recup != null) {
            System.out.println("👉 Compte récupéré : " + recup.getNom() + " " + recup.getPrenom());
        }
    }
}

