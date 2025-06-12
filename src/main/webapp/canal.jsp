<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Messages du canal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-4">
    <h2 class="mb-4 text-center">Messages du canal : <span id="canalName"></span></h2>

    <div id="messagesList" class="list-group mb-4"></div>

    <div class="text-center">
        <a href="index.jsp" class="btn btn-outline-primary">Retour à l'accueil</a>
    </div>
</div>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const canal = urlParams.get("canal");
    const contextPath = "<%= contextPath %>";

    document.getElementById("canalName").textContent = canal;

    fetch(contextPath + "/messages?canal=" + encodeURIComponent(canal))
        .then(response => response.json())
        .then(messages => {
            const container = document.getElementById("messagesList");

            if (messages.length === 0) {
                container.innerHTML = "<p class='text-muted'>Aucun message pour ce canal.</p>";
            } else {
                container.innerHTML = "";
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
            }
        });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>