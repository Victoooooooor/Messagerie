package com.example.discord.dao;

import com.example.discord.model.Administre;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministreDAO {

    public List<Administre> findAll() {
        List<Administre> result = new ArrayList<>();
        String sql = "SELECT * FROM Administre";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Administre(
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomEspace")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll Administre : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(Administre admin) {
        String sql = "INSERT INTO Administre (NomUtilisateur, NomEspace) VALUES (?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNomUtilisateur());
            stmt.setString(2, admin.getNomEspace());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert Administre : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomUtilisateur, String nomEspace) {
        String sql = "DELETE FROM Administre WHERE NomUtilisateur = ? AND NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, nomEspace);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete Administre : " + e.getMessage());
            return false;
        }
    }
}