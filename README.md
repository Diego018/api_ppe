# API Gestión de Estudiantes - PPE

API REST para gestión de estudiantes desarrollada con Spring Boot como parte del proyecto PPE. Implementa arquitectura MVC, GitFlow y CI/CD con GitHub Actions.

---

## 📋 Requisitos Previos
- Java 25 (Oracle JDK)
- Gradle (incluido en el proyecto)
- MySQL 8+
- Git

---

##  Instrucciones de Ejecución

### 1. Clonar repositorio
en git bash:
git clone https://github.com/Diego018/api_ppe.git
cd api_ppe

### 2. Configurar base de datos

En MySQL debemos de crear una base de datos exactamente con el nombre "creacionApi"
CREATE DATABASE creacionApi;

###- Editamos el application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/creacionApi?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

### 3. Ejecutamos la aplicación
./gradlew bootRun

### 4. Probamos los endpoints 
Podemos probar los endpoints ya sea en la herramienta "Postman" o en alguna otra que pruebe APIs


Método	Endpoint	Descripción
GET	/students	Listar todos los estudiantes
GET	/students/{id}	Buscar por ID
POST	/students	Crear estudiante
PUT	/students/{id}	Actualizar
DELETE	/students/{id}	Eliminar

### Ejemplo POST:

json
{
  "name": "Juan Pérez",
  "career": "Ingeniería Informática"
}

## Estructura del proyecto y dependencias

api_ppe/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── src/
│   ├── main/
│   │   ├── java/com/diego/api/
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java
│   │   │   ├── entity/
│   │   │   │   └── Student.java
│   │   │   ├── repository/
│   │   │   │   └── IStudentRepository.java
│   │   │   ├── service/
│   │   │   │   └── StudentService.java
│   │   │   └── ApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/diego/api/
│           └── ApiApplicationTests.java
│
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md

### build.gradle

plugins {
    id 'java'
    id 'org.springframework.boot' version '4.0.2'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.diego'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}

### Justificación de la Estrategia de Git: GitFlow vs Trunk-Based
Estrategia elegida: GitFlow

¿Por qué GitFlow y no Trunk-Based?

Mantenía main siempre estable
Con GitFlow, la rama main solo recibió código ya probado en development. Esto asegura que en cualquier momento el proyecto está listo para entregar sin errores.

Tenía un espacio para probar antes de entregar
La rama development me permitió desarrollar tranquilo, equivocarme y corregir sin afectar la rama principal. En Trunk-Based todo se hace directo en main y eso es riesgoso cuando recién estás aprendiendo.

El profesor puede ver todo el proceso
GitFlow deja un historial claro: están main, development, el Pull Request y el merge. Es fácil de evaluar. Trunk-Based es más desordenado y todo se mezcla.

Los releases automáticos necesitan una rama confiable
El pipeline está configurado para generar el Release automático cuando se hace push a main. Con GitFlow, main siempre tiene código probado. Con Trunk-Based, no hay esa garantía.

Es un proyecto individual, no una empresa grande
GitFlow funciona perfecto para 1 persona. Trunk-Based es para equipos enormes que lanzan código muchas veces al día. No era nuestro caso.

En resumen:
Elegí GitFlow porque es más ordenado, más seguro para un proyecto académico y deja evidencia clara de todo lo que hice. main quedó como la versión final impecable, development fue mi espacio de trabajo, y el release automático se disparó solo cuando todo estuvo listo.

### Explicación herramientas del Pipeline
1. actions/checkout@v4
Qué hace: Clona el repositorio en el runner de GitHub

Por qué: Necesario para tener acceso al código y poder compilarlo

2. actions/setup-java@v4
Qué hace: Instala y configura Oracle JDK 25 (Early Access)

Por qué: El proyecto requiere Java 25 para Spring Boot 4.0.2

3. chmod +x gradlew
Qué hace: Otorga permisos de ejecución al wrapper de Gradle

Por qué: Los runners de Linux no tienen permisos por defecto

4. gradle/actions/setup-gradle@v4
Qué hace: Configura Gradle y cachea dependencias

Por qué: Acelera el build al no descargar dependencias cada vez

5. ./gradlew build
Qué hace: Compila el código, ejecuta pruebas y empaqueta la aplicación

Por qué: Verifica que el proyecto funcione en un entorno limpio

6. softprops/action-gh-release@v2
Qué hace: Crea automáticamente un GitHub Release

Por qué: Automatiza la entrega versionada del proyecto

Condición: Solo se ejecuta en push a main

7. GITHUB_TOKEN (secreto)
Qué hace: Token de autenticación automático de GitHub

Por qué: Permite que el action cree Releases sin credenciales manuales

### Disparadores del pipeline:

on:
  push: [development, main]        # Al subir código
  pull_request: [development, main] # Al crear PR
