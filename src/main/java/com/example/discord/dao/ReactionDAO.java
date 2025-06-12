package com.example.discord.dao;

import com.example.discord.model.Reaction;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReactionDAO {

    public List<Reaction> findAll() {
        List<Reaction> result = new ArrayList<>();
        String sql = "SELECT * FROM Reaction";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Reaction(
                        rs.getString("NomUtilisateur"),
                        rs.getInt("IdMessage"),
                        rs.getString("typeReaction")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur findAll Reaction : " + e.getMessage());
        }

        return result;
    }

    public boolean insert(Reaction r) {
        String sql = "INSERT INTO Reaction (NomUtilisateur, IdMessage, typeReaction) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getNomUtilisateur());
            stmt.setInt(2, r.getIdMessage());
            stmt.setString(3, r.getTypeReaction());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert Reaction : " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String nomUtilisateur, int idMessage) {
        String sql = "DELETE FROM Reaction WHERE NomUtilisateur = ? AND IdMessage = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setInt(2, idMessage);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur delete Reaction : " + e.getMessage());
            return false;
        }
    }

    public boolean addReaction(String nomUtilisateur, int idMessage, String typeReaction) {
        String sql = "INSERT INTO reaction (nomutilisateur, idmessage, typereaction) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setInt(2, idMessage);
            stmt.setString(3, typeReaction);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur insert reaction : " + e.getMessage());
            return false;
        }
    }

    public List<Reaction> getReactionsForMessage(int idMessage) {
        List<Reaction> reactions = new ArrayList<>();

        String sql = "SELECT nomutilisateur, typereaction FROM reaction WHERE idmessage = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMessage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reaction r = new Reaction();
                r.setNomUtilisateur(rs.getString("nomutilisateur"));
                r.setTypeReaction(rs.getString("typereaction"));
                r.setIdMessage(idMessage);
                reactions.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Erreur getReactionsForMessage : " + e.getMessage());
        }

        return reactions;
    }

    public String getExistingReactionType(String nomUtilisateur, int idMessage) {
        String sql = "SELECT typeReaction FROM Reaction WHERE nomUtilisateur = ? AND idMessage = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            stmt.setInt(2, idMessage);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("typeReaction");
            }

        } catch (SQLException e) {
            System.out.println("Erreur getExistingReactionType : " + e.getMessage());
        }

        return null; // aucune réaction existante
    }

    public boolean upsertReaction(String nomUtilisateur, int idMessage, String typeReaction) {
        String existing = getExistingReactionType(nomUtilisateur, idMessage);

        if (existing == null) {
            // aucune réaction → insérer
            return addReaction(nomUtilisateur, idMessage, typeReaction);
        } else if (existing.equals(typeReaction)) {
            // même réaction cliquée → supprimer
            return delete(nomUtilisateur, idMessage);
        } else {
            // autre réaction → mettre à jour
            return updateReaction(nomUtilisateur, idMessage, typeReaction);
        }
    }

    public boolean updateReaction(String nomUtilisateur, int idMessage, String newType) {
        String sql = "UPDATE Reaction SET typeReaction = ? WHERE nomUtilisateur = ? AND idMessage = ?";
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newType);
            stmt.setString(2, nomUtilisateur);
            stmt.setInt(3, idMessage);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur updateReaction : " + e.getMessage());
            return false;
        }
    }
}