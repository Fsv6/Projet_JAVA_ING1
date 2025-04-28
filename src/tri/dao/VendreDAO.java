package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.utils.DatabaseConnection;

public class VendreDAO {

    // Create : enregistrer qu'un commerce vend un produit
    public void insertVente(int idCommerce, int idProduit) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO vendre (idCommerce, idProduit) VALUES ("
                    + idCommerce + ", "
                    + idProduit + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans vendre : " + e.getMessage(), e);
        }
    }

    // Read : récupérer tous les produits vendus par un commerce
    public List<Integer> getProduitsByCommerce(int idCommerce) {
        List<Integer> produits = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idProduit FROM vendre WHERE idCommerce = " + idCommerce)) {

            while (rs.next()) {
                produits.add(rs.getInt("idProduit"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits vendus par le commerce : " + e.getMessage(), e);
        }
        return produits;
    }

    // Read (optionnel) : récupérer tous les commerces qui vendent un produit
    public List<Integer> getCommercesByProduit(int idProduit) {
        List<Integer> commerces = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idCommerce FROM vendre WHERE idProduit = " + idProduit)) {

            while (rs.next()) {
                commerces.add(rs.getInt("idCommerce"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commerces pour le produit : " + e.getMessage(), e);
        }
        return commerces;
    }

    // Delete : supprimer une vente (optionnel)
    public void deleteVente(int idCommerce, int idProduit) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM vendre WHERE idCommerce = " + idCommerce + " AND idProduit = " + idProduit;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression dans vendre : " + e.getMessage(), e);
        }
    }
}
