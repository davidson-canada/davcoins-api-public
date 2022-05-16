# davcoins-api

Repository for the backend APIs of the Dav'Coin project

Prerequisites
---------
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [IntelliJ](https://www.jetbrains.com/fr-fr/idea/download)

Quick Start
-----------
- clone the project:
```
gh repo clone davidson-canada/davcoins-api
```
- build the project in your IDE:
```
mvn clean install
```

Local Testing
-------------
- define a VM Option in your SpringBoot Application Configuration
```
-Dspring.profiles.active=local
```
- create a PostgreSQL database in your name in the [Google Cloud project](https://console.cloud.google.com/sql/instances/create;engine=PostgreSQL?authuser=1&project=dav-coins)
  the database name should follow this pattern : "your-name"-localdev
  
- create an application-local.properties file where you will substitute the properties declared in application-dev.properties
    the values of theses properties cand be found either in the [Google Cloud Secret Manager](https://console.cloud.google.com/security/secret-manager?authuser=1&project=dav-coins), in the environment variables of the pod in the [Google Cloud console](https://console.cloud.google.com/run/detail/northamerica-northeast1/davcoins-api/metrics?authuser=1&project=dav-coins), or in the configuration of your local database
