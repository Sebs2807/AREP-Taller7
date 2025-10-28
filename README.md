# 🏛️ MiniTwitter – Microservicios en AWS Lambda

## 📄 Resumen del Proyecto

Este proyecto corresponde al Taller de Arquitectura Empresarial, donde se implementa una **aplicación web tipo Twitter** con una arquitectura moderna basada en **microservicios** desplegados en **AWS Lambda**. El sistema incluye autenticación JWT y un frontend JavaScript interactivo.

La solución está compuesta por:

* **Backend:** Tres microservicios independientes (Usuarios, Hilos, Posts) desarrollados en Java (Spring Boot), expuestos como funciones Lambda y orquestados por API Gateway.
* **Frontend:** Aplicación JS estática desplegada en S3, disponible en: [MiniTwitter S3](http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com)

---

## 🏗️ Arquitectura del Sistema

**Diagrama general:**

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│  Frontend   │───► │ API Gateway │───►   │ AWS Lambda  │
│   (S3)      │      │             │      │ (Usuarios)  │
│  JS + HTML  │      │             │      │ (Hilos)     │
└─────────────┘      └─────────────┘      │ (Posts)     │
										  └─────────────┘
```

* **Frontend:** Cliente JS consume los endpoints REST protegidos con JWT.
* **Backend:** Cada microservicio gestiona su propio dominio y persistencia en PostgreSQL (NeonDB).
* **Seguridad:** Autenticación y autorización con JWT; los tokens se generan y validan en el backend.

---

## 🔐 Configuración JWT

La autenticación se implementa usando **JSON Web Tokens (JWT)**. El backend incluye:

* **Generación de tokens:** Al iniciar sesión, el usuario recibe un JWT firmado.
* **Validación:** Los endpoints protegidos verifican el token usando filtros personalizados (`JwtAuthFilter`, `SecurityConfig`).
* **Configuración:**
  - El secreto y parámetros del JWT se definen en el backend (`application.properties` y clases de seguridad).
  - El frontend almacena el token en localStorage y lo envía en el header `Authorization: Bearer <token>`.

---

## 🌐 Frontend JS en S3

Se desarrolló una aplicación JS que permite:

* Registro y login de usuarios.
* Creación y visualización de hilos y posts.
* Consumo de los microservicios vía fetch/AJAX.

**URL pública:** [http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com](http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com)

---

## 📦 Endpoints Principales

* **Usuarios:**
  - `GET /usuarios` – Listar usuarios
  - `POST /usuarios` – Crear usuario

* **Hilos:**
  - `GET /hilos` – Listar hilos
  - `POST /hilos` – Crear hilo (requiere JWT)

* **Posts:**
  - `GET /hilos/{hiloId}/posts` – Listar posts de un hilo
  - `POST /hilos/{hiloId}/posts` – Crear post (requiere JWT)

---

## ⚙️ Instrucciones de Implementación

### ✅ Requisitos

* AWS CLI y SAM CLI
* Java 17 y Maven
* PostgreSQL NeonDB
* Cuenta AWS con permisos Lambda y S3

---

### 🧩 Ejecución local

1. Clonar el repositorio:
	```powershell
	git clone https://github.com/Sebs2807/AREP-Taller7.git
	cd AREP-Taller7
	```
2. Compilar y ejecutar backend:
	```powershell
	mvn clean package -DskipTests
	sam local start-api --template template.yaml
	```
3. Probar endpoints en Postman:
	```
	POST http://localhost:3000/usuarios
	POST http://localhost:3000/auth/login
	```

---

### ☁️ Despliegue en AWS Lambda y S3

1. Empaquetar y desplegar backend:
	```powershell
	sam deploy --guided --template-file template.yaml --stack-name minitwitter-backend --s3-bucket <tu-bucket>
	```
2. Subir frontend a S3:
	```powershell
	aws s3 sync src/main/resources/static/ s3://minitwitter-camilo --acl public-read
	```
3. Acceder a la app:
	[http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com](http://minitwitter-camilo.s3-website-us-east-1.amazonaws.com)

---

## 🛠 Tecnologías Utilizadas

| Tecnología                  | Descripción                                    |
| --------------------------- | ---------------------------------------------- |
| **AWS Lambda**              | Backend serverless para microservicios         |
| **Spring Boot**             | Framework Java para REST y seguridad           |
| **JWT**                     | Autenticación y autorización                  |
| **PostgreSQL (NeonDB)**     | Base de datos relacional en la nube            |
| **AWS S3**                  | Hosting estático para frontend JS              |
| **API Gateway**             | Orquestación y routing de APIs                 |
| **AWS SAM**                 | Infraestructura como código y despliegue       |
| **JavaScript**              | Frontend dinámico y consumo de APIs            |

---

## 📸 Pruebas y Capturas

* Registro y login de usuarios
* Creación de hilos y posts
* Pruebas de endpoints protegidos con JWT
* Acceso público al frontend JS

---

## 📚 Documentación y Referencias

* [AWS Lambda + Java](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)
* [Spring Boot + JWT](https://spring.io/guides/gs/securing-web/)
* [AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html)

---

## 🎥 Video de Implementación

* (Agrega aquí el enlace a tu video de presentación si lo tienes)
