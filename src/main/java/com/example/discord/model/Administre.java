package com.example.discord.model;

public class Administre {
    private String nomUtilisateur;
    private String nomEspace;

    public Administre() {}

    public Administre(String nomUtilisateur, String nomEspace) {
        this.nomUtilisateur = nomUtilisateur;
        this.nomEspace = nomEspace;
    }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getNomEspace() { return nomEspace; }
    public void setNomEspace(String nomEspace) { this.nomEspace = nomEspace; }
}