package tri.test.dao;

import tri.dao.DepotDAO;
import tri.logic.Depot;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class DepotDAOTest {

    public static void main(String[] args) {
        DepotDAO dao = new DepotDAO();

        System.out.println("=== Test DepotDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage préalable
            st.executeUpdate("DELETE FROM depot");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idDepot = 1;
        int idCompte = 1; // Supposé exister en base
        int idPoubelle = 1; // Supposé exister aussi

        // 1. Création d'un dépôt
        Depot depot = new Depot(idDepot, 50, new Date(), new ArrayList<>());
        dao.insertDepot(depot, idCompte, idPoubelle);
        System.out.println("Insertion réussie pour dépôt " + idDepot);

        // 2. Lecture du dépôt
        Depot depotLu = dao.getDepotById(idDepot);
        System.out.println("Depot lu : id=" + depotLu.getIdDepot() +
                ", points=" + depotLu.getPointsAttribues() +
                ", date=" + depotLu.getDateDepot());

        // 3. Suppression du dépôt
        dao.deleteDepot(idDepot);
        System.out.println("Suppression réussie du dépôt.");

        // 4. Vérification
        try {
            dao.getDepotById(idDepot);
        } catch (RuntimeException e) {
            System.out.println("Vérification OK : dépôt bien supprimé. (" + e.getMessage() + ")");
        }
    }
}

