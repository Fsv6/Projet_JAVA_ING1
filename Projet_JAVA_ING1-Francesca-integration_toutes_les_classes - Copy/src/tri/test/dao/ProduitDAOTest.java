package tri.test.dao;

import tri.dao.ProduitDAO;
import tri.logic.Produit;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProduitDAOTest {

    public static void main(String[] args) {
        ProduitDAO dao = new ProduitDAO();

        System.out.println("=== Test ProduitDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM produit");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idProduit = 1;

        Produit produit = new Produit(idProduit, "alimentaire", "Bouteille d''eau", 1.99);
        dao.insertProduit(produit);
        System.out.println("Insertion réussie pour produit " + idProduit);

        Produit produitLu = dao.getProduitById(idProduit);
        System.out.println("Produit lu : id=" + produitLu.getIdProduit() +
                ", nom=" + produitLu.getNom() +
                ", catégorie=" + produitLu.getCategorie() +
                ", prix=" + produitLu.getPrix());

        dao.deleteProduit(idProduit);
        System.out.println("Suppression réussie du produit.");

        try {
            dao.getProduitById(idProduit);
        } catch (RuntimeException e) {
            System.out.println("Vérification OK : produit bien supprimé. (" + e.getMessage() + ")");
        }
    }
}
