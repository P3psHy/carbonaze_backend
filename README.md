# Carbonaze Backend

Backend Java Spring Boot permettant de sauvegarder et recuperer un historique de calculs d'emission de CO2.

## Fonctionnalites

- creation d'une societe
- creation d'un site rattache a une societe
- sauvegarde d'un bilan carbone pour un site
- recuperation de l'historique des bilans d'un site
- sauvegarde et recuperation des materiaux pour synchronisation local/ligne
- base H2 en memoire initialisee via un schema SQL inspire du diagramme fourni

## Lancer le projet

```bash
mvn spring-boot:run
```

L'application demarre par defaut sur `http://localhost:8080`.

## Endpoints principaux

### Creer une societe

```http
POST /api/societies
Content-Type: application/json

{
  "name": "Carbonaze"
}
```

### Creer un site

```http
POST /api/sites
Content-Type: application/json

{
  "name": "Siege Paris",
  "city": "Paris",
  "numberEmployee": 120,
  "parkingPlaces": 35,
  "numberPc": 95,
  "societyId": 1
}
```

### Sauvegarder un calcul

```http
POST /api/sites/1/bilans
Content-Type: application/json

{
  "electricityKwhYear": 12000,
  "gasKwhYear": 5400,
  "totalCo2": 19.8,
  "calculationDate": "2026-03-16"
}
```

### Recuperer l'historique d'un site

```http
GET /api/sites/1/bilans
```

### Recuperer tous les bilans

```http
GET /api/bilans
```

### Recuperer un bilan par id

```http
GET /api/bilans/1
```

### Supprimer un bilan

```http
DELETE /api/bilans/1
```

### Sauvegarder des materiaux

```http
POST /api/materials
Content-Type: application/json

[
  {
    "name": "Acier",
    "energeticValue": 2.5,
    "quantity": 10
  },
  {
    "id": 1,
    "name": "Acier recycle",
    "energeticValue": 2.1,
    "quantity": 12
  }
]
```

### Recuperer les materiaux

```http
GET /api/materials
```
