package tri.test.dao;

import tri.dao.BonAchatDAO;
import tri.logic.BonAchat;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BonAchatDAOTest {

    public static void main(String[] args) {
        BonAchatDAO dao = new BonAchatDAO();

        System.out.println("=== Test BonAchatDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM bonachat");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        BonAchat bon = new BonAchat(50);
        dao.insertBonAchat(bon);
        System.out.println("Insertion réussie : " + bon);

        BonAchat bonLu = dao.getBonAchatById(bon.getId());
        System.out.println("Bon lu en base : " + bonLu);

        if (bon.getId() == bonLu.getId()) {
            System.out.println("Test OK : Id cohérent entre Java et BDD (" + bon.getId() + ")");
        } else {
            System.out.println("Test FAILED : Id incohérent !");
        }

        dao.deleteBonAchat(bon.getId());
        System.out.println("Suppression réussie du bon d'achat.");
    }
}
