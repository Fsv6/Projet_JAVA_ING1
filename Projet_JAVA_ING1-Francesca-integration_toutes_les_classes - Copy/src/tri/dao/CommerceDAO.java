package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tri.logic.Commerce;
import tri.utils.DatabaseConnection;

public class CommerceDAO {

    public void insertCommerce(Commerce commerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO commerce (idCommerce, nom) VALUES ("
                    + commerce.getIdCommerce() + ", '"
                    + commerce.getNom() + "')";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du commerce : " + e.getMessage(), e);
        }
    }

    public Commerce getCommerceById(int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM commerce WHERE idCommerce = " + idCommerce)) {

            if (rs.next()) {
                return new Commerce(
                        rs.getInt("idCommerce"),
                        rs.getString("nom")
                );
            } else {
                throw new RuntimeException("Aucun commerce trouvé avec l'id " + idCommerce);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du commerce : " + e.getMessage(), e);
        }
    }

    public void deleteCommerce(int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM commerce WHERE idCommerce = " + idCommerce;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du commerce : " + e.getMessage(), e);
        }
    }
    public List<Commerce> getAllCommerces() {
        List<Commerce> commerces = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM commerce")) {
            while (rs.next()) {
                Commerce commerce = new Commerce(
                        rs.getInt("idCommerce"),
                        rs.getString("nom")
                );
                commerces.add(commerce);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commerces : " + e.getMessage(), e);
        }
        return commerces;
    }
}

