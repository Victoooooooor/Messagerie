package com.example.discord.servlet;

import com.example.discord.dao.CanalDAO;
import com.example.discord.dao.MessageDAO;
import com.example.discord.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.List;

@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

    private final MessageDAO messageDAO = new MessageDAO();
    private final CanalDAO canalDAO = new CanalDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);
        boolean success = messageDAO.insert(message);
        if (success) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), message);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de l'insertion du message");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String canal = req.getParameter("canal");
        String from = req.getParameter("from");
        String to = req.getParameter("to");

        List<Message> messages;

        if (canal != null) {
            if (!canalDAO.exists(canal)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Canal introuvable");
                return;
            }
            messages = messageDAO.findByCanal(canal);
        } else if (from != null && to != null) {
            messages = messageDAO.findDirectMessages(from, to);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres manquants");
            return;
        }

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), messages);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message updated = objectMapper.readValue(req.getInputStream(), Message.class);
        boolean success = messageDAO.update(updated);
        if (success) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Message non trouvé ou non mis à jour");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID manquant");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            boolean success = messageDAO.delete(id);
            if (success) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Message non trouvé");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID invalide");
        }
    }
}