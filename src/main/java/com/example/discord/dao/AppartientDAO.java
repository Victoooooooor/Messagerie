package com.example.discord.dao;

import com.example.discord.model.Appartient;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppartientDAO {

    public List<Appartient> findAll() {
        List<Appartient> result = new ArrayList<>();
        String sql = "SELECT * FROM Appartient";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Appartient(
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomEspace")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll Appartient : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(Appartient a) {
        String sql = "INSERT INTO Appartient (NomUtilisateur, NomEspace) VALUES (?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getNomUtilisateur());
            stmt.setString(2, a.getNomEspace());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert Appartient : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomUtilisateur, String nomEspace) {
        String sql = "DELETE FROM Appartient WHERE NomUtilisateur = ? AND NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, nomEspace);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete Appartient : " + e.getMessage());
            return false;
        }
    }
}