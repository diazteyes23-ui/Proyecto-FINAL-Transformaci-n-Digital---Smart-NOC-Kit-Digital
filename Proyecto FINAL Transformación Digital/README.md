# 🚀 SmartNOC – Centro de Operaciones de Red Digitalizado
> Proyecto Final Transformación Digital · España 2026

---

## 📁 Estructura del Proyecto

```
Proyecto FINAL Transformación Digital/
│
├── index.html          ← Frontend (abrir en navegador)
├── style.css           ← Estilos dark-tech
├── app.js              ← Gráficos y animaciones
│
└── SmartNOC-Backend/   ← Backend Spring Boot (Eclipse)
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/noc/smartnoc/
        │   │   ├── SmartNOCApplication.java     ← Arranque
        │   │   ├── model/
        │   │   │   ├── Incidencia.java          ← Entidad JPA
        │   │   │   ├── Tecnico.java             ← Entidad JPA
        │   │   │   ├── Equipo.java              ← Entidad JPA
        │   │   │   └── OrdenTrabajo.java        ← Entidad JPA
        │   │   ├── repository/
        │   │   │   ├── IncidenciaRepository.java
        │   │   │   ├── TecnicoRepository.java
        │   │   │   ├── EquipoRepository.java
        │   │   │   └── OrdenTrabajoRepository.java
        │   │   ├── service/
        │   │   │   ├── IncidenciaService.java   ← Lógica de negocio
        │   │   │   └── OrdenTrabajoService.java
        │   │   ├── controller/
        │   │   │   ├── IncidenciaController.java   ← REST API
        │   │   │   ├── TecnicoController.java
        │   │   │   └── OrdenTrabajoController.java
        │   │   └── config/
        │   │       └── CorsConfig.java          ← CORS para frontend
        │   └── resources/
        │       ├── application.properties       ← Config MySQL
        │       └── schema.sql                   ← BD + datos prueba
        └── test/
            └── java/com/noc/smartnoc/
                ├── service/IncidenciaServiceTest.java   ← Tests unitarios
                └── controller/IncidenciaControllerTest.java ← Tests REST
```

---

## ⚙️ Pasos para ejecutar

### 1. Crear la base de datos en MySQL

Abre **MySQL Workbench** y ejecuta el archivo:
```
SmartNOC-Backend/src/main/resources/schema.sql
```
Esto crea la base de datos `smart_noc` con tablas y datos de prueba.

### 2. Configurar contraseña MySQL

Edita `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

### 3. Importar en Eclipse

1. `File → Import → Existing Maven Projects`
2. Selecciona la carpeta `SmartNOC-Backend/`
3. Eclipse descargará las dependencias automáticamente
4. Click derecho en `SmartNOCApplication.java → Run As → Spring Boot App`

### 4. Verificar que funciona

El servidor arranca en `http://localhost:8080`

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | **Documentación API interactiva** |
| `http://localhost:8080/api/v1/incidencias` | Lista incidencias |
| `http://localhost:8080/api/v1/tecnicos` | Lista técnicos |
| `http://localhost:8080/api/v1/ordenes-trabajo` | Lista órdenes |
| `http://localhost:8080/api/v1/incidencias/kpis` | KPIs: MTTR y activas |

### 5. Ver el Frontend

Abre `index.html` directamente en el navegador (doble clic).

---

## 🏗️ Arquitectura MVC

```
┌─────────────────┐    HTTP/JSON    ┌──────────────────┐
│   Frontend      │ ─────────────→  │   Controller     │  ← Capa MVC (C)
│  (HTML/JS)      │ ←─────────────  │  @RestController │
└─────────────────┘                 └────────┬─────────┘
                                             │
                                    ┌────────▼─────────┐
                                    │    Service       │  ← Lógica negocio
                                    │  @Service        │
                                    └────────┬─────────┘
                                             │
                                    ┌────────▼─────────┐
                                    │   Repository     │  ← Capa MVC (M)
                                    │  JpaRepository   │
                                    └────────┬─────────┘
                                             │
                                    ┌────────▼─────────┐
                                    │   MySQL DB       │
                                    │  smart_noc       │
                                    └──────────────────┘
```

---

## 📡 API REST – Endpoints principales

### Incidencias
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/v1/incidencias` | Listar todas |
| GET | `/api/v1/incidencias/{id}` | Por ID |
| GET | `/api/v1/incidencias/estado/ABIERTA` | Filtrar por estado |
| GET | `/api/v1/incidencias/kpis` | KPIs MTTR + activas |
| POST | `/api/v1/incidencias` | Crear nueva |
| PATCH | `/api/v1/incidencias/{id}/estado` | Cambiar estado |
| DELETE | `/api/v1/incidencias/{id}` | Eliminar |

### Técnicos
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/v1/tecnicos` | Listar todos |
| GET | `/api/v1/tecnicos/disponibles` | Solo disponibles |
| GET | `/api/v1/tecnicos/zona/Zaragoza Norte` | Por zona |
| POST | `/api/v1/tecnicos` | Registrar técnico |
| PUT | `/api/v1/tecnicos/{id}` | Actualizar |

### Órdenes de Trabajo (FSM)
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/v1/ordenes-trabajo` | Listar todas |
| GET | `/api/v1/ordenes-trabajo/estado/PENDIENTE` | Filtrar |
| GET | `/api/v1/ordenes-trabajo/tecnico/{id}` | Por técnico |
| POST | `/api/v1/ordenes-trabajo` | Crear + despachar |
| PATCH | `/api/v1/ordenes-trabajo/{id}/estado` | Actualizar estado |

---

## 🧪 Ejecutar Tests

En Eclipse, click derecho sobre la carpeta `test/` → `Run As → JUnit Test`

O desde terminal:
```bash
mvn test
```

Tests incluidos:
- `IncidenciaServiceTest` — 5 tests unitarios con Mockito
- `IncidenciaControllerTest` — 4 tests REST con MockMvc

---

## 🛡️ Stack Tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | Java 17 + Spring Boot 3.2 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL 8.x |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Testing | JUnit 5 + Mockito + MockMvc |
| Frontend | HTML5 + CSS3 + JavaScript + Chart.js |
| IDE Backend | Eclipse |
| IDE Frontend | Visual Studio Code |
