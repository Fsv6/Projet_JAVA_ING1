package tri.dao;


import java.sql.*;
import tri.logic.Produit;
import tri.utils.DatabaseConnection;

public class ProduitDAO {

    public void insertProduit(Produit produit) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO produit (idProduit, nomProduit, categorie, prix) VALUES ("
                    + produit.getIdProduit() + ", '"
                    + produit.getNom() + "', '"
                    + produit.getCategorie() + "', "
                    + produit.getPrix() + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du produit : " + e.getMessage(), e);
        }
    }

    public Produit getProduitById(int idProduit) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM produit WHERE idProduit = " + idProduit)) {

            if (rs.next()) {
                return new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("categorie"),
                        rs.getString("nomProduit"),
                        rs.getDouble("prix")
                );
            } else {
                throw new RuntimeException("Aucun produit trouvé avec l'id " + idProduit);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du produit : " + e.getMessage(), e);
        }
    }

    public void deleteProduit(int idProduit) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM produit WHERE idProduit = " + idProduit;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du produit : " + e.getMessage(), e);
        }
    }
}

