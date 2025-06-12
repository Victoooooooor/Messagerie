<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
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
        <!-- Les messages seront affichés ici dynamiquement -->
    </div>

    <div class="text-center">
        <a href="index.jsp" class="btn btn-outline-primary">Retour à l'accueil</a>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const urlParams = new URLSearchParams(window.location.search);
        const canal = urlParams.get("canal");
        const contextPath = "<%= contextPath %>";

        document.getElementById("canalName").textContent = canal;

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
                    console.log("Message reçu :", msg);  // ← tu le fais déjà, c’est bon

                    const item = document.createElement("div");
                    item.className = "list-group-item";
                    item.innerHTML =
                        '<strong>' + (msg.nomUtilisateur || "Inconnu") + '</strong>' +
                        '<small class="text-muted float-end">' + (msg.time_ || "?") + '</small><br>' +
                        (msg.contenu || "");
                    container.appendChild(item);
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