package com.example.discord.servlet;

import com.example.discord.dao.CanalDAO;
import com.example.discord.dao.MessageDAO;
import com.example.discord.model.Message;
import com.example.discord.model.Utilisateur;
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

            Utilisateur currentUser = (Utilisateur) req.getSession().getAttribute("utilisateur");
            if (currentUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            boolean autorise = canalDAO.isUserInCanal(currentUser.getNomUtilisateur(), canal);
            if (!autorise) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                objectMapper.writeValue(resp.getWriter(),
                        new ErrorResponse("Accès interdit à ce canal."));
                return;
            }

            List<Message> messages = messageDAO.findByCanal(canal);
            objectMapper.writeValue(resp.getWriter(), messages);
            return;
        }

        // Cas 2 : messages directs entre deux utilisateurs
        if (from != null && to != null) {
            Utilisateur currentUser = (Utilisateur) req.getSession().getAttribute("utilisateur");
            if (currentUser == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            String current = currentUser.getNomUtilisateur();

            if (!current.equals(from) && !current.equals(to)) {
                // Redirection HTML vers l'accueil avec un message d'autorisation refusée
                resp.sendRedirect("index.jsp?unauthorized=1");
                return;
            }

            List<Message> messages = messageDAO.findDirectMessages(from, to);
            objectMapper.writeValue(resp.getWriter(), messages);
            return;
        }

        // Cas 3 : requête incorrecte
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Paramètres manquants."));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}