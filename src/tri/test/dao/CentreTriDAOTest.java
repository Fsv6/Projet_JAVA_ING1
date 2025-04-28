package tri.test.dao;

import tri.dao.CentreTriDAO;
import tri.logic.CentreTri;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CentreTriDAOTest {

    public static void main(String[] args) {
        CentreTriDAO dao = new CentreTriDAO();

        System.out.println("=== Test CentreTriDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage
            st.executeUpdate("DELETE FROM centretri");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        // 1. Création d'un centre de tri
        CentreTri centre = new CentreTri(1, "Centre Nord", 12, "Rue des Fleurs", "Paris", 75000);
        dao.insertCentreTri(centre);
        System.out.println("Insertion réussie : " + centre.getNomCentreTri());

        // 2. Lecture
        CentreTri centreLu = dao.getCentreTriById(1);
        System.out.println("Centre lu : id=" + centreLu.getIdCentreTri() + ", nom=" + centreLu.getNomCentreTri() +
                ", adresse : " + centreLu.getNumRue() + " " + centreLu.getNomRue() + ", " + centreLu.getVille() + " " + centreLu.getCodePostal());

        // 3. Suppression
        dao.deleteCentreTri(1);
        System.out.println("Suppression réussie.");
    }
}

