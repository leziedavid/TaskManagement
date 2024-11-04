# Étape 1: Construction de l'application
FROM maven:3.9.4-eclipse-temurin-21 as build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
COPY src ./src

# Compilation du projet
RUN mvn clean package -DskipTests

# Étape 2: Image finale avec l'application Spring Boot
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copier le jar généré depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080 pour l'application Spring Boot
EXPOSE 8080

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
