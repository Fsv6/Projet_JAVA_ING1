package tri.dao;

import java.sql.*;
import tri.logic.PoubelleIntelligente;
import tri.utils.DatabaseConnection;

public class PoubelleIntelligenteDAO {

    // Create : insérer une poubelle intelligente
    public void insertPoubelle(PoubelleIntelligente poubelle, int idCentreTri) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "INSERT INTO poubelleintelligente (idPoubelleIntelligente, nomQuartier, longitudeEmplacement, latitudeEmplacement, idCentreTri) VALUES ("
                    + poubelle.getId() + ", '"
                    + poubelle.getNomQuartier() + "', "
                    + poubelle.getLongitudeEmplacement() + ", "
                    + poubelle.getLatitudeEmplacement() + ", "
                    + idCentreTri + ")";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de la poubelle intelligente : " + e.getMessage(), e);
        }
    }

    // Read : récupérer une poubelle par son id
    public PoubelleIntelligente getPoubelleById(int idPoubelleIntelligente) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM poubelleintelligente WHERE idPoubelleIntelligente = " + idPoubelleIntelligente)) {

            if (rs.next()) {
                return new PoubelleIntelligente(
                        rs.getInt("idPoubelleIntelligente"),
                        rs.getString("nomQuartier"),
                        rs.getFloat("latitudeEmplacement"),
                        rs.getFloat("longitudeEmplacement")
                );
            } else {
                throw new RuntimeException("Aucune poubelle trouvée avec l'id " + idPoubelleIntelligente);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la poubelle : " + e.getMessage(), e);
        }
    }

    public void deletePoubelle(int idPoubelleIntelligente) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM poubelleintelligente WHERE idPoubelleIntelligente = " + idPoubelleIntelligente;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la poubelle intelligente : " + e.getMessage(), e);
        }
    }
}
