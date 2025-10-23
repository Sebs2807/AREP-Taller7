# AREP - Taller 7

Proyecto monolito Spring Boot para practicar un microblog (hilos, posts, usuarios).

## Estado actual
- Backend: Spring Boot (Java 21)
- Base: JPA con PostgreSQL (configuración en `src/main/resources/application.properties`)
- Frontend: `frontend/index.html` (estático)
- Añadido: OpenAPI / Swagger UI

## Construir y ejecutar

Requisitos: Java 21, Maven. En PowerShell desde la carpeta `arep`:

```powershell
mvn test
mvn spring-boot:run
```

La aplicación por defecto levanta en `http://localhost:8080`.

## Swagger / OpenAPI
Una vez la aplicación esté corriendo, la documentación y la UI de Swagger estarán disponibles en:

- Swagger documentation: `http://localhost:8080/swagger-ui.html` (o `/swagger-ui/index.html`)

Se puede usar esa UI para explorar y probar los endpoints (crear usuarios, hilos y posts).

## Endpoints principales

- GET `/usuarios` — listar usuarios
- POST `/usuarios` — crear usuario (envía JSON de `Usuario`)
- GET `/hilos` — listar hilos
- POST `/hilos` — crear hilo (envía JSON de `Hilo`, o usa desde Swagger)
- POST `/hilos/{hiloId}/posts?userId={userId}&content={texto}` — crear un post en un hilo
- GET `/hilos/{hiloId}/posts` — listar posts de un hilo

Ejemplo rápido (curl):

```powershell
# Crear usuario
curl -X POST -H "Content-Type: application/json" -d '{"username":"alice","email":"a@x.com","displayName":"Alice"}' http://localhost:8080/usuarios

# Crear hilo (body JSON)
curl -X POST -H "Content-Type: application/json" -d '{"title":"Mi hilo","owner":{"id":"usuario_xxx"}}' http://localhost:8080/hilos

# Crear post (params)
curl -X POST "http://localhost:8080/hilos/{hiloId}/posts?userId={userId}&content=Hola%20mundo"
```
