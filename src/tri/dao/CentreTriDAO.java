package tri.dao;

import java.sql.*;
import tri.logic.CentreTri;
import tri.utils.DatabaseConnection;

public class CentreTriDAO {

    public void insertCentreTri(CentreTri centreTri) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO centretri (idCentreTri, nomCentreTri, numRue, nomRue, ville, codePostal) VALUES ("
                    + centreTri.getIdCentreTri() + ", '"
                    + centreTri.getNomCentreTri() + "', "
                    + centreTri.getNumRue() + ", '"
                    + centreTri.getNomRue() + "', '"
                    + centreTri.getVille() + "', "
                    + centreTri.getCodePostal() + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du centre de tri : " + e.getMessage(), e);
        }
    }

    public CentreTri getCentreTriById(int idCentreTri) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM centretri WHERE idCentreTri = " + idCentreTri)) {

            if (rs.next()) {
                return new CentreTri(
                        rs.getInt("idCentreTri"),
                        rs.getString("nomCentreTri"),
                        rs.getInt("numRue"),
                        rs.getString("nomRue"),
                        rs.getString("ville"),
                        rs.getInt("codePostal")
                );
            } else {
                throw new RuntimeException("Aucun centre de tri trouvé avec l'id " + idCentreTri);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du centre de tri : " + e.getMessage(), e);
        }
    }

    public void deleteCentreTri(int idCentreTri) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM centretri WHERE idCentreTri = " + idCentreTri;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du centre de tri : " + e.getMessage(), e);
        }
    }
}

