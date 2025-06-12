package com.example.discord.dao;

import com.example.discord.model.InvitationCanal;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationCanalDAO {

    public List<InvitationCanal> findAll() {
        List<InvitationCanal> result = new ArrayList<>();
        String sql = "SELECT * FROM InvitationCanal";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new InvitationCanal(
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomEspace"),
                        rs.getString("emailinvitation"),
                        rs.getString("statut")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll InvitationCanal : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(InvitationCanal ic) {
        String sql = "INSERT INTO InvitationCanal (NomUtilisateur, NomEspace, emailinvitation, statut) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ic.getNomUtilisateur());
            stmt.setString(2, ic.getNomEspace());
            stmt.setString(3, ic.getEmailInvitation());
            stmt.setString(4, ic.getStatut());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert InvitationCanal : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomUtilisateur, String nomEspace) {
        String sql = "DELETE FROM InvitationCanal WHERE NomUtilisateur = ? AND NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, nomEspace);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete InvitationCanal : " + e.getMessage());
            return false;
        }
    }
}