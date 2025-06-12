package com.example.discord.model;

public class Reaction {
    private String nomUtilisateur;
    private int idMessage;
    private String typeReaction;

    public Reaction() {}

    public Reaction(String nomUtilisateur, int idMessage, String typeReaction) {
        this.nomUtilisateur = nomUtilisateur;
        this.idMessage = idMessage;
        this.typeReaction = typeReaction;
    }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }
    public String getTypeReaction() { return typeReaction; }
    public void setTypeReaction(String typeReaction) { this.typeReaction = typeReaction; }
}
