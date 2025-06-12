package com.example.discord.dao;

import com.example.discord.model.Utilisateur;
import com.example.discord.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";

        try (Connection conn = ConnexionBD.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getString("NomUtilisateur"),
                        rs.getString("email"),
                        rs.getString("motdepasse")
                );
                utilisateurs.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Erreur DAO (findAll): " + e.getMessage());
        }
        return utilisateurs;
    }

    public boolean insert(Utilisateur utilisateur) {
        String sql = "INSERT INTO Utilisateur (NomUtilisateur, email, motdepasse) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNomUtilisateur());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotdepasse());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Erreur DAO (insert): " + e.getMessage());
            return false;
        }
    }

    public Utilisateur findById(String nomUtilisateur) {
        String sql = "SELECT * FROM Utilisateur WHERE NomUtilisateur = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getString("NomUtilisateur"),
                        rs.getString("email"),
                        rs.getString("motdepasse")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erreur DAO (findById): " + e.getMessage());
        }
        return null;
    }

    public boolean delete(String nomUtilisateur) {
        String sql = "DELETE FROM Utilisateur WHERE NomUtilisateur = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Erreur DAO (delete): " + e.getMessage());
            return false;
        }
    }

    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE Utilisateur SET email = ?, motdepasse = ? WHERE NomUtilisateur = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getEmail());
            stmt.setString(2, utilisateur.getMotdepasse());
            stmt.setString(3, utilisateur.getNomUtilisateur());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Erreur DAO (update): " + e.getMessage());
            return false;
        }
    }
    public Utilisateur findByLogin(String nom, String motdepasse) {
        String sql = "SELECT * FROM Utilisateur WHERE nomutilisateur = ? AND motdepasse = ?";

        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, motdepasse);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setNomUtilisateur(rs.getString("nomutilisateur"));
                u.setEmail(rs.getString("email"));
                u.setMotdepasse(rs.getString("motdepasse"));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Erreur findByLogin : " + e.getMessage());
        }
        return null;
    }

}