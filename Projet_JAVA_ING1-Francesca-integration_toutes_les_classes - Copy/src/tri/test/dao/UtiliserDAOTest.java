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

            st.executeUpdate("DELETE FROM utiliser");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idBonAchat = 1;
        int idCommerce = 1;

        dao.insertUtilisation(idBonAchat, idCommerce);
        System.out.println("Insertion réussie pour bon " + idBonAchat + " dans commerce " + idCommerce);

        List<Integer> commerces = dao.getCommercesByBonAchat(idBonAchat);
        System.out.println("Commerces récupérés pour bon " + idBonAchat + " : " + commerces);

        dao.deleteUtilisation(idBonAchat, idCommerce);
        System.out.println("Suppression réussie.");

        commerces = dao.getCommercesByBonAchat(idBonAchat);
        System.out.println("Commerces après suppression : " + commerces);
    }
}

