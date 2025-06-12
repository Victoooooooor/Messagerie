<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Conversation directe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-4">
    <h2 class="mb-4 text-center">
        Conversation entre <span id="fromUser"></span> et <span id="toUser"></span>
    </h2>

    <div id="messagesList" class="list-group mb-4">
        <!-- Messages de la conversation affichés ici -->
    </div>

    <div class="text-center">
        <a href="index.jsp" class="btn btn-outline-primary">Retour à l'accueil</a>
    </div>
</div>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const from = urlParams.get("from");
    const to = urlParams.get("to");

    document.getElementById("fromUser").textContent = from;
    document.getElementById("toUser").textContent = to;

    fetch(`/Discord/messages?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erreur lors du chargement des messages");
            }
            return response.json();
        })
        .then(messages => {
            const container = document.getElementById("messagesList");

            if (messages.length === 0) {
                container.innerHTML = "<p class='text-muted'>Aucun message dans cette conversation.</p>";
            }

            messages.forEach(msg => {
                const item = document.createElement("div");
                item.className = "list-group-item";

                item.innerHTML = `
                    <strong>${msg.nomUtilisateur}</strong>
                    <small class="text-muted float-end">${msg.time_}</small><br>
                    ${msg.contenu}
                `;
                container.appendChild(item);
            });
        })
        .catch(error => {
            document.getElementById("messagesList").innerHTML =
                `<p class="text-danger">Erreur : ${error.message}</p>`;
        });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>