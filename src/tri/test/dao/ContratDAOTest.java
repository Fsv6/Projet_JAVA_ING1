package tri.test.dao;

import tri.dao.ContratDAO;
import tri.logic.Contrat;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;

public class ContratDAOTest {

    public static void main(String[] args) {
        ContratDAO dao = new ContratDAO();

        System.out.println("=== Test ContratDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage
            st.executeUpdate("DELETE FROM contrat");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        // 1. Création d'un contrat
        Contrat contrat = new Contrat(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                Arrays.asList("alimentaire", "électronique")
        );
        dao.insertContrat(contrat, 1, 1);
        System.out.println("Insertion réussie.");

        // 2. Lecture du contrat
        Contrat contratLu = dao.getContrat(1, 1);
        System.out.println("Contrat lu : début=" + contratLu.getDateDebut() + ", fin=" + contratLu.getDateFin()
                + ", catégories=" + contratLu.getListeCatProduits());

        // 3. Suppression du contrat
        dao.deleteContrat(1, 1);
        System.out.println("Suppression réussie.");
    }
}
