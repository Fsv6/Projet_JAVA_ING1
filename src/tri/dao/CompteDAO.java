package tri.dao;

import java.sql.*;
import tri.logic.Compte;
import tri.utils.DatabaseConnection;

public class CompteDAO {

    public void insertCompte(Compte compte) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO compte (idCompte, nomResponsable, prenomResponsable, nbPointsFidelite) VALUES ("
                    + compte.getId() + ", '"
                    + compte.getNom() + "', '"
                    + compte.getPrenom() + "', "
                    + compte.getNbPointsFidelite() + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du compte : " + e.getMessage(), e);
        }
    }

    public Compte getCompteById(int idCompte) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM compte WHERE idCompte = " + idCompte)) {

            if (rs.next()) {
                return new Compte(
                        rs.getInt("idCompte"),
                        rs.getString("nomResponsable"),
                        rs.getString("prenomResponsable"),
                        rs.getInt("nbPointsFidelite")
                );
            } else {
                throw new RuntimeException("Aucun compte trouvé avec l'id " + idCompte);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du compte : " + e.getMessage(), e);
        }
    }

    public void updatePointsFidelite(int idCompte, int nouveauxPoints) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "UPDATE compte SET nbPointsFidelite = " + nouveauxPoints + " WHERE idCompte = " + idCompte;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des points de fidélité : " + e.getMessage(), e);
        }
    }

    public void deleteCompte(int idCompte) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM compte WHERE idCompte = " + idCompte;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du compte : " + e.getMessage(), e);
        }
    }
}
