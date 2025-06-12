<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Accueil - Mini Discord</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">

    <h1 class="mb-4 text-center">Mini Discord</h1>

    <!-- Zone pour les erreurs -->
    <div id="errorAlert" class="mb-4"></div>

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

        <!-- Formulaire conversation -->
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Accéder à une conversation directe</h5>
                    <form action="conversation.jsp" method="get">
                        <div class="mb-3">
                            <label for="from" class="form-label">Utilisateur 1</label>
                            <input type="text" id="from" name="from" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="to" class="form-label">Utilisateur 2</label>
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

<!-- Script pour valider l'existence du canal -->
<script>
    const contextPath = "<%= request.getContextPath() %>";

    document.getElementById("canalForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const canal = document.getElementById("canal").value.trim();
        const alertDiv = document.getElementById("errorAlert");
        alertDiv.innerHTML = "";

        fetch(contextPath + "/messages?canal=" + encodeURIComponent(canal))
            .then(response => {
                if (!response.ok) {
                    throw new Error(); // on ignore volontairement le contenu exact de l'erreur
                }
                return response.json();
            })
            .then(() => {
                // Le canal existe, on redirige
                window.location.href = "canal.jsp?canal=" + encodeURIComponent(canal);
            })
            .catch(() => {
                // Affichage propre et fixe
                alertDiv.innerHTML = `
                    <div class="alert alert-danger text-center" role="alert">
                        Aucun canal avec ce nom n'existe.
                    </div>
                `;
            });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>