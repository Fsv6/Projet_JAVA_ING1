package tri.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import tri.logic.Contrat;
import tri.utils.DatabaseConnection;

public class ContratDAO {

    public void insertContrat(Contrat contrat, int idCentreTri, int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String listeCategories = String.join(",", contrat.getListeCatProduits());

            String sql = "INSERT INTO contrat (idCentreTri, idCommerce, dateDebutContrat, dateFinContrat, listeCatProduits) VALUES ("
                    + idCentreTri + ", "
                    + idCommerce + ", '"
                    + contrat.getDateDebut() + "', '"
                    + contrat.getDateFin() + "', '"
                    + listeCategories + "')";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du contrat : " + e.getMessage(), e);
        }
    }

    public Contrat getContrat(int idCentreTri, int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM contrat WHERE idCentreTri = " + idCentreTri + " AND idCommerce = " + idCommerce)) {

            if (rs.next()) {
                List<String> listeCatProduits = Arrays.asList(rs.getString("listeCatProduits").split(","));
                return new Contrat(
                        rs.getDate("dateDebutContrat").toLocalDate(),
                        rs.getDate("dateFinContrat").toLocalDate(),
                        listeCatProduits
                );
            } else {
                throw new RuntimeException("Aucun contrat trouvé pour idCentreTri=" + idCentreTri + " et idCommerce=" + idCommerce);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du contrat : " + e.getMessage(), e);
        }
    }


    public void deleteContrat(int idCentreTri, int idCommerce) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM contrat WHERE idCentreTri = " + idCentreTri + " AND idCommerce = " + idCommerce;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du contrat : " + e.getMessage(), e);
        }
    }
}

