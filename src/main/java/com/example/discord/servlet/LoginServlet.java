package com.example.discord.servlet;

import com.example.discord.dao.UtilisateurDAO;
import com.example.discord.model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String nom = req.getParameter("nomutilisateur");
        String mdp = req.getParameter("motdepasse");

        Utilisateur user = utilisateurDAO.findByLogin(nom, mdp);
        if (user != null) {
            req.getSession().setAttribute("utilisateur", user);
            resp.sendRedirect("index.jsp");
        } else {
            resp.sendRedirect("login.jsp?error=1");
        }
    }
}