package com.example.discord.dao;

import com.example.discord.model.Canal;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CanalDAO {

    public List<Canal> findAll() {
        List<Canal> result = new ArrayList<>();
        String sql = "SELECT * FROM Canal";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Canal(
                        rs.getString("NomCanal"),
                        rs.getString("typecanal"),
                        rs.getString("NomEspace")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll Canal : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(Canal canal) {
        String sql = "INSERT INTO Canal (NomCanal, typecanal, NomEspace) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, canal.getNomCanal());
            stmt.setString(2, canal.getTypecanal());
            stmt.setString(3, canal.getNomEspace());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert Canal : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomCanal) {
        String sql = "DELETE FROM Canal WHERE NomCanal = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomCanal);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete Canal : " + e.getMessage());
            return false;
        }
    }

    public boolean update(Canal canal) {
        String sql = "UPDATE Canal SET typecanal = ?, NomEspace = ? WHERE NomCanal = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, canal.getTypecanal());
            stmt.setString(2, canal.getNomEspace());
            stmt.setString(3, canal.getNomCanal());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update Canal : " + e.getMessage());
            return false;
        }
    }

    public boolean exists(String nomCanal) {
        String sql = "SELECT 1 FROM canal WHERE nomcanal = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomCanal);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erreur exists Canal : " + e.getMessage());
            return false;
        }
    }

    public boolean isUserInCanal(String nomUtilisateur, String nomCanal) {
        String sql = """
        SELECT 1
        FROM appartient a
        JOIN canal c ON a.nomespace = c.nomespace
        WHERE a.nomutilisateur = ? AND c.nomcanal = ?
    """;
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, nomCanal);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erreur isUserInCanal : " + e.getMessage());
            return false;
        }
    }
}