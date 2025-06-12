package com.example.discord.servlet;

import com.example.discord.dao.ReactionDAO;
import com.example.discord.model.Reaction;
import com.example.discord.model.Utilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/reactions")
public class ReactionServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ReactionDAO reactionDAO = new ReactionDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try (BufferedReader reader = req.getReader()) {
            Map<String, Object> data = mapper.readValue(reader, Map.class);

            int idMessage = (int) data.get("idMessage");
            String type = (String) data.get("typeReaction");

            boolean success = reactionDAO.upsertReaction(user.getNomUtilisateur(), idMessage, type);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (IOException | ClassCastException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idMessage = Integer.parseInt(req.getParameter("idMessage"));
        List<Reaction> reactions = reactionDAO.getReactionsForMessage(idMessage);

        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), reactions);
    }
}