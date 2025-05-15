package tri.dao;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;
import tri.logic.BonAchat;
import tri.utils.DatabaseConnection;

public class BonAchatDAO {

    public void insertBonAchat(BonAchat bonAchat) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO bonachat (idBonAchat, montant) VALUES ("
                    + bonAchat.getId() + ", "
                    + bonAchat.getMontant() + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du bon d'achat : " + e.getMessage(), e);
        }
    }

    public BonAchat getBonAchatById(int idBonAchat) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bonachat WHERE idBonAchat = " + idBonAchat)) {

            if (rs.next()) {
                return new BonAchat(
                        rs.getInt("idBonAchat"),
                        rs.getInt("montant")
                );
            } else {
                throw new RuntimeException("Aucun bon d'achat trouvé avec l'id " + idBonAchat);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du bon d'achat : " + e.getMessage(), e);
        }
    }

    public void deleteBonAchat(int idBonAchat) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM bonachat WHERE idBonAchat = " + idBonAchat;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du bon d'achat : " + e.getMessage(), e);
        }
    }

    public List<BonAchat> getAllBonAchats() {
        List<BonAchat> bons = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bonachat")) {

            while (rs.next()) {
                BonAchat bon = new BonAchat(
                        rs.getInt("idBonAchat"),
                        rs.getInt("montant")
                );
                bons.add(bon);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des bons d'achat : " + e.getMessage(), e);
        }
        return bons;
    }
}
