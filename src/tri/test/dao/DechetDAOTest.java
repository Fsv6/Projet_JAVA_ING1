package tri.test.dao;

import tri.dao.DechetDAO;
import tri.logic.Dechet;
import tri.logic.TypeDechet;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DechetDAOTest {

    public static void main(String[] args) {
        DechetDAO dao = new DechetDAO();

        System.out.println("=== Test DechetDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage préalable
            st.executeUpdate("DELETE FROM dechet");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idDechet = 1;
        int idDepot = 1; // Supposé exister

        // 1. Création d'un déchet
        Dechet dechet = new Dechet(idDechet, TypeDechet.PLASTIQUE, 5);
        dao.insertDechet(dechet, idDepot);
        System.out.println("Insertion réussie pour déchet " + idDechet);

        // 2. Lecture du déchet
        Dechet dechetLu = dao.getDechetById(idDechet);
        System.out.println("Déchet lu : id=" + dechetLu.getIdDechet() +
                ", type=" + dechetLu.getType() +
                ", poids=" + dechetLu.getPoids());

        // 3. Suppression du déchet
        dao.deleteDechet(idDechet);
        System.out.println("Suppression réussie du déchet.");

        // 4. Vérification
        try {
            dao.getDechetById(idDechet);
        } catch (RuntimeException e) {
            System.out.println("Vérification OK : déchet bien supprimé. (" + e.getMessage() + ")");
        }
    }
}

