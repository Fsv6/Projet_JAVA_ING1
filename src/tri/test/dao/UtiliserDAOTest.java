package tri.test.dao;

import tri.dao.UtiliserDAO;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UtiliserDAOTest {

    public static void main(String[] args) {
        UtiliserDAO dao = new UtiliserDAO();

        System.out.println("=== Test UtiliserDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage préalable
            st.executeUpdate("DELETE FROM utiliser");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idBonAchat = 1;
        int idCommerce = 1; // Supposé exister en base

        // 1. Enregistrement de l'utilisation d'un bon
        dao.insertUtilisation(idBonAchat, idCommerce);
        System.out.println("Insertion réussie pour bon " + idBonAchat + " dans commerce " + idCommerce);

        // 2. Lecture : commerces pour un bon
        List<Integer> commerces = dao.getCommercesByBonAchat(idBonAchat);
        System.out.println("Commerces récupérés pour bon " + idBonAchat + " : " + commerces);

        // 3. Suppression de l'utilisation
        dao.deleteUtilisation(idBonAchat, idCommerce);
        System.out.println("Suppression réussie.");

        // 4. Vérification après suppression
        commerces = dao.getCommercesByBonAchat(idBonAchat);
        System.out.println("Commerces après suppression : " + commerces);
    }
}

