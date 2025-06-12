package com.example.discord.model;

public class EspaceTravail {
    private String nomEspace;
    private String description;

    public EspaceTravail() {}

    public EspaceTravail(String nomEspace, String description) {
        this.nomEspace = nomEspace;
        this.description = description;
    }

    public String getNomEspace() { return nomEspace; }
    public void setNomEspace(String nomEspace) { this.nomEspace = nomEspace; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}