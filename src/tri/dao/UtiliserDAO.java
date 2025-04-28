package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.utils.DatabaseConnection;

public class UtiliserDAO {

    // Create : enregistrer l'utilisation d'un bon d'achat dans un commerce
    public void insertUtilisation(int idBonAchat, int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO utiliser (idBonAchat, idCommerce) VALUES ("
                    + idBonAchat + ", "
                    + idCommerce + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans utiliser : " + e.getMessage(), e);
        }
    }

    // Read : récupérer tous les commerces où un bon a été utilisé
    public List<Integer> getCommercesByBonAchat(int idBonAchat) {
        List<Integer> commerces = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idCommerce FROM utiliser WHERE idBonAchat = " + idBonAchat)) {

            while (rs.next()) {
                commerces.add(rs.getInt("idCommerce"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commerces pour le bon : " + e.getMessage(), e);
        }
        return commerces;
    }

    // Delete : (optionnel) supprimer une utilisation enregistrée
    public void deleteUtilisation(int idBonAchat, int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM utiliser WHERE idBonAchat = " + idBonAchat + " AND idCommerce = " + idCommerce;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression dans utiliser : " + e.getMessage(), e);
        }
    }
}

