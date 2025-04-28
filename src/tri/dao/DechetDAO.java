package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.logic.Dechet;
import tri.logic.TypeDechet;
import tri.utils.DatabaseConnection;

public class DechetDAO {

    public void insertDechet(Dechet dechet, int idDepot) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO dechet (idDechet, typeDechet, poids, idDepot) VALUES ("
                    + dechet.getIdDechet() + ", '"
                    + dechet.getType().name() + "', "
                    + dechet.getPoids() + ", "
                    + idDepot + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du déchet : " + e.getMessage(), e);
        }
    }

    public Dechet getDechetById(int idDechet) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM dechet WHERE idDechet = " + idDechet)) {

            if (rs.next()) {
                return new Dechet(
                        rs.getInt("idDechet"),
                        TypeDechet.valueOf(rs.getString("typeDechet")),
                        rs.getInt("poids")
                );
            } else {
                throw new RuntimeException("Aucun déchet trouvé avec l'id " + idDechet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du déchet : " + e.getMessage(), e);
        }
    }

    public List<Dechet> getDechetsByDepot(int idDepot) {
        List<Dechet> dechets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM dechet WHERE idDepot = " + idDepot)) {

            while (rs.next()) {
                Dechet dechet = new Dechet(
                        rs.getInt("idDechet"),
                        TypeDechet.valueOf(rs.getString("typeDechet")),
                        rs.getInt("poids")
                );
                dechets.add(dechet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des déchets : " + e.getMessage(), e);
        }
        return dechets;
    }

    public void deleteDechet(int idDechet) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM dechet WHERE idDechet = " + idDechet;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du déchet : " + e.getMessage(), e);
        }
    }
}
