# API REST de Citas Médicas - Medivol Backend

![Logo de Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Logo de Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Logo de MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Logo de Flyway](https://img.shields.io/badge/Flyway-CC3333?style=for-the-badge&logo=flyway&logoColor=white)
![Logo de JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)

## ✨ Visión General

Esta es la API REST (Backend) de **Medivol**, un sistema integral para la gestión de citas médicas. Desarrollada con **Java Spring Boot**, esta API proporciona una base robusta y segura para la aplicación móvil (u otros clientes) que consume sus servicios.

La API maneja todas las operaciones **CRUD** (Crear, Leer, Actualizar, Eliminar) para las principales entidades del sistema médico: **Usuarios**, **Médicos**, **Pacientes** y **Consultas**. La seguridad es una prioridad, implementando autenticación y autorización mediante **JWT (JSON Web Tokens)**, garantizando que solo los usuarios autorizados puedan acceder a los recursos protegidos. Las migraciones de base de datos son gestionadas eficientemente con **Flyway**, asegurando un control de versiones y despliegue consistente del esquema de la base de datos **MySQL**.

## 🚀 Características Principales

* **Gestión de Usuarios:**
    * Registro y autenticación de usuarios (`/login`).
    * Roles de usuario (`ROLE_USER`, `ROLE_ADMIN`) para control de acceso granular.
    * CRUD completo para la administración de usuarios.
* **Gestión de Médicos:**
    * Registro, consulta, actualización y eliminación de información de médicos.
    * Filtrado y listado de médicos disponibles.
* **Gestión de Pacientes:**
    * Registro, consulta, actualización y eliminación de información de pacientes.
    * Listado de pacientes registrados.
* **Gestión de Consultas Médicas:**
    * Agendamiento y cancelación de citas médicas.
    * Consulta de consultas por médico, paciente o fecha.
* **Seguridad Robustas:**
    * Autenticación basada en **JWT** para sesiones sin estado.
    * Autorización basada en roles (`hasRole('ADMIN')`, `hasRole('USER')`).
    * Cifrado de contraseñas con **BCrypt**.
* **Manejo de Base de Datos:**
    * Persistencia de datos en **MySQL**.
    * Gestión de migraciones de esquema de base de datos con **Flyway**.
* **Documentación:**
    * Documentación de la API generada automáticamente con **Swagger/OpenAPI** (`/v3/api-docs`, `/swagger-ui.html`).

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** `Java 17+` ☕
* **Framework:** `Spring Boot` 🍃
* **Base de Datos:** `MySQL` 🗄️
* **Migraciones DB:** `Flyway` ✈️
* **Seguridad:** `Spring Security` 🛡️, `JWT (JSON Web Tokens)` 🔑
* **Persistencia:** `Spring Data JPA`
* **Construcción:** `Maven` o `Gradle`
* **Librerías Útiles:** `Lombok` (para reducir código boilerplate), `Jakarta Validation` (para validación de datos).

## ⚙️ Configuración y Ejecución

Para levantar esta API en tu entorno local, sigue los siguientes pasos:

### Requisitos Previos

Asegúrate de tener instalados:

* **Java Development Kit (JDK) 17 o superior**
* **Maven** o **Gradle** (según tu preferencia de gestión de proyectos)
* **MySQL Server**
* Un cliente HTTP como **Postman**, **Insomnia**, o **cURL** para probar los endpoints.

### 1. Clona el Repositorio

```bash
git clone [https://github.com/tu_usuario/tu_repo_api_medivol.git](https://github.com/tu_usuario/tu_repo_api_medivol.git)
cd tu_repo_api_medivol
