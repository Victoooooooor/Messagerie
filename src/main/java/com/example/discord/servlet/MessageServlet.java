package com.example.discord.servlet;

import com.example.discord.dao.CanalDAO;
import com.example.discord.dao.MessageDAO;
import com.example.discord.dao.ReactionDAO;
import com.example.discord.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
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
        System.out.println("[DEBUG] Appel POST /messages");

        try {
            JsonNode json = objectMapper.readTree(req.getInputStream());
            System.out.println("[DEBUG] JSON reçu : " + json.toString());

            Message message = new Message();

            String contenu = json.has("contenu") ? json.get("contenu").asText() : null;
            String nomCanal = json.has("nomCanal") && !json.get("nomCanal").isNull() ? json.get("nomCanal").asText() : null;
            String nomUtilisateur1 = json.has("nomUtilisateur1") ? json.get("nomUtilisateur1").asText() : null;
            String nomUtilisateur2 = json.has("nomUtilisateur2") ? json.get("nomUtilisateur2").asText() : null;

            if (contenu == null || nomUtilisateur1 == null || nomUtilisateur2 == null) {
                System.out.println("[ERREUR] Champs manquants !");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Champs requis manquants."));
                return;
            }

            message.setContenu(contenu);
            message.setNomUtilisateur(nomUtilisateur1);
            message.setNomCanal(nomCanal);
            message.setNomUtilisateur1(nomUtilisateur1);
            message.setNomUtilisateur2(nomUtilisateur2);

            // Utilisation du timestamp courant du serveur
            java.sql.Timestamp now = java.sql.Timestamp.valueOf(java.time.LocalDateTime.now());
            message.setTime_(now);

            System.out.println("[DEBUG] Message préparé : " + contenu + " à " + now);

            int nextId = messageDAO.getNextIdMessage();
            message.setIdMessage(nextId);
            boolean success = messageDAO.insert(message);

            if (success) {
                System.out.println("[SUCCESS] Message inséré");
                resp.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(resp.getWriter(), message);
            } else {
                System.out.println("[ERREUR] Échec d'insertion");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Erreur lors de l'insertion du message."));
            }

        } catch (Exception e) {
            System.out.println("[EXCEPTION] " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Erreur inattendue côté serveur."));
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
        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id manquant");
                return;
            }

            int idMessage = Integer.parseInt(idParam);
            ReactionDAO reactionDAO = new ReactionDAO();
            reactionDAO.deleteByMessageId(idMessage); // Supprime les réactions d'abord

            boolean deleted = messageDAO.delete(idMessage); // Puis supprime le message


            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Message non trouvé");
            }

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la suppression : " + e.getMessage());
        }
    }


    static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}