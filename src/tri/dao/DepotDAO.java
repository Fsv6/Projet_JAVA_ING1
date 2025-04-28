package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.logic.Depot;
import tri.utils.DatabaseConnection;

public class DepotDAO {

    public void insertDepot(Depot depot, int idCompte, int idPoubelleIntelligente) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            java.sql.Date sqlDateDepot = new java.sql.Date(depot.getDateDepot().getTime());

            String sql = "INSERT INTO depot (idDepot, poidsDechet, pointsAttribues, dateDepot, idCompte, idPoubelleIntelligente) VALUES ("
                    + depot.getIdDepot() + ", "
                    + depot.getPoidsDechet() + ", "
                    + depot.getPointsAttribues() + ", '"
                    + sqlDateDepot + "', "
                    + idCompte + ", "
                    + idPoubelleIntelligente + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du dépôt : " + e.getMessage(), e);
        }
    }

    public Depot getDepotById(int idDepot) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM depot WHERE idDepot = " + idDepot)) {

            if (rs.next()) {
                Depot depot = new Depot();
                depot.setIdDepot(rs.getInt("idDepot"));
                depot.setPointsAttribues(rs.getInt("pointsAttribues"));
                depot.setDateDepot(rs.getDate("dateDepot"));
                // Le poids total est stocké en base, mais pas besoin de le recalculer ici
                // Liste de déchets non gérée en base pour ce projet
                return depot;
            } else {
                throw new RuntimeException("Aucun dépôt trouvé avec l'id " + idDepot);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du dépôt : " + e.getMessage(), e);
        }
    }

    public void deleteDepot(int idDepot) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM depot WHERE idDepot = " + idDepot;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du dépôt : " + e.getMessage(), e);
        }
    }

    // Read all : récupérer tous les dépôts d'un compte (optionnel, utile pour historique)
    public List<Depot> getDepotsByCompte(int idCompte) {
        List<Depot> depots = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM depot WHERE idCompte = " + idCompte)) {

            while (rs.next()) {
                Depot depot = new Depot();
                depot.setIdDepot(rs.getInt("idDepot"));
                depot.setPointsAttribues(rs.getInt("pointsAttribues"));
                depot.setDateDepot(rs.getDate("dateDepot"));
                depots.add(depot);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des dépôts du compte : " + e.getMessage(), e);
        }
        return depots;
    }
}
