package com.example.discord.servlet;

import com.example.discord.dao.UtilisateurDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/utilisateur-existe")
public class VerifUtilisateurServlet extends HttpServlet {
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        try (PrintWriter out = resp.getWriter()) {
            String nom = req.getParameter("nom");

            if (nom == null || nom.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Paramètre manquant.");
                return;
            }

            boolean existe = utilisateurDAO.exists(nom);
            if (existe) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.println("OK");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("Utilisateur non trouvé.");
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace(); // Log interne
        }
    }
}