package com.example.discord.model;

public class Canal {
    private String nomCanal;
    private String typecanal;
    private String nomEspace;

    public Canal() {}

    public Canal(String nomCanal, String typecanal, String nomEspace) {
        this.nomCanal = nomCanal;
        this.typecanal = typecanal;
        this.nomEspace = nomEspace;
    }

    public String getNomCanal() { return nomCanal; }
    public void setNomCanal(String nomCanal) { this.nomCanal = nomCanal; }
    public String getTypecanal() { return typecanal; }
    public void setTypecanal(String typecanal) { this.typecanal = typecanal; }
    public String getNomEspace() { return nomEspace; }
    public void setNomEspace(String nomEspace) { this.nomEspace = nomEspace; }
}
