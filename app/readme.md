# 🏥 Medivol App - Tu Gestión Médica al Alcance de tu Mano

![Logotipo de Medivol (opcional)](/screenshots/logo.png) ## ✨ Visión General

**Medivol** es una aplicación móvil desarrollada en **Kotlin** 📱 para la plataforma Android, diseñada para optimizar la gestión de citas y datos médicos. Esta aplicación se conecta a un potente servicio backend desarrollado con **Java Spring Boot** 🍃, ofreciendo un control de acceso robusto mediante **JWT (JSON Web Tokens)** 🔑 y persistencia de datos en **MySQL** 🗄️.

Actualmente, Medivol proporciona funcionalidades esenciales como el inicio de sesión y la visualización de listas de médicos, pacientes y consultas. La visión a futuro es expandir la aplicación para ofrecer una gestión **CRUD** (Crear, Leer, Actualizar, Eliminar) completa para todas las entidades (usuarios, médicos, pacientes y consultas), brindando una solución integral para la administración médica.

## 🚀 Características Actuales

* **Autenticación de Usuarios:** Sistema de `login` seguro con JWT.
* **Gestión de Roles:** Soporte para `roles de usuario` (USER) y `administrador` (ADMIN) para un control de acceso granular.
* **Listado de Médicos:** Visualización de la lista de profesionales médicos.
* **Listado de Pacientes:** Visualización de la lista de pacientes registrados.
* **Listado de Consultas:** Visualización de las citas médicas programadas.
* **Conectividad Robusta:** Consumo de la API REST a través de `Retrofit`.

## 🛠️ Tecnologías Utilizadas

### **Frontend (Android App)** 🤖

* **Lenguaje:** **Kotlin** 👨‍💻
* **API Nivel:** API **36** (desarrollada)
* **Compatibilidad:** Android **13+** (API 33 en adelante)
* **Comunicación API:** **Retrofit** 🔄
* **Diseño UI:** **Material Design** ✨
* **Librerías:** **AndroidX Libraries**

### **Backend (API REST)** ☕

* **Framework:** **Java Spring Boot** 🍃
* **Seguridad:** **Spring Security** 🛡️ con **JWT (JSON Web Tokens)** 🔑
* **Base de Datos:** **MySQL** 🗄️

## 📸 Capturas de Pantalla

Aquí puedes añadir algunas capturas de pantalla de tu aplicación para mostrar su interfaz y funcionalidad.

| Pantalla de Inicio de Sesión | Lista de Médicos |
| :--------------------------: | :--------------: |
| ![Pantalla de Login](/screenshots/login_screen.png) | ![Lista de Médicos](/screenshots/medicos_list.png) |

| Lista de Pacientes | Lista de Consultas |
| :----------------: | :----------------: |
| ![Lista de Pacientes](/screenshots/pacientes_list.png) | ![Lista de Consultas](/screenshots/consultas_list.png) |

---

**Nota:**
* Reemplaza los paths `/screenshots/login_screen.png`, etc., con las rutas reales de tus imágenes en tu repositorio GitHub. Es común crear una carpeta `screenshots` en la raíz del repositorio para esto.
* Asegúrate de que tus capturas de pantalla sean claras y muestren los aspectos más importantes de la interfaz de usuario.
* Si tienes un logotipo para "Medivol", puedes añadirlo al principio.

## ⚙️ Configuración y Ejecución

### Requisitos Previos

* **Android Studio** (para la aplicación Android)
* **Java Development Kit (JDK)** 17+ (para el backend Spring Boot)
* **MySQL Server**
* Conocimientos básicos de **Git**

### Configuración del Backend

1.  **Clona el repositorio del Backend:**
    ```bash
    git clone [https://github.com/tu_usuario/tu_repo_backend.git](https://github.com/tu_usuario/tu_repo_backend.git)
    cd tu_repo_backend
    ```
2.  **Configura la Base de Datos:**
    * Crea una base de datos MySQL llamada `db_medi_vol`.
    * Asegúrate de que la configuración de la conexión a la base de datos en `application.properties` o `application.yml` de tu proyecto Spring Boot sea correcta (usuario, contraseña, etc.).
    * Ejecuta las migraciones o scripts SQL para crear las tablas (`usuarios`, `medicos`, `pacientes`, `consultas`, etc.) y poblar algunos datos iniciales.
3.  **Ejecuta el Backend:**
    ```bash
    ./mvnw spring-boot:run # Para usuarios de Maven
    # o si usas Gradle:
    ./gradlew bootRun
    ```
    Asegúrate de que el backend se inicie en el puerto esperado (por defecto 8080) y que la URL base de tu API esté expuesta correctamente (ej. `http://localhost:8080/api/`).

### Configuración de la Aplicación Android

1.  **Clona este repositorio (Frontend):**
    ```bash
    git clone [https://github.com/tu_usuario/este_repo_frontend.git](https://github.com/tu_usuario/este_repo_frontend.git)
    cd este_repo_frontend
    ```
2.  **Abre el Proyecto en Android Studio.**
3.  **Configura la URL de la API:**
    * En tu `ApiClient.kt` (o donde configures Retrofit), asegúrate de que `BASE_URL` apunte a la dirección IP de tu máquina si estás probando en un emulador o dispositivo físico. Por ejemplo: `private const val BASE_URL = "http://192.168.1.XX:8080/api/"`.
    * **Importante:** `localhost` o `127.0.0.1` en el emulador se refieren al propio emulador. Usa `10.0.2.2` si el backend está en tu máquina local y usas el emulador de Android Studio, o la IP local de tu máquina para dispositivos físicos.
4.  **Ejecuta la Aplicación:**
    * Conecta un dispositivo Android (versión 13 o superior) o inicia un emulador.
    * Haz clic en el botón "Run" en Android Studio.

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si deseas contribuir a este proyecto, por favor sigue estos pasos:

1.  Haz un "fork" del repositorio.
2.  Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3.  Realiza tus cambios y haz "commit" (`git commit -am 'feat: Añade nueva funcionalidad X'`).
4.  Sube tus cambios a tu "fork" (`git push origin feature/nueva-funcionalidad`).
5.  Abre un "Pull Request" a la rama `main` de este repositorio.

## 📜 Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

**¡Gracias por revisar el proyecto Medivol!**

---