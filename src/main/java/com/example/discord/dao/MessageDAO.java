package com.example.discord.dao;

import com.example.discord.model.Message;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> findAll() {
        List<Message> result = new ArrayList<>();
        String sql = "SELECT * FROM Message";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Message(
                        rs.getInt("IdMessage"),
                        rs.getString("contenu"),
                        rs.getString("time_"),
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomUtilisateur_1"),
                        rs.getString("NomUtilisateur_2"),
                        rs.getString("NomCanal")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll Message : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(Message msg) {
        String sql = "INSERT INTO Message (IdMessage, contenu, time_, NomUtilisateur, NomUtilisateur_1, NomUtilisateur_2, NomCanal) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, msg.getIdMessage());
            stmt.setString(2, msg.getContenu());
            stmt.setString(3, msg.getTime());
            stmt.setString(4, msg.getNomUtilisateur());
            stmt.setString(5, msg.getNomUtilisateur1());
            stmt.setString(6, msg.getNomUtilisateur2());
            stmt.setString(7, msg.getNomCanal());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert Message : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int idMessage) {
        String sql = "DELETE FROM Message WHERE IdMessage = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMessage);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete Message : " + e.getMessage());
            return false;
        }
    }

    public boolean update(Message msg) {
        String sql = "UPDATE Message SET contenu = ?, time_ = ?, NomUtilisateur = ?, NomUtilisateur_1 = ?, NomUtilisateur_2 = ?, NomCanal = ? WHERE IdMessage = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, msg.getContenu());
            stmt.setString(2, msg.getTime());
            stmt.setString(3, msg.getNomUtilisateur());
            stmt.setString(4, msg.getNomUtilisateur1());
            stmt.setString(5, msg.getNomUtilisateur2());
            stmt.setString(6, msg.getNomCanal());
            stmt.setInt(7, msg.getIdMessage());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update Message : " + e.getMessage());
            return false;
        }
    }

    public List<Message> findByCanal(String nomCanal) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE NomCanal = ? ORDER BY time_";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomCanal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("IdMessage"),
                        rs.getString("contenu"),
                        rs.getString("time_"),
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomUtilisateur_1"),
                        rs.getString("NomUtilisateur_2"),
                        rs.getString("NomCanal")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findByCanal : " + e.getMessage());
        }

        return messages;
    }

    public List<Message> findDirectMessages(String from, String to) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE " +
                "(NomUtilisateur_1 = ? AND NomUtilisateur_2 = ?) OR " +
                "(NomUtilisateur_1 = ? AND NomUtilisateur_2 = ?) ORDER BY time_";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, from);
            stmt.setString(2, to);
            stmt.setString(3, to);
            stmt.setString(4, from);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("IdMessage"),
                        rs.getString("contenu"),
                        rs.getString("time_"),
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomUtilisateur_1"),
                        rs.getString("NomUtilisateur_2"),
                        rs.getString("NomCanal")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findDirectMessages : " + e.getMessage());
        }

        return messages;
    }
}