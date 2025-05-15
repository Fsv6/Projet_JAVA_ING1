package tri.test.dao;

import tri.dao.VendreDAO;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class VendreDAOTest {

    public static void main(String[] args) {
        VendreDAO dao = new VendreDAO();

        System.out.println("=== Test VendreDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage préalable
            st.executeUpdate("DELETE FROM vendre");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idCommerce = 1;
        int idProduit = 1; // Supposé exister en base

        // 1. Enregistrement de la vente
        dao.insertVente(idCommerce, idProduit);
        System.out.println("Insertion réussie : commerce " + idCommerce + " vend produit " + idProduit);

        // 2. Lecture : produits vendus par un commerce
        List<Integer> produits = dao.getProduitsByCommerce(idCommerce);
        System.out.println("Produits vendus par commerce " + idCommerce + " : " + produits);

        // 3. Lecture (optionnel) : commerces vendant un produit
        List<Integer> commerces = dao.getCommercesByProduit(idProduit);
        System.out.println("Commerces qui vendent le produit " + idProduit + " : " + commerces);

        // 4. Suppression
        dao.deleteVente(idCommerce, idProduit);
        System.out.println("Suppression réussie de la vente.");

        // 5. Vérification après suppression
        produits = dao.getProduitsByCommerce(idCommerce);
        System.out.println("Produits après suppression : " + produits);
    }
}
