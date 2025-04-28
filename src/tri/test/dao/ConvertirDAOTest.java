package tri.test.dao;

import tri.dao.ConvertirDAO;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ConvertirDAOTest {

    public static void main(String[] args) {
        ConvertirDAO dao = new ConvertirDAO();

        System.out.println("=== Test ConvertirDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM convertir");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idCompte = 1;
        int idBonAchat = 1;

        dao.insertConversion(idCompte, idBonAchat);
        System.out.println("Insertion réussie pour compte " + idCompte + " et bon " + idBonAchat);

        List<Integer> bons = dao.getBonAchatsByCompte(idCompte);
        System.out.println("Bons récupérés pour le compte " + idCompte + " : " + bons);

        dao.deleteConversion(idCompte, idBonAchat);
        System.out.println("Suppression réussie pour compte " + idCompte + " et bon " + idBonAchat);

        bons = dao.getBonAchatsByCompte(idCompte);
        System.out.println("Bons après suppression : " + bons);
    }
}
