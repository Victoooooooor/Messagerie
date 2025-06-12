package com.example.discord.model;

public class InvitationCanal {
    private String nomUtilisateur;
    private String nomEspace;
    private String emailInvitation;
    private String statut;

    public InvitationCanal() {}

    public InvitationCanal(String nomUtilisateur, String nomEspace, String emailInvitation, String statut) {
        this.nomUtilisateur = nomUtilisateur;
        this.nomEspace = nomEspace;
        this.emailInvitation = emailInvitation;
        this.statut = statut;
    }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getNomEspace() { return nomEspace; }
    public void setNomEspace(String nomEspace) { this.nomEspace = nomEspace; }
    public String getEmailInvitation() { return emailInvitation; }
    public void setEmailInvitation(String emailInvitation) { this.emailInvitation = emailInvitation; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
