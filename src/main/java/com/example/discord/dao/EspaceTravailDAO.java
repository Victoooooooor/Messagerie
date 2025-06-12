package com.example.discord.dao;

import com.example.discord.model.EspaceTravail;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceTravailDAO {

    public List<EspaceTravail> findAll() {
        List<EspaceTravail> result = new ArrayList<>();
        String sql = "SELECT * FROM EspaceTravail";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new EspaceTravail(
                        rs.getString("NomEspace"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll EspaceTravail : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(EspaceTravail e) {
        String sql = "INSERT INTO EspaceTravail (NomEspace, description) VALUES (?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNomEspace());
            stmt.setString(2, e.getDescription());
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Erreur insert EspaceTravail : " + ex.getMessage());
            return false;
        }
    }

    public boolean delete(String nomEspace) {
        String sql = "DELETE FROM EspaceTravail WHERE NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomEspace);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete EspaceTravail : " + e.getMessage());
            return false;
        }
    }

    public boolean update(EspaceTravail e) {
        String sql = "UPDATE EspaceTravail SET description = ? WHERE NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getDescription());
            stmt.setString(2, e.getNomEspace());
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Erreur update EspaceTravail : " + ex.getMessage());
            return false;
        }
    }
}