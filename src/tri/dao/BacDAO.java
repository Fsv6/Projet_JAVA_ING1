package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tri.logic.Bac;
import tri.logic.TypeDechet;
import tri.utils.DatabaseConnection;

public class BacDAO {

    public void insertBac(Bac bac, int idPoubelleIntelligente) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String typesDechets = String.join(",",
                    bac.getTypesDechets()
                            .stream()
                            .map(TypeDechet::name)
                            .toArray(String[]::new)
            );

            String sql = "INSERT INTO bac (idBac, poidsActuel, capaciteMax, typeDechetBac, idPoubelleIntelligente) VALUES (" +
                    bac.getIdBac() + ", " +
                    bac.getPoidsActuel() + ", " +
                    bac.getCapaciteMax() + ", '" +
                    typesDechets + "', " +
                    idPoubelleIntelligente + ")";

            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du bac : " + e.getMessage(), e);
        }
    }


    public Bac getBacById(int idBac) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bac WHERE idBac = " + idBac)) {

            if (rs.next()) {
                String typesDechetsStr = rs.getString("typeDechet");
                List<TypeDechet> typesDechets = new ArrayList<>();
                for (String type : typesDechetsStr.split(",")) {
                    typesDechets.add(TypeDechet.valueOf(type.trim()));
                }

                Bac bac = new Bac(
                        rs.getInt("idBac"),
                        rs.getInt("capaciteMax"),
                        typesDechets
                );
                bac.setPoidsActuel(rs.getInt("poidsActuel"));
                return bac;
            } else {
                throw new RuntimeException("Aucun bac trouvé avec l'id " + idBac);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du bac : " + e.getMessage(), e);
        }
    }


    public void updatePoidsActuel(int idBac, int nouveauPoids) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "UPDATE bac SET poidsActuel = " + nouveauPoids + " WHERE idBac = " + idBac;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du poids du bac : " + e.getMessage(), e);
        }
    }

    public void deleteBac(int idBac) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            String sql = "DELETE FROM bac WHERE idBac = " + idBac;
            st.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du bac : " + e.getMessage(), e);
        }
    }

    public List<Bac> getBacsByPoubelle(int idPoubelleIntelligente) {
        List<Bac> bacs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bac WHERE idPoubelleIntelligente = " + idPoubelleIntelligente)) {

            while (rs.next()) {
                String typesDechetsStr = rs.getString("typeDechet");
                List<TypeDechet> typesDechets = new ArrayList<>();
                for (String type : typesDechetsStr.split(",")) {
                    typesDechets.add(TypeDechet.valueOf(type.trim()));
                }

                Bac bac = new Bac(
                        rs.getInt("idBac"),
                        rs.getInt("capaciteMax"),
                        typesDechets
                );
                bac.setPoidsActuel(rs.getInt("poidsActuel"));
                bacs.add(bac);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des bacs : " + e.getMessage(), e);
        }
        return bacs;
    }
    
    public void viderBac(int idBac) {
        String sql = "UPDATE bac SET poidsActuel = 0 WHERE idBac = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBac);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
