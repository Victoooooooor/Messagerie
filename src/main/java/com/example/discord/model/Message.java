package com.example.discord.model;

import java.sql.Timestamp;

public class Message {
    private int idMessage;
    private String contenu;
    private Timestamp time_;
    private String nomUtilisateur;
    private String nomCanal;
    private String nomUtilisateur1;
    private String nomUtilisateur2;

    public Message() {}

    public Message(int idMessage, String contenu, Timestamp time_, String nomUtilisateur, String nomCanal) {
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

    public Timestamp getTime_() {
        return time_;
    }
    public void setTime_(Timestamp time_) {
        this.time_ = time_;
    }


    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getNomCanal() { return nomCanal; }
    public void setNomCanal(String nomCanal) { this.nomCanal = nomCanal; }

    public String getNomUtilisateur1() {
        return nomUtilisateur1;
    }

    public void setNomUtilisateur1(String nomUtilisateur1) {
        this.nomUtilisateur1 = nomUtilisateur1;
    }

    public String getNomUtilisateur2() {
        return nomUtilisateur2;
    }

    public void setNomUtilisateur2(String nomUtilisateur2) {
        this.nomUtilisateur2 = nomUtilisateur2;
    }
}