package tri.test.dao;


import tri.dao.CommerceDAO;
import tri.logic.Commerce;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CommerceDAOTest {

    public static void main(String[] args) {
        CommerceDAO dao = new CommerceDAO();

        System.out.println("=== Test CommerceDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM commerce");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        Commerce commerce = new Commerce(1, "Super U");
        dao.insertCommerce(commerce);
        System.out.println("Insertion réussie : " + commerce.getNom());

        Commerce commerceLu = dao.getCommerceById(1);
        System.out.println("Commerce lu : id=" + commerceLu.getIdCommerce() + ", nom=" + commerceLu.getNom());

        dao.deleteCommerce(1);
        System.out.println("Suppression réussie.");
    }
}

