<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<%
    com.example.discord.model.Utilisateur utilisateur = (com.example.discord.model.Utilisateur) session.getAttribute("utilisateur");
    String utilisateurConnecte = utilisateur.getNomUtilisateur();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Conversation directe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-4">
    <h2 class="mb-4 text-center">
        Conversation entre <span id="fromUser"></span> et <span id="toUser"></span>
    </h2>

    <div id="messagesList" class="list-group mb-4">
        <!-- Messages affichés ici dynamiquement -->
    </div>

    <div class="text-center">
        <a href="index.jsp" class="btn btn-outline-primary">Retour à l'accueil</a>
    </div>
    <!-- Formulaire d'envoi de message -->
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
        const from = urlParams.get("from");
        const to = urlParams.get("to");
        const contextPath = "<%= contextPath %>";
        const utilisateurConnecte = "<%= utilisateurConnecte %>";

        document.getElementById("fromUser").textContent = from;
        document.getElementById("toUser").textContent = to;

        fetch(contextPath + "/messages?from=" + encodeURIComponent(from) + "&to=" + encodeURIComponent(to))
            .then(response => {
                if (!response.ok) throw new Error("Erreur lors du chargement des messages");
                return response.json();
            })
            .then(messages => {
                const container = document.getElementById("messagesList");
                container.innerHTML = "";

                if (!messages || messages.length === 0) {
                    container.innerHTML = `<div class="text-muted text-center">Aucun message dans cette conversation.</div>`;
                    return;
                }

                messages.forEach(msg => {
                    const item = document.createElement("div");
                    item.className = "list-group-item";
                    const timestamp = parseInt(msg.time_);
                    const dateAffichee = !isNaN(timestamp)
                        ? new Intl.DateTimeFormat("fr-FR", {
                            dateStyle: "short",
                            timeStyle: "medium"
                        }).format(new Date(timestamp))
                        : "?";

                    item.innerHTML =
                        '<strong>' + (msg.nomUtilisateur || "Inconnu") + '</strong>' +
                        '<small class="text-muted float-end">' + dateAffichee + '</small><br>' +
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

                // Ajout des listeners de réactions
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
                        })
                            .then(response => {
                                if (!response.ok) throw new Error("Erreur lors de la réaction.");
                                return response.text(); // Optionnel, ou .json() si tu veux retourner une info serveur
                            })
                            .then(() => {
                                // Recharge la liste des réactions pour ce message uniquement :
                                fetch(contextPath + "/reactions?idMessage=" + idMessage)
                                    .then(response => response.json())
                                    .then(reactions => {
                                        const reactionsDiv = document.getElementById("reactions-" + idMessage);
                                        reactionsDiv.innerHTML = ""; // reset

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
                            })
                            .catch(err => {
                                alert("Erreur : " + err.message);
                            });
                    });
                });
                document.getElementById("sendMessageBtn").addEventListener("click", () => {
                    const contenu = document.getElementById("messageInput").value.trim();
                    if (contenu === "") return;

                    const from = utilisateurConnecte;
                    const to = urlParams.get("to");

                    fetch(contextPath + "/messages", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            contenu: contenu,
                            nomCanal: null,
                            nomUtilisateur1: from,
                            nomUtilisateur2: to,
                            time_: new Date().toLocaleTimeString("fr-FR", { hour12: false })
                        })
                    })
                        .then(response => {
                            if (!response.ok) throw new Error("Erreur lors de l'envoi du message.");
                            return response.json();
                        })
                        .then(() => {
                            document.getElementById("messageInput").value = "";
                            location.reload(); // ou appel dynamique si tu préfères sans reload
                        })
                        .catch(err => {
                            alert("Erreur : " + err.message);
                        });
                });
            })


            .catch(error => {
                const container = document.getElementById("messagesList");
                container.innerHTML = `<div class="text-danger text-center">Erreur : ${error.message}</div>`;
                console.error("Erreur lors du fetch :", error);
            });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>