package com.example.discord.model;

public class Message {
    private int idMessage;
    private String contenu;
    private String time_;
    private String nomUtilisateur;
    private String nomCanal;

    public Message() {}

    public Message(int idMessage, String contenu, String time_, String nomUtilisateur, String nomCanal) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.time_ = time_;
        this.nomUtilisateur = nomUtilisateur;
        this.nomCanal = nomCanal;
    }

    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getTime_() { return time_; }
    public void setTime_(String time_) { this.time_ = time_; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getNomCanal() { return nomCanal; }
    public void setNomCanal(String nomCanal) { this.nomCanal = nomCanal; }
}