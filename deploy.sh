#!/bin/bash
# ==========================================================
# DÉPLOIEMENT MANUEL - carbonaze_backend
# Lance ce script depuis la racine de ton projet Spring Boot
# Prérequis : Azure CLI + Docker installés et lancés
# ==========================================================

set -e

# ---- VARIABLES ----
RESOURCE_GROUP="rg-carbonaze"
ACR_NAME="acrcarbonaze"
APP_NAME="carbonaze-backend"
ACR_LOGIN_SERVER="${ACR_NAME}.azurecr.io"
IMAGE_TAG="${ACR_LOGIN_SERVER}/${APP_NAME}:latest"

echo "🚀 Déploiement de carbonaze-backend..."
echo ""

# ---- 1. Build du JAR Maven ----
echo "📦 Build Maven..."
mvn clean package -DskipTests -q
echo "✅ JAR généré"

# ---- 2. Connexion à ACR ----
echo ""
echo "🔐 Connexion au Container Registry..."
az acr login --name $ACR_NAME

# ---- 3. Build et push de l'image Docker ----
echo ""
echo "🐳 Build de l'image Docker..."
docker build -t $IMAGE_TAG .
echo ""
echo "⬆️  Push vers ACR..."
docker push $IMAGE_TAG
echo "✅ Image poussée : $IMAGE_TAG"

# ---- 4. Redéploiement de la Container App ----
echo ""
echo "☁️  Redéploiement sur Azure Container Apps..."
az containerapp update \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --image $IMAGE_TAG

# ---- 5. Résumé ----
APP_URL=$(az containerapp show \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --query "properties.configuration.ingress.fqdn" -o tsv)

echo ""
echo "============================================================"
echo "✅ Déploiement terminé !"
echo "🌐 URL : https://$APP_URL"
echo "============================================================"
