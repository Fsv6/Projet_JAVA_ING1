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

            // Nettoyage préalable
            st.executeUpdate("DELETE FROM convertir");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idCompte = 1;
        int idBonAchat = 1;

        // 1. Insertion d'une conversion
        dao.insertConversion(idCompte, idBonAchat);
        System.out.println("Insertion réussie pour compte " + idCompte + " et bon " + idBonAchat);

        // 2. Lecture des bons d'un compte
        List<Integer> bons = dao.getBonAchatsByCompte(idCompte);
        System.out.println("Bons récupérés pour le compte " + idCompte + " : " + bons);

        // 3. Suppression de la conversion
        dao.deleteConversion(idCompte, idBonAchat);
        System.out.println("Suppression réussie pour compte " + idCompte + " et bon " + idBonAchat);

        // 4. Vérification après suppression
        bons = dao.getBonAchatsByCompte(idCompte);
        System.out.println("Bons après suppression : " + bons);
    }
}
