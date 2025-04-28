package tri.dao;

import tri.utils.DatabaseConnection;
import java.sql.*;

public class CodeAccesDAO {


    public void insertCodeAcces(String code, int idPoubelle) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO codesacces (code, id_poubelle) VALUES ('" + code + "', " + idPoubelle + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du code d'accès : " + e.getMessage(), e);
        }
    }

    public boolean verifierCodeAcces(String code) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM codesacces WHERE code = '" + code + "'")) {

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du code d'accès : " + e.getMessage(), e);
        }
    }

    public int getIdPoubelleByCode(String code) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_poubelle FROM codesacces WHERE code = '" + code + "'")) {

            if (rs.next()) {
                return rs.getInt("id_poubelle");
            } else {
                throw new RuntimeException("Aucune poubelle trouvée pour le code : " + code);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'id de la poubelle : " + e.getMessage(), e);
        }
    }

    // Update
    public void updateCodeAcces(int idPoubelle, String newCode) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "UPDATE codesacces SET code = '" + newCode + "' WHERE id_poubelle = " + idPoubelle;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du code d'accès : " + e.getMessage(), e);
        }
    }

    public void deleteCodeAcces(int idPoubelle) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM codesacces WHERE id_poubelle = " + idPoubelle;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du code d'accès : " + e.getMessage(), e);
        }
    }
}
