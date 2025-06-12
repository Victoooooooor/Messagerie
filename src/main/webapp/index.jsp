<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    Object utilisateur = session.getAttribute("utilisateur");

    if (utilisateur == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String nomUtilisateur = ((com.example.discord.model.Utilisateur) utilisateur).getNomUtilisateur();
%>
<html>
<head>
    <title>Accueil - Mini Discord</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Mini Discord</h1>
        <div class="text-end">
            Connecté en tant que <strong><%= nomUtilisateur %></strong>
            <a href="logout" class="btn btn-sm btn-outline-danger ms-2">Déconnexion</a>
        </div>
    </div>

    <!-- Zone pour les erreurs -->
    <div id="errorAlert" class="mb-4"></div>

    <% if ("1".equals(request.getParameter("unauthorized"))) { %>
    <div class="alert alert-danger text-center" role="alert">
        Vous n'êtes pas autorisé à accéder à cette conversation.
    </div>
    <% } %>

    <% if ("1".equals(request.getParameter("unauthorizedCanal"))) { %>
    <div class="alert alert-danger text-center" role="alert">
        Vous n'êtes pas autorisé à accéder à ce canal.
    </div>
    <% } %>

    <div class="row g-4">
        <!-- Formulaire canal -->
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Accéder à un canal</h5>
                    <form id="canalForm">
                        <div class="mb-3">
                            <label for="canal" class="form-label">Nom du canal</label>
                            <input type="text" id="canal" name="canal" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Voir les messages</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Formulaire conversation directe -->
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Accéder à une conversation directe</h5>
                    <form id="conversationForm">
                        <!-- Champ caché avec utilisateur connecté -->
                        <input type="hidden" id="from" name="from" value="<%= nomUtilisateur %>">

                        <div class="mb-3">
                            <label for="to" class="form-label">Utilisateur destinataire</label>
                            <input type="text" id="to" name="to" class="form-control" required>
                        </div>

                        <button type="submit" class="btn btn-success">Voir la conversation</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Lien formulaire message -->
        <div class="col-12">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title">Envoyer un message</h5>
                    <a href="messageForm.jsp" class="btn btn-outline-secondary">Accéder au formulaire de message</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Script canal -->
<script>
    const contextPath = "<%= contextPath %>";
    const currentUser = "<%= nomUtilisateur %>";

    document.getElementById("canalForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const canal = document.getElementById("canal").value.trim();
        const alertDiv = document.getElementById("errorAlert");
        alertDiv.innerHTML = "";

        fetch(contextPath + "/messages?canal=" + encodeURIComponent(canal))
            .then(response => {
                if (!response.ok) {
                    if (response.status === 403) {
                        throw new Error("unauthorized");
                    }
                    throw new Error("notfound");
                }
                return response.json();
            })
            .then(() => {
                window.location.href = "canal.jsp?canal=" + encodeURIComponent(canal);
            })
            .catch(err => {
                if (err.message === "unauthorized") {
                    alertDiv.innerHTML = `
                <div class="alert alert-danger text-center" role="alert">
                    Vous n'êtes pas autorisé à accéder à ce canal.
                </div>`;
                } else {
                    alertDiv.innerHTML = `
                <div class="alert alert-danger text-center" role="alert">
                    Aucun canal avec ce nom n'existe.
                </div>`;
                }
            });
    });

    // Script conversation directe
    document.getElementById("conversationForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const from = document.getElementById("from").value.trim();
        const to = document.getElementById("to").value.trim();
        const alertDiv = document.getElementById("errorAlert");
        alertDiv.innerHTML = "";

        if (from !== currentUser && to !== currentUser) {
            alertDiv.innerHTML = `
                <div class="alert alert-danger text-center" role="alert">
                    Vous n'êtes pas autorisé à accéder à cette conversation.
                </div>
            `;
            return;
        }

        window.location.href = "conversation.jsp?from=" + encodeURIComponent(from) + "&to=" + encodeURIComponent(to);
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>