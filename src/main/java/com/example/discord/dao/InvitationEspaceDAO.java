package com.example.discord.dao;

import com.example.discord.model.InvitationEspace;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationEspaceDAO {

    public List<InvitationEspace> findAll() {
        List<InvitationEspace> result = new ArrayList<>();
        String sql = "SELECT * FROM InvitationEspace";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new InvitationEspace(
                        rs.getString("NomUtilisateur"),
                        rs.getString("NomEspace"),
                        rs.getString("emailinvitation"),
                        rs.getString("statut")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll InvitationEspace : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(InvitationEspace ie) {
        String sql = "INSERT INTO InvitationEspace (NomUtilisateur, NomEspace, emailinvitation, statut) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ie.getNomUtilisateur());
            stmt.setString(2, ie.getNomEspace());
            stmt.setString(3, ie.getEmailInvitation());
            stmt.setString(4, ie.getStatut());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert InvitationEspace : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomUtilisateur, String nomEspace) {
        String sql = "DELETE FROM InvitationEspace WHERE NomUtilisateur = ? AND NomEspace = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, nomEspace);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete InvitationEspace : " + e.getMessage());
            return false;
        }
    }
}