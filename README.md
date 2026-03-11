# Foro API

Proyecto de backend Spring Boot para un foro simple. Permite registrar usuarios, autenticarse con JWT y manejar tópicos.

## Endpoints principales

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| POST `/usuarios` | Registrar un usuario | no requerida |
| POST `/auth` | Obtener token JWT (Bearer) | no requerida |
| GET `/topicos` | Listar tópicos activos (paginado) | abierta |
| GET `/topicos/{id}` | Detalle de tópico | abierta |
| POST `/topicos` | Crear tópico | debe estar autenticado |
| PUT `/topicos` | Actualizar tópico (propio) | debe estar autenticado |
| DELETE `/topicos/{id}` | Eliminar (marcar inactivo) tópico propio | debe estar autenticado |

## Seguridad

- Se usa Spring Security con un filtro `JwtAuthFilter` que valida el header `Authorization: Bearer TOKEN`.
- Los tokens se generan usando `io.jsonwebtoken:jjwt` y una clave configurable en `application.properties`.
- Solo el autor de un tópico puede modificarlo o eliminarlo.

## Modelos importantes

- `Usuario` (id, nombre, email, password) - Password se almacena encriptada con BCrypt.
- `Topico` (id, titulo, mensaje, fechaCreacion, status, autor, curso)

## Base de datos

- H2 en memoria configurada por defecto.
- Flyway se encarga de crear las tablas (`V1` y `V2`).
- Al iniciar se insertan datos de ejemplo (usuario `admin@foro.com` / `admin123` y 3 tópicos).
- Se puede acceder a la consola en `http://localhost:8080/h2-console` (permite SQL). Usuario/Password: `sa`.

## Ejecutar

1. Compilar:
   ```bash
   ./mvnw clean package
   ```
2. Iniciar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Probar con Insomnia/Postman como se describe en la introducción del desafío.

## Pruebas

Hay un test de integración (`ForoApplicationTests`) que cubre registro, autenticación,
creación, actualización y eliminación de un tópico.

---

¡Sientete libre de extender el proyecto, agregar frontend, comentarios, respuestas, etc!
