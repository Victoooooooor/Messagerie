package com.example.discord.model;

public class Utilisateur {
    private String nomUtilisateur;
    private String email;
    private String motdepasse;

    public Utilisateur() {}

    public Utilisateur(String nomUtilisateur, String email, String motdepasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.email = email;
        this.motdepasse = motdepasse;
    }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotdepasse() { return motdepasse; }
    public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }
}
