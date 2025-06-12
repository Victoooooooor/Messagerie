<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
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
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const urlParams = new URLSearchParams(window.location.search);
        const from = urlParams.get("from");
        const to = urlParams.get("to");
        const contextPath = "<%= contextPath %>";

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

                    item.innerHTML =
                        '<strong>' + (msg.nomUtilisateur || "Inconnu") + '</strong>' +
                        '<small class="text-muted float-end">' + (msg.time_ || "?") + '</small><br>' +
                        (msg.contenu || "") +
                        '<div class="mt-2 reactions" id="reactions-' + msg.idMessage + '"></div>' + // les réactions seront insérées ici
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