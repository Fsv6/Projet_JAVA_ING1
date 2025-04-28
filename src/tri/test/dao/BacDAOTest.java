package tri.test.dao;

import tri.dao.BacDAO;
import tri.logic.Bac;
import tri.logic.TypeDechet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import tri.utils.DatabaseConnection;

public class BacDAOTest {

    public static void main(String[] args) {
        BacDAO bacDAO = new BacDAO();

        System.out.println("=== Test de BacDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage préalable pour des tests propres
            st.executeUpdate("DELETE FROM bac");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        // 1. Créer un bac
        Bac bac = new Bac(1, 100, List.of(TypeDechet.PLASTIQUE));

        bac.setPoidsActuel(0);

        // 2. Insertion
        bacDAO.insertBac(bac, 1); // Supposons que la poubelle id=1 existe déjà
        System.out.println("Insertion du bac réussie.");

        // 3. Lecture
        Bac bacLu = bacDAO.getBacById(1);
        System.out.println("Bac récupéré : id=" + bacLu.getIdBac() + ", type=" + bacLu.getTypesDechets() + ", capacité=" + bacLu.getCapaciteMax() + ", poidsActuel=" + bacLu.getPoidsActuel());

        // 4. Mise à jour
        bacDAO.updatePoidsActuel(1, 50);
        Bac bacMisAJour = bacDAO.getBacById(1);
        System.out.println("Poids du bac après mise à jour : " + bacMisAJour.getPoidsActuel());


        bacDAO.deleteBac(1);
        System.out.println("Suppression du bac réussie.");

        // 6. Vérification suppression
        try {
            bacDAO.getBacById(1);
        } catch (RuntimeException e) {
            System.out.println("Bac correctement supprimé : " + e.getMessage());
        }
    }
}
