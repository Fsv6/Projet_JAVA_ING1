package tri.dao;

import tri.logic.Compte;
import tri.utils.DatabaseConnection;

import java.sql.*;

public class CompteDAO {

    public Compte getById(int id) {
        String sql = "SELECT * FROM Compte WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Compte(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("code_acces"),
                        rs.getInt("points_fidelite")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCompte(Compte compte) {
        String sql = "INSERT INTO Compte (nom, prenom, code_acces, points_fidelite) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, compte.getNom());
            stmt.setString(2, compte.getPrenom());
            stmt.setInt(3, compte.getCodeAcces());
            stmt.setInt(4, compte.getNbPointsFidelite());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    compte.setId(generatedKeys.getInt(1)); // met à jour l'id dans l'objet Java
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

