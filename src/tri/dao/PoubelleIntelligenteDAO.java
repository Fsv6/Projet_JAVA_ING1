package tri.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tri.logic.Bac;
import tri.logic.PoubelleIntelligente;
import tri.logic.TypeDechet;
import tri.utils.DatabaseConnection;

public class PoubelleIntelligenteDAO {

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
    
    public List<Bac> getBacsParPoubelleId(int idPoubelle) {
        List<Bac> bacs = new ArrayList<>();
        String sql = "SELECT * FROM bac WHERE idPoubelleIntelligente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPoubelle);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Récupérer l'id du bac
                int idBac = rs.getInt("idBac"); 

                // Récupérer la capacité maximale et le poids actuel
                int capaciteMax = rs.getInt("capaciteMax");
                int poidsActuel = rs.getInt("poidsActuel");

                // Créer un objet Bac sans types de déchets pour l'instant
                Bac bac = new Bac(idBac, capaciteMax, new ArrayList<>()); // Liste vide pour l'instant

                // Récupérer les types de déchets associés à ce bac
                List<TypeDechet> typesDechets = bac.getTypesDechets(); // Appel à la méthode d'instance

                // Mettre à jour les types de déchets dans l'objet Bac
                bac.setTypesDechets(typesDechets);

                // Mettre à jour le poids actuel si nécessaire
                bac.setPoidsActuel(poidsActuel);

                // Ajouter le bac à la liste
                bacs.add(bac);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bacs;
    }



    // Nouvelle méthode pour récupérer toutes les poubelles
    public List<PoubelleIntelligente> getAllPoubelles() {
        List<PoubelleIntelligente> poubelles = new ArrayList<>();

        // 1. Récupération des poubelles
        String sql = "SELECT * FROM poubelleintelligente";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("idPoubelleIntelligente");
                String quartier = rs.getString("nomQuartier");
                float latitude = rs.getFloat("latitudeEmplacement");
                float longitude = rs.getFloat("longitudeEmplacement");

                PoubelleIntelligente poubelle = new PoubelleIntelligente(id, quartier, latitude, longitude);

                // 2. Charger les bacs associés
                List<Bac> bacs = getBacsParPoubelleId(id);
                for (Bac bac : bacs) {
                    poubelle.ajouterBac(bac);
                }

                poubelles.add(poubelle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return poubelles;
    }

}
