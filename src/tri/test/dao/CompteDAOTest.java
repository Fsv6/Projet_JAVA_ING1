package tri.test.dao;

import tri.dao.CompteDAO;
import tri.logic.Compte;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CompteDAOTest {

    public static void main(String[] args) {
        CompteDAO dao = new CompteDAO();

        System.out.println("=== Test CompteDAO ===");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Nettoyage pour commencer proprement
            st.executeUpdate("DELETE FROM compte");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        // 1. Création d'un compte
        Compte compte = new Compte(1, "Dupont", "Jean", 100);
        dao.insertCompte(compte);
        System.out.println("Insertion réussie : " + compte.getNom() + " " + compte.getPrenom());

        // 2. Lecture du compte
        Compte compteLu = dao.getCompteById(1);
        System.out.println("Compte lu : id=" + compteLu.getId() + ", nom=" + compteLu.getNom() +
                ", prénom=" + compteLu.getPrenom() + ", points=" + compteLu.getNbPointsFidelite());

        // 3. Mise à jour des points
        dao.updatePointsFidelite(1, 150);
        Compte compteMisAJour = dao.getCompteById(1);
        System.out.println("Après mise à jour : points=" + compteMisAJour.getNbPointsFidelite());


        dao.deleteCompte(1);
        System.out.println("Suppression réussie du compte.");
    }
}
