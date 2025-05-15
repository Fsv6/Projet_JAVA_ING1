package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.utils.DatabaseConnection;

public class ConvertirDAO {

    public void insertConversion(int idCompte, int idBonAchat) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO convertir (idCompte, idBonAchat) VALUES ("
                    + idCompte + ", "
                    + idBonAchat + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans convertir : " + e.getMessage(), e);
        }
    }

    public void deleteConversion(int idCompte, int idBonAchat) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM convertir WHERE idCompte = " + idCompte + " AND idBonAchat = " + idBonAchat;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression dans convertir : " + e.getMessage(), e);
        }
    }

    public List<Integer> getBonAchatsByCompte(int idCompte) {
        List<Integer> bons = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idBonAchat FROM convertir WHERE idCompte = " + idCompte)) {

            while (rs.next()) {
                bons.add(rs.getInt("idBonAchat"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des bons pour le compte : " + e.getMessage(), e);
        }
        return bons;
    }
}
