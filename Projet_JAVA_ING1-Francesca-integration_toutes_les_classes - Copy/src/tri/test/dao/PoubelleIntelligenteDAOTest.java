package tri.test.dao;

import tri.dao.PoubelleIntelligenteDAO;
import tri.logic.PoubelleIntelligente;
import tri.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PoubelleIntelligenteDAOTest {

    public static void main(String[] args) {
        PoubelleIntelligenteDAO dao = new PoubelleIntelligenteDAO();


        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM poubelleintelligente");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur de nettoyage : " + e.getMessage(), e);
        }

        int idPoubelle = 1;
        int idCentreTri = 1;

        PoubelleIntelligente poubelle = new PoubelleIntelligente(idPoubelle, "Quartier Nord", 48.8566f, 2.3522f);
        dao.insertPoubelle(poubelle, idCentreTri);
        System.out.println("Insertion réussie de la poubelle " + idPoubelle);

        PoubelleIntelligente poubelleLue = dao.getPoubelleById(idPoubelle);
        System.out.println("Poubelle lue : id=" + poubelleLue.getId() +
                ", quartier=" + poubelleLue.getNomQuartier() +
                ", latitude=" + poubelleLue.getLatitudeEmplacement() +
                ", longitude=" + poubelleLue.getLongitudeEmplacement());

        dao.deletePoubelle(idPoubelle);
        System.out.println("Suppression réussie de la poubelle.");

        try {
            dao.getPoubelleById(idPoubelle);
        } catch (RuntimeException e) {
            System.out.println("Vérification OK : poubelle bien supprimée. (" + e.getMessage() + ")");
        }
    }
}
