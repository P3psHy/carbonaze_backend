# Documentation API - Carbonaze Backend

Cette API expose les operations de gestion d'authentification, societes, sites, bilans carbone et materiaux.

## Base URL

- Local: `http://localhost:8080`
- Prefixe API: `/api`

## Authentification

- Les routes `/api/auth/**` sont publiques.
- Toutes les autres routes `/api/**` necessitent un JWT dans l'en-tete:

```http
Authorization: Bearer <token>
```

- Duree de validite du token (par defaut): `86400000 ms` (24 h).

## Format des erreurs

En cas d'erreur metier/validation, la reponse suit ce format:

```json
{
  "timestamp": "2026-03-17T10:22:41.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Champs invalides ou manquants: mail, password",
  "path": "/api/auth/login"
}
```

Erreurs frequentes:
- `400 Bad Request`: corps invalide ou champs manquants.
- `401 Unauthorized`: token absent/invalide ou identifiants invalides.
- `404 Not Found`: ressource introuvable.
- `409 Conflict`: tentative de creation d'un compte deja existant.

## Endpoints

### 1) Authentification

#### POST `/api/auth/register`

Crée un utilisateur et sa societe, puis retourne un JWT.

Corps:

```json
{
  "mail": "alice@carbonaze.io",
  "password": "motdepassefort",
  "societyName": "Carbonaze"
}
```

Contraintes:
- `mail`: email valide, max 256.
- `password`: entre 8 et 128 caracteres.
- `societyName`: entre 2 et 256 caracteres.

Reponse `201 Created`:

```json
{
  "token": "<jwt>",
  "userId": 1,
  "mail": "alice@carbonaze.io",
  "societyId": 1,
  "societyName": "Carbonaze"
}
```

#### POST `/api/auth/login`

Authentifie un utilisateur existant.

Corps:

```json
{
  "mail": "alice@carbonaze.io",
  "password": "motdepassefort"
}
```

Reponse `200 OK`: meme format que `register`.

### 2) Societes

#### POST `/api/societies`

Crée une societe.

Auth: requise.

Corps:

```json
{
  "name": "Carbonaze"
}
```

Reponse `201 Created`:

```json
{
  "id": 1,
  "name": "Carbonaze"
}
```

### 3) Sites

#### POST `/api/sites`

Crée un site rattache a une societe existante.

Auth: requise.

Corps:

```json
{
  "name": "Siege Paris",
  "city": "Paris",
  "numberEmployee": 120,
  "parkingPlaces": 35,
  "numberPc": 95,
  "societyId": 1
}
```

Contraintes:
- `name`, `city`: obligatoires.
- `numberEmployee`, `parkingPlaces`, `numberPc`: entiers >= 0.
- `societyId`: obligatoire.

Reponse `201 Created`:

```json
{
  "id": 1,
  "name": "Siege Paris",
  "city": "Paris",
  "numberEmployee": 120,
  "parkingPlaces": 35,
  "numberPc": 95,
  "createdAt": "2026-03-17T10:24:00.000",
  "societyId": 1
}
```

#### GET `/api/sites/comparison`

Retourne les sites avec leur dernier bilan (si disponible).

Auth: requise.

Reponse `200 OK`:

```json
[
  {
    "id": 1,
    "name": "Siege Paris",
    "city": "Paris",
    "numberEmployee": 120,
    "parkingPlaces": 35,
    "numberPc": 95,
    "createdAt": "2026-03-17T10:24:00",
    "societyId": 1,
    "latestBilanId": 12,
    "latestCalculationDate": "2026-03-16",
    "latestTotalCo2": 19.8,
    "latestElectricityKwhYear": 12000.0,
    "latestGasKwhYear": 5400.0
  }
]
```

Note: les champs `latest*` peuvent etre `null` si aucun bilan n'existe.

### 4) Bilans carbone

#### POST `/api/sites/{siteId}/bilans`

Crée un bilan pour un site.

Auth: requise.

Parametres chemin:
- `siteId` (Long): id du site cible.

Corps:

```json
{
  "electricityKwhYear": 12000.0,
  "gasKwhYear": 5400.0,
  "totalCo2": 19.8,
  "calculationDate": "2026-03-16",
  "materials": [
    {
      "materialId": 1,
      "name": "Acier",
      "quantity": 10.0,
      "factor": 2.5,
      "emission": 25.0
    }
  ]
}
```

Contraintes:
- `electricityKwhYear`, `gasKwhYear`, `totalCo2`: obligatoires, >= 0.
- `calculationDate`: optionnelle (format `yyyy-MM-dd`), valeur par defaut: date du jour.
- `materials`: optionnelle.
- Chaque materiau: `name` obligatoire, `quantity`, `factor`, `emission` >= 0.

Reponse `201 Created` (extrait):

```json
{
  "id": 12,
  "electricityKwhYear": 12000.0,
  "gasKwhYear": 5400.0,
  "totalCo2": 19.8,
  "calculationDate": "2026-03-16",
  "siteId": 1,
  "site": {
    "id": 1,
    "name": "Siege Paris",
    "city": "Paris",
    "numberEmployee": 120,
    "parkingPlaces": 35,
    "numberPc": 95,
    "societyId": 1
  },
  "materials": [
    {
      "id": 1,
      "materialId": 1,
      "name": "Acier",
      "quantity": 10.0,
      "factor": 2.5,
      "emission": 25.0
    }
  ]
}
```

#### GET `/api/sites/{siteId}/bilans`

Retourne l'historique des bilans d'un site (ordre descendant par date de calcul, puis id).

Auth: requise.

Reponse `200 OK`: tableau de `BilanResponse`.

#### GET `/api/bilans`

Retourne tous les bilans (ordre descendant par date de calcul, puis id).

Auth: requise.

Reponse `200 OK`: tableau de `BilanResponse`.

#### GET `/api/bilans/{bilanId}`

Retourne un bilan par id.

Auth: requise.

Reponse `200 OK`: `BilanResponse`.

#### DELETE `/api/bilans/{bilanId}`

Supprime un bilan.

Auth: requise.

Reponse `204 No Content`.

### 5) Materiaux

#### GET `/api/materials`

Retourne la liste des materiaux (ordre ascendant par id).

Auth: requise.

Reponse `200 OK`:

```json
[
  {
    "id": 1,
    "name": "Acier",
    "energeticValue": 2.5,
    "quantity": 10.0
  }
]
```

#### POST `/api/materials`

Crée ou met a jour des materiaux en lot.

Auth: requise.

Corps:

```json
[
  {
    "name": "Acier",
    "energeticValue": 2.5,
    "quantity": 10.0
  },
  {
    "id": 1,
    "name": "Acier recycle",
    "energeticValue": 2.1,
    "quantity": 12.0
  }
]
```

Regles:
- `id` absent: creation.
- `id` present mais introuvable: creation d'un nouveau materiau (sans erreur).
- `name` obligatoire.
- `energeticValue`, `quantity` obligatoires et >= 0.

Reponse `201 Created`: tableau de `MaterialResponse`.

## Exemple d'appel avec cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"mail":"alice@carbonaze.io","password":"motdepassefort"}'

# Utiliser le token recu
curl http://localhost:8080/api/sites/comparison \
  -H "Authorization: Bearer <token>"
```
