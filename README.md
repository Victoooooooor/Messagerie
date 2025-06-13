# Mini Discord – Comptes de connexion

## ⚙️ Installation

### 1. Créer la base de données

Dans PostgreSQL, crée une base de données (ex. : `projet_webservices`), puis exécute le script SQL fourni (`script.sql`) pour créer les tables et insérer les données initiales.

```bash
psql -U postgres -d projet_webservices -f chemin/vers/script.sql
```

### 2. Modifier le fichier de connexion

Dans src/main/java/com/example/discord/util/ConnexionBD.java, adapte les constantes :
```bash
private static final String URL = "jdbc:postgresql://localhost:5432/projet_webservices";
private static final String UTILISATEUR = "postgres";
private static final String MOT_DE_PASSE = "votre_mot_de_passe";
```


## Comptes disponibles

| Nom d'utilisateur | Email              | Mot de passe |
|-------------------|--------------------|--------------|
| `alice`           | alice@mail.com     | `mdp123`     |
| `bob`             | bob@mail.com       | `bobpass`    |
| `carol`           | carol@mail.com     | `carolpw`    |

## Connexion

Sur la page de connexion (`login.jsp`), utilisez l’un des comptes ci-dessus :

- **Nom d’utilisateur** : correspond à la colonne `nomutilisateur`
- **Mot de passe** : tel qu’indiqué dans le tableau