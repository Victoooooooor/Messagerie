<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    com.example.discord.model.Utilisateur utilisateur = (com.example.discord.model.Utilisateur) session.getAttribute("utilisateur");
    String utilisateurConnecte = utilisateur.getNomUtilisateur();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Messages du canal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-4">
    <h2 class="mb-4 text-center">Messages du canal : <span id="canalName"></span></h2>

    <div id="messagesList" class="list-group mb-4">
        <!-- Messages dynamiques -->
    </div>

    <div class="text-center">
        <a href="index.jsp" class="btn btn-outline-primary">Retour à l'accueil</a>
    </div>

    <!-- Barre de chat -->
    <div class="card shadow-sm fixed-bottom mx-auto" style="max-width: 800px;">
        <div class="card-body d-flex align-items-center gap-2">
            <input type="text" id="messageInput" class="form-control" placeholder="Écrire un message..." />
            <button id="sendMessageBtn" class="btn btn-primary">Envoyer</button>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const urlParams = new URLSearchParams(window.location.search);
        const canal = urlParams.get("canal");
        const contextPath = "<%= contextPath %>";
        const utilisateurConnecte = "<%= utilisateurConnecte %>";

        document.getElementById("canalName").textContent = canal;

        function chargerMessages() {
            fetch(contextPath + "/messages?canal=" + encodeURIComponent(canal))
                .then(response => {
                    if (!response.ok) throw new Error("Erreur lors du chargement des messages");
                    return response.json();
                })
                .then(messages => {
                    const container = document.getElementById("messagesList");
                    container.innerHTML = "";

                    if (!messages || messages.length === 0) {
                        container.innerHTML = `<div class="text-muted text-center">Aucun message pour ce canal.</div>`;
                        return;
                    }

                    messages.forEach(msg => {
                        const item = document.createElement("div");
                        item.className = "list-group-item";
                        item.innerHTML =
                            '<strong>' + (msg.nomUtilisateur || "Inconnu") + '</strong>' +
                            '<small class="text-muted float-end">' + (msg.time_ || "?") + '</small><br>' +
                            (msg.contenu || "") +
                            '<div class="mt-2 reactions" id="reactions-' + msg.idMessage + '"></div>' +
                            '<div class="mt-2">' +
                            '<button class="btn btn-outline-secondary btn-sm me-1 reaction-btn" data-id="' + msg.idMessage + '" data-reaction="like">👍</button>' +
                            '<button class="btn btn-outline-secondary btn-sm me-1 reaction-btn" data-id="' + msg.idMessage + '" data-reaction="love">❤️</button>' +
                            '<button class="btn btn-outline-secondary btn-sm me-1 reaction-btn" data-id="' + msg.idMessage + '" data-reaction="funny">😂</button>' +
                            '</div>';
                        container.appendChild(item);

                        fetch(contextPath + "/reactions?idMessage=" + msg.idMessage)
                            .then(response => response.json())
                            .then(reactions => {
                                const reactionsDiv = document.getElementById("reactions-" + msg.idMessage);
                                reactions.forEach(r => {
                                    const emoji = r.typeReaction === "like" ? "👍" :
                                        r.typeReaction === "love" ? "❤️" :
                                            r.typeReaction === "funny" ? "😂" : "";
                                    const span = document.createElement("span");
                                    span.className = "badge bg-light text-dark me-1";
                                    span.innerText = emoji + " " + r.nomUtilisateur;
                                    reactionsDiv.appendChild(span);
                                });
                            });
                    });

                    document.querySelectorAll(".reaction-btn").forEach(btn => {
                        btn.addEventListener("click", () => {
                            const idMessage = btn.dataset.id;
                            const typeReaction = btn.dataset.reaction;

                            fetch(contextPath + "/reactions", {
                                method: "POST",
                                headers: { "Content-Type": "application/json" },
                                body: JSON.stringify({
                                    idMessage: parseInt(idMessage),
                                    typeReaction: typeReaction
                                })
                            }).then(response => {
                                if (!response.ok) throw new Error("Erreur lors de la réaction.");
                                return response.text();
                            }).then(() => {
                                fetch(contextPath + "/reactions?idMessage=" + idMessage)
                                    .then(response => response.json())
                                    .then(reactions => {
                                        const reactionsDiv = document.getElementById("reactions-" + idMessage);
                                        reactionsDiv.innerHTML = "";
                                        reactions.forEach(r => {
                                            const emoji = r.typeReaction === "like" ? "👍" :
                                                r.typeReaction === "love" ? "❤️" :
                                                    r.typeReaction === "funny" ? "😂" : "";
                                            const span = document.createElement("span");
                                            span.className = "badge bg-light text-dark me-1";
                                            span.innerText = emoji + " " + r.nomUtilisateur;
                                            reactionsDiv.appendChild(span);
                                        });
                                    });
                            }).catch(err => {
                                alert("Erreur : " + err.message);
                            });
                        });
                    });
                })
                .catch(error => {
                    const container = document.getElementById("messagesList");
                    container.innerHTML = `<div class="text-danger text-center">Erreur : ${error.message}</div>`;
                    console.error("Erreur lors du fetch :", error);
                });
        }

        chargerMessages();

        // Envoi d’un message
        document.getElementById("sendMessageBtn").addEventListener("click", () => {
            const contenu = document.getElementById("messageInput").value.trim();
            if (contenu === "") return;

            fetch(contextPath + "/messages", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    contenu: contenu,
                    nomUtilisateur: utilisateurConnecte,
                    nomUtilisateur1: utilisateurConnecte,
                    nomUtilisateur2: utilisateurConnecte, // par défaut car pas de destinataire en canal
                    nomCanal: canal,
                    time_: new Date().toLocaleTimeString("fr-FR", { hour12: false })
                })
            })
                .then(response => {
                    if (!response.ok) throw new Error("Erreur lors de l'envoi du message.");
                    return response.json();
                })
                .then(() => {
                    document.getElementById("messageInput").value = "";
                    chargerMessages(); // rechargement dynamique sans reload
                })
                .catch(err => {
                    alert("Erreur : " + err.message);
                });
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>