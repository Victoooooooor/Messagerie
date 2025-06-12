package com.example.discord.model;

public class Message {
    private int idMessage;
    private String contenu;
    private String time;
    private String nomUtilisateur;
    private String nomUtilisateur1;
    private String nomUtilisateur2;
    private String nomCanal;

    public Message() {}

    public Message(int idMessage, String contenu, String time, String nomUtilisateur, String nomUtilisateur1, String nomUtilisateur2, String nomCanal) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.time = time;
        this.nomUtilisateur = nomUtilisateur;
        this.nomUtilisateur1 = nomUtilisateur1;
        this.nomUtilisateur2 = nomUtilisateur2;
        this.nomCanal = nomCanal;
    }

    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getNomUtilisateur1() { return nomUtilisateur1; }
    public void setNomUtilisateur1(String nomUtilisateur1) { this.nomUtilisateur1 = nomUtilisateur1; }
    public String getNomUtilisateur2() { return nomUtilisateur2; }
    public void setNomUtilisateur2(String nomUtilisateur2) { this.nomUtilisateur2 = nomUtilisateur2; }
    public String getNomCanal() { return nomCanal; }
    public void setNomCanal(String nomCanal) { this.nomCanal = nomCanal; }
}
