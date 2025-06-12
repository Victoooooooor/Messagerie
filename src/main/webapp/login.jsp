<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Connexion - Mini Discord</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">

<div class="card p-4 shadow" style="width: 350px;">
  <h4 class="text-center mb-3">Connexion</h4>

  <% if (request.getParameter("error") != null) { %>
  <div class="alert alert-danger" role="alert">
    Nom d'utilisateur ou mot de passe invalide.
  </div>
  <% } %>

  <form action="login" method="post">
    <div class="mb-3">
      <label for="nomutilisateur" class="form-label">Nom d'utilisateur</label>
      <input type="text" class="form-control" id="nomutilisateur" name="nomutilisateur" required>
    </div>
    <div class="mb-3">
      <label for="motdepasse" class="form-label">Mot de passe</label>
      <input type="password" class="form-control" id="motdepasse" name="motdepasse" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">Se connecter</button>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>