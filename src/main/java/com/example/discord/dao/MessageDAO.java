package com.example.discord.dao;

import com.example.discord.model.Message;
import com.example.discord.util.ConnexionBD;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public int getNextIdMessage() {
        String sql = "SELECT COALESCE(MAX(idmessage), 0) + 1 AS next_id FROM message";
        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

        } catch (SQLException e) {
            System.out.println("Erreur getNextIdMessage : " + e.getMessage());
        }
        return 1;
    }

    public boolean insert(Message msg) {
        String sql = "INSERT INTO Message (idmessage, contenu, time_, NomUtilisateur, NomUtilisateur_1, NomUtilisateur_2, NomCanal) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, msg.getIdMessage());
            stmt.setString(2, msg.getContenu());
            stmt.setTime(3, msg.getTime_());
            stmt.setString(4, msg.getNomUtilisateur());
            stmt.setString(5, msg.getNomUtilisateur1());
            stmt.setString(6, msg.getNomUtilisateur2());

            if (msg.getNomCanal() == null) {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(7, msg.getNomCanal());
            }

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
        String sql = "UPDATE Message SET contenu = ?, NomUtilisateur = ?, NomCanal = ? WHERE IdMessage = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, msg.getContenu());
            stmt.setString(2, msg.getNomUtilisateur());

            if (msg.getNomCanal() == null) {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(3, msg.getNomCanal());
            }

            stmt.setInt(4, msg.getIdMessage());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur update Message : " + e.getMessage());
            return false;
        }
    }


    public List<Message> findByCanal(String nomCanal) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT IdMessage, contenu, time_, NomUtilisateur, NomCanal FROM Message WHERE NomCanal = ? ORDER BY time_";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomCanal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Message m = new Message();
                m.setIdMessage(rs.getInt("IdMessage"));
                m.setContenu(rs.getString("contenu"));
                m.setTime_(rs.getTime("time_"));
                m.setNomUtilisateur(rs.getString("NomUtilisateur"));
                m.setNomCanal(rs.getString("NomCanal"));
                messages.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Erreur findByCanal : " + e.getMessage());
        }

        return messages;
    }

    public List<Message> findDirectMessages(String from, String to) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT idmessage, contenu, time_, nomutilisateur, nomcanal " +
                "FROM message " +
                "WHERE nomcanal IS NULL AND ( " +
                "      (nomutilisateur_1 = ? AND nomutilisateur_2 = ?) " +
                "   OR (nomutilisateur_1 = ? AND nomutilisateur_2 = ?) " +
                ") " +
                "ORDER BY time_";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, from);
            stmt.setString(2, to);
            stmt.setString(3, to);
            stmt.setString(4, from);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Message msg = new Message();
                msg.setIdMessage(rs.getInt("idmessage"));
                msg.setContenu(rs.getString("contenu"));
                msg.setTime_(rs.getTime("time_"));
                msg.setNomUtilisateur(rs.getString("NomUtilisateur"));
                msg.setNomCanal(rs.getString("NomCanal"));
                messages.add(msg);
            }

        } catch (SQLException e) {
            System.out.println("Erreur findDirectMessages : " + e.getMessage());
        }

        return messages;
    }
}