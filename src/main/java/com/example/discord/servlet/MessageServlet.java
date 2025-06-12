package com.example.discord.servlet;

import com.example.discord.dao.CanalDAO;
import com.example.discord.dao.MessageDAO;
import com.example.discord.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

    private final MessageDAO messageDAO = new MessageDAO();
    private final CanalDAO canalDAO = new CanalDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String canal = req.getParameter("canal");
        String from = req.getParameter("from");
        String to = req.getParameter("to");

        // Cas 1 : messages publics par canal
        if (canal != null) {
            if (!canalDAO.exists(canal)) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(),
                        new ErrorResponse("Le canal '" + canal + "' est introuvable."));
                return;
            }

            List<Message> messages = messageDAO.findByCanal(canal);
            objectMapper.writeValue(resp.getWriter(), messages);
            return;
        }

        // Cas 2 : messages directs entre 2 utilisateurs
        if (from != null && to != null) {
            List<Message> messages = messageDAO.findDirectMessages(from, to);
            objectMapper.writeValue(resp.getWriter(), messages);
            return;
        }

        // Cas 3 : mauvaise requête
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        objectMapper.writeValue(resp.getWriter(),
                new ErrorResponse("Veuillez spécifier soit 'canal', soit 'from' et 'to'."));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);

        boolean success = messageDAO.insert(message);
        if (success) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), message);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(),
                    new ErrorResponse("Erreur lors de l'insertion du message."));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Message updated = objectMapper.readValue(req.getInputStream(), Message.class);

        boolean success = messageDAO.update(updated);
        if (success) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(),
                    new ErrorResponse("Message non trouvé ou non mis à jour."));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String idStr = req.getParameter("id");

        if (idStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Paramètre 'id' manquant."));
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            boolean success = messageDAO.delete(id);
            if (success) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Message non trouvé."));
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("ID invalide : " + idStr));
        }
    }

    // Classe interne pour les erreurs (si tu veux afficher les erreurs proprement côté JS)
    static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}