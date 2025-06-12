<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Envoyer un message</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="mb-4 text-center">Envoyer un message</h2>

    <form id="messageForm" class="card p-4 shadow-sm bg-white">
        <div class="mb-3">
            <label for="nomUtilisateur" class="form-label">Auteur du message</label>
            <input type="text" class="form-control" id="nomUtilisateur" required>
        </div>

        <div class="mb-3">
            <label for="contenu" class="form-label">Contenu du message</label>
            <textarea class="form-control" id="contenu" rows="3" required></textarea>
        </div>

        <div class="mb-3">
            <label for="type" class="form-label">Type de message</label>
            <select class="form-select" id="type" required>
                <option value="canal">Canal</option>
                <option value="direct">Direct</option>
            </select>
        </div>

        <div class="mb-3 canal-only">
            <label for="nomCanal" class="form-label">Nom du canal</label>
            <input type="text" class="form-control" id="nomCanal">
        </div>

        <div class="mb-3 direct-only">
            <label for="utilisateur1" class="form-label">Utilisateur 1</label>
            <input type="text" class="form-control" id="utilisateur1">
        </div>

        <div class="mb-3 direct-only">
            <label for="utilisateur2" class="form-label">Utilisateur 2</label>
            <input type="text" class="form-control" id="utilisateur2">
        </div>

        <button type="submit" class="btn btn-primary">Envoyer</button>
    </form>

    <div id="messageResult" class="mt-4 text-center"></div>

    <div class="text-center mt-4">
        <a href="index.jsp" class="btn btn-outline-secondary">Retour à l'accueil</a>
    </div>
</div>

<script>
    const form = document.getElementById("messageForm");
    const typeSelect = document.getElementById("type");
    const canalFields = document.querySelectorAll(".canal-only");
    const directFields = document.querySelectorAll(".direct-only");

    function toggleFields() {
        const type = typeSelect.value;
        canalFields.forEach(el => el.style.display = type === "canal" ? "block" : "none");
        directFields.forEach(el => el.style.display = type === "direct" ? "block" : "none");
    }

    toggleFields();
    typeSelect.addEventListener("change", toggleFields);

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const type = typeSelect.value;
        const data = {
            contenu: document.getElementById("contenu").value,
            nomUtilisateur: document.getElementById("nomUtilisateur").value,
            time_: new Date().toLocaleTimeString("fr-FR", { hour12: false }),
            nomCanal: "",
            nomUtilisateur_1: "",
            nomUtilisateur_2: ""
        };

        if (type === "canal") {
            data.nomCanal = document.getElementById("nomCanal").value;
        } else {
            data.nomUtilisateur_1 = document.getElementById("utilisateur1").value;
            data.nomUtilisateur_2 = document.getElementById("utilisateur2").value;
            data.nomCanal = "direct"; // ou une valeur neutre si nécessaire
        }

        fetch("/Discord/messages", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (res.ok) return res.json();
                throw new Error("Erreur lors de l'envoi");
            })
            .then(() => {
                document.getElementById("messageResult").innerHTML =
                    "<div class='alert alert-success'>Message envoyé avec succès !</div>";
                form.reset();
            })
            .catch(err => {
                document.getElementById("messageResult").innerHTML =
                    `<div class='alert alert-danger'>${err.message}</div>`;
            });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>