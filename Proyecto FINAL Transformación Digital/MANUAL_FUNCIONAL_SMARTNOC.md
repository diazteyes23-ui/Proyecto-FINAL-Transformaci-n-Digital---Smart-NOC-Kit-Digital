# 📚 MANUAL FUNCIONAL - SmartNOC
## Guía Operativa del Sistema de Gestión del Centro de Operaciones de Red

---

**Versión:** 1.0  
**Fecha:** 12 de mayo de 2026  
**Audiencia:** Operadores, Técnicos, Administradores  
**Clasificación:** Interna  

---

## 📑 Tabla de Contenidos

1. [Introducción](#introducción)
2. [Conceptos Fundamentales](#conceptos-fundamentales)
3. [Arquitectura del Sistema](#arquitectura-del-sistema)
4. [Flujo General de Operaciones](#flujo-general-de-operaciones)
5. [Gestión de Incidencias](#gestión-de-incidencias)
6. [Gestión de Órdenes de Trabajo](#gestión-de-órdenes-de-trabajo)
7. [Ejemplos Prácticos](#ejemplos-prácticos)
8. [Base de Datos](#base-de-datos)
9. [Consultas Comunes](#consultas-comunes)
10. [Troubleshooting](#troubleshooting)

---

## 🎯 Introducción

### ¿Qué es SmartNOC?

SmartNOC es una plataforma digital que centraliza la gestión de incidencias y órdenes de trabajo en un Centro de Operaciones de Red (NOC). Permite que los operadores del NOC:

- 📊 Monitoreen incidencias en tiempo real
- 👷 Asignen técnicos a trabajos de reparación
- 📍 Rastrreen la ubicación y disponibilidad de técnicos
- ⏱️ Midan el desempeño del equipo (MTTR, TTD)
- 📈 Generen reportes y KPIs

### Escenario Real

**Situación:** Es martes 12 de mayo de 2026, 09:15 AM en Zaragoza

Una OLT (Optical Line Terminal) comienza a mostrar degradación. 1,240 clientes de fibra óptica están siendo afectados.

**Lo que pasaría SIN SmartNOC:**
- El operador llama por teléfono a técnicos
- Espera a que alguien conteste
- Desconoce ubicación exacta del trabajo
- No sabe si cumplirá el SLA
- No hay registro de la incidencia

**Lo que pasa CON SmartNOC:**
1. El sistema crea automáticamente la incidencia
2. Identifica técnicos disponibles en Zaragoza Norte
3. Asigna a Carlos Martínez (especialista en FTTH)
4. Carlos recibe notificación en su móvil
5. Sistema calcula ETA automáticamente
6. Se monitorea el progreso en tiempo real
7. Queda registro completo de la incidencia

---

## 💡 Conceptos Fundamentales

### Entidades Principales

#### 1. **TÉCNICO** 👷
Profesional que resuelve incidencias en el campo.

```
Carlos Martínez López
├─ Email: carlos.martinez@smartnoc.es
├─ Teléfono: 612345678
├─ Skills: [FTTH_L3, OPGW]
├─ Zona: Zaragoza Norte
├─ Disponible: ✅ Sí
└─ Ubicación: 41.6601°N, -0.8774°O (GPS)
```

**Estados:**
- ✅ **Disponible**: Listo para nuevas órdenes
- 🚗 **En Camino**: Viajando al sitio
- ⚙️ **En Progreso**: Trabajando en la incidencia
- ❌ **No Disponible**: En pausa/offline

---

#### 2. **EQUIPO** 🔧
Dispositivo de red que requiere mantenimiento.

```
OLT-ZGZ-042 (Optical Line Terminal)
├─ Tipo: OLT
├─ Modelo: Huawei MA5800-X7
├─ IP: 10.100.42.1
├─ Site ID: ZGZ-042
├─ Zona: Zaragoza Norte
├─ Estado: DEGRADADO ⚠️
└─ Ubicación: 41.6560°N, -0.8800°O
```

**Tipos de Equipos:**
- `OLT`: Terminal de Línea Óptica
- `BTS_5G`: Estación base 5G
- `ONU`: Unidad de Red Óptica (en casa del cliente)
- `ROUTER`: Enrutador de red
- `SWITCH`: Conmutador Ethernet
- `RRU`: Unidad de Radio Remota

**Estados:**
- 🟢 **OPERATIVO**: Funcionamiento normal
- 🟡 **DEGRADADO**: Rendimiento reducido (⚠️ Alerta)
- 🔴 **FALLO**: Sin servicio (🚨 Crítico)
- 🔧 **MANTENIMIENTO**: En reparación programada

---

#### 3. **INCIDENCIA** 🚨
Problema o evento que afecta el servicio de red.

```
INC-20260512-001 (Código único)
├─ Título: Degradación OLT Zaragoza Norte
├─ Severidad: P1 (Crítica)
├─ Estado: EN_PROGRESO
├─ Clientes Afectados: 1,240
├─ Equipo: OLT-ZGZ-042
├─ TTD (Time To Detect): 4 minutos
└─ TTR (Time To Repair): En cálculo...
```

**Severidades:**
- 🔴 **P1**: CRÍTICA - Servicio caído o severamente degradado
  - TTR Objetivo: < 30 minutos
  - Escalamiento automático
  - Notificación inmediata a supervisores

- 🟠 **P2**: ALTA - Impacto significativo en clientes
  - TTR Objetivo: < 60 minutos
  - Notificación a supervisor

- 🟡 **P3**: MEDIA - Impacto limitado
  - TTR Objetivo: < 4 horas
  - Registro en BD

- 🟢 **P4**: BAJA - Informativa/Mantenimiento
  - TTR Objetivo: < 1 día
  - Puede programarse

**Estados de Incidencia:**
```
ABIERTA → EN_PROGRESO → RESUELTA → CERRADA
  ↓
  └─→ CANCELADA (en casos especiales)
```

---

#### 4. **ORDEN DE TRABAJO** 📋
Instrucción para que un técnico resuelva una incidencia.

```
WO-2026-08847 (Código único)
├─ Incidencia: INC-20260512-001
├─ Técnico Asignado: Carlos Martínez
├─ Estado: EN_PROGRESO
├─ Prioridad: P1
├─ SLA Deadline: 2026-05-12 09:45
├─ ETA: 12 minutos
└─ Distancia: 2.3 km
```

**Estados de Orden:**
```
PENDIENTE → DESPACHADA → EN_CAMINO → EN_PROGRESO → COMPLETADA
  ↑                                                      ↓
  └──────────────────── CANCELADA ←────────────────────┘
```

---

### Relaciones entre Entidades

```
┌─────────────────┐
│   INCIDENCIA    │
│  (Qué pasó)     │
└────────┬────────┘
         │ 1:N (Una incidencia puede generar
         │       múltiples órdenes)
         ↓
┌─────────────────────────┐          ┌──────────────┐
│  ORDEN DE TRABAJO       │ N:1      │  TÉCNICO     │
│  (Qué hacer)            ├─────────→│  (Quién      │
│                         │          │   lo hace)   │
└─────────────────────────┘          └──────────────┘
         │
         │ N:1
         ↓
┌─────────────────┐
│    EQUIPO       │
│  (Dónde)        │
└─────────────────┘
```

---

## 🏗️ Arquitectura del Sistema

### Componentes Interconectados

```
┌──────────────────────────────────────────────────────────────┐
│                     FRONTEND (HTML/CSS/JS)                   │
│                                                              │
│   Dashboard    │ Incidencias │ Técnicos │ Equipos │ KPIs    │
└──────────────────────────┬───────────────────────────────────┘
                           │
                  HTTP REST APIs (JSON)
                  ├─ GET    /api/v1/incidencias
                  ├─ POST   /api/v1/incidencias
                  ├─ PATCH  /api/v1/incidencias/{id}/estado
                  ├─ GET    /api/v1/ordenes-trabajo
                  ├─ POST   /api/v1/ordenes-trabajo
                  ├─ GET    /api/v1/tecnicos
                  └─ GET    /api/v1/equipos
                           │
┌──────────────────────────▼───────────────────────────────────┐
│              BACKEND - SPRING BOOT 3.2.5                     │
│                                                              │
│  Controllers (REST API)                                      │
│      ↓                                                       │
│  Services (Lógica de Negocio)                               │
│  ├─ IncidenciaService    (Generación de códigos, MTTR)      │
│  ├─ OrdenTrabajoService  (Asignación de técnicos, SLA)      │
│  ├─ TecnicoService       (Disponibilidad, ubicación)        │
│  └─ EquipoService        (Estado de equipos)                │
│      ↓                                                       │
│  Repositories (Spring Data JPA)                             │
│      ↓                                                       │
│  ORM Hibernation (Entity Mapping)                           │
└──────────────────────────┬───────────────────────────────────┘
                           │
                    JDBC / Connection Pooling
                           │
┌──────────────────────────▼───────────────────────────────────┐
│               MYSQL DATABASE (smart_noc)                     │
│                                                              │
│  Tablas:                                                     │
│  ├─ tecnicos          (4 registros)                         │
│  ├─ equipos           (4 registros)                         │
│  ├─ incidencias       (3 registros)                         │
│  └─ ordenes_trabajo   (conectada a incidencias)            │
└──────────────────────────────────────────────────────────────┘
```

### Flujo de Datos

```
1. Usuario abre navegador
   ↓
2. Browser solicita index.html
   ↓
3. JavaScript ejecuta app.js
   ↓
4. app.js hace petición AJAX a API
   ↓
5. Spring Boot recibe solicitud HTTP
   ↓
6. Controller enruta a servicio
   ↓
7. Service ejecuta lógica de negocio
   ↓
8. Repository consulta BD con JPA
   ↓
9. Hibernate traduce a SQL
   ↓
10. MySQL retorna ResultSet
    ↓
11. Hibernate mapea a entidades Java
    ↓
12. Service procesa datos
    ↓
13. Controller convierte a JSON
    ↓
14. HTTP Response 200 OK
    ↓
15. Browser recibe JSON
    ↓
16. JavaScript renderiza HTML dinámicamente
    ↓
17. Usuario ve datos en pantalla 👁️
```

---

## 🔄 Flujo General de Operaciones

### Un Día Típico en la Operación

#### ⏰ 09:00 - Inicio de Turno
**Estado inicial:**
- 4 técnicos disponibles
- Todos los equipos operativos (7/7)
- 0 incidencias activas

```
Técnicos Disponibles (Dashboard):
├─ Carlos Martínez    ✅ Disponible (Zaragoza Norte)
├─ Ana García         ✅ Disponible (Zaragoza Sur)
├─ Miguel Fernández   ❌ No disponible (Huesca)
└─ Laura Sánchez      ✅ Disponible (Teruel)

Equipos Operativos:
├─ OLT-ZGZ-042        🟢 Operativo
├─ BTS-5G-ZGZ-011     🟢 Operativo
├─ OLT-HUE-007        🟢 Operativo
├─ RRU-5G-TER-003     🟢 Operativo
└─ ... (3 más)
```

#### ⏰ 09:15 - EVENTO CRÍTICO
**Alerta:** Potencia óptica baja en OLT-ZGZ-042

```bash
# El Sistema NOC detecta automáticamente:
- TTD: 4 minutos desde degradación
- Clientes afectados: 1,240
- Severidad detectada: P1
- Equipo: OLT-ZGZ-042 (Huawei MA5800-X7)
```

**Sistema automáticamente:**
1. ✅ Crea INC-20260512-001
2. ✅ Asigna severidad P1
3. ✅ Identifica área afectada: Zaragoza Norte
4. ✅ Busca técnicos con skill FTTH_L3
5. ✅ Encuentra: Carlos Martínez disponible
6. ✅ Crea orden de trabajo WO-2026-08847
7. ✅ Notifica a Carlos
8. ✅ Inicia cronómetro de SLA

#### ⏰ 09:17 - Técnico Recibe Asignación
**Carlos Martínez** recibe notificación en su dispositivo:

```
🚨 NUEVA ORDEN P1
─────────────────
Incidencia: INC-20260512-001
Equipo: OLT-ZGZ-042
Ubicación: Zaragoza Norte (2.3 km)
ETA: 12 minutos
SLA: 30 minutos
```

**Carlos cambia estado:** "Disponible" → "EN_CAMINO"

#### ⏰ 09:29 - Técnico Llega al Sitio
**Carlos llega a las instalaciones**

```
📍 Ubicación: 41.6560°N, -0.8800°O
⏱️ Tiempo de llegada: 12 minutos
🔧 Equipo objetivo: OLT-ZGZ-042
```

**Carlos cambia estado:** "EN_CAMINO" → "EN_PROGRESO"

**Orden de trabajo se actualiza:**
```
WO-2026-08847
├─ Estado: EN_PROGRESO
├─ Inicio de trabajo: 09:29
├─ Notas: "Inspeccionando tarjetas GPON"
```

#### ⏰ 09:42 - Problema Identificado
**Carlos diagnostica:** Tarjeta GPON #3 con pérdida óptica

**Actualiza orden:**
```
Notas: "Tarjeta GPON #3 con -5dB de atenuación.
        Reemplazando por unidad de respaldo."
```

#### ⏰ 09:47 - Servicio Restaurado
**Carlos completa la reparación** - Servicio restaurado a 100%

```bash
# Impacto:
- Clientes recuperados: 1,240 ✅
- Tiempo desde detección: 43 minutos
- TTR (Time To Repair): 32 minutos
- SLA Status: CUMPLIDO ✅ (era deadline 09:45)
```

**Carlos cambia estado:** "EN_PROGRESO" → "Disponible"

**Sistema actualiza:**
1. Marca WO-2026-08847 como COMPLETADA
2. Marca INC-20260512-001 como RESUELTA
3. Calcula MTTR: 32 minutos
4. Registra en BD

#### ⏰ 10:00 - Reporte
**Dashboard actualizado:**

```
INCIDENCIA: INC-20260512-001
├─ Estado: RESUELTA ✅
├─ Severidad: P1
├─ TTD: 4 minutos
├─ TTR: 32 minutos
├─ MTTR: 32 minutos
├─ SLA Cumplido: ✅ SÍ
├─ Técnico: Carlos Martínez
└─ Clientes Recuperados: 1,240

KPIs ACTUALES:
├─ Incidencias Activas: 0
├─ MTTR Promedio: 32 minutos
├─ Disponibilidad: 99.95%
└─ SLA Cumplimiento: 100%
```

---

## 📋 Gestión de Incidencias

### Ciclo de Vida Completo

```
ABIERTA (Creada)
    ↓
    └─→ [Operador analiza]
    
EN_PROGRESO (Trabajando)
    ↓
    └─→ [Técnico en sitio]
    
RESUELTA (Arreglada)
    ↓
    └─→ [Operador verifica]
    
CERRADA (Finalizada)
    ↓
    └─→ [Confirmado por supervisor]
```

### Ejemplo: Creación de Incidencia (POSTMAN)

#### Paso 1: Crear la Incidencia

**Request:**
```http
POST http://localhost:8080/api/v1/incidencias
Content-Type: application/json

{
  "titulo": "Degradación OLT Zaragoza Norte",
  "descripcion": "Potencia óptica por debajo del umbral en 3 tarjetas GPON. Clientes reportan velocidad reducida.",
  "severidad": "P1",
  "tipo": "FTTH_OLT_DEGRADATION",
  "clientesAfectados": 1240,
  "equipo_id": 1
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "codigo": "INC-20260512-001",
  "titulo": "Degradación OLT Zaragoza Norte",
  "descripcion": "Potencia óptica por debajo del umbral en 3 tarjetas GPON. Clientes reportan velocidad reducida.",
  "severidad": "P1",
  "tipo": "FTTH_OLT_DEGRADATION",
  "estado": "ABIERTA",
  "clientesAfectados": 1240,
  "equipo": {
    "id": 1,
    "nombre": "OLT-ZGZ-042",
    "tipo": "OLT",
    "modelo": "Huawei MA5800-X7",
    "direccion_ip": "10.100.42.1",
    "zone": "Zaragoza Norte"
  },
  "fechaApertura": "2026-05-12T09:15:00",
  "ttdMinutos": 4,
  "creadoEn": "2026-05-12T09:15:00"
}
```

#### Paso 2: Cambiar a EN_PROGRESO

**Request:**
```http
PATCH http://localhost:8080/api/v1/incidencias/1/estado
Content-Type: application/json

{
  "estado": "EN_PROGRESO"
}
```

**Response:**
```json
{
  "id": 1,
  "codigo": "INC-20260512-001",
  "estado": "EN_PROGRESO",
  "fechaApertura": "2026-05-12T09:15:00"
}
```

#### Paso 3: Resolver la Incidencia

**Request:**
```http
PATCH http://localhost:8080/api/v1/incidencias/1/estado
Content-Type: application/json

{
  "estado": "RESUELTA"
}
```

**Response (Sistema calcula TTR automáticamente):**
```json
{
  "id": 1,
  "codigo": "INC-20260512-001",
  "estado": "RESUELTA",
  "fechaApertura": "2026-05-12T09:15:00",
  "fechaCierre": "2026-05-12T09:47:00",
  "ttdMinutos": 4,
  "ttrMinutos": 32
}
```

#### Paso 4: Cerrar la Incidencia

**Request:**
```http
PATCH http://localhost:8080/api/v1/incidencias/1/estado
Content-Type: application/json

{
  "estado": "CERRADA"
}
```

### Consultar Incidencias por Estado

**Request:**
```http
GET http://localhost:8080/api/v1/incidencias/estado/EN_PROGRESO
```

**Response:**
```json
[
  {
    "id": 1,
    "codigo": "INC-20260512-001",
    "titulo": "Degradación OLT Zaragoza Norte",
    "severidad": "P1",
    "estado": "EN_PROGRESO",
    "clientesAfectados": 1240,
    "fechaApertura": "2026-05-12T09:15:00"
  },
  {
    "id": 2,
    "codigo": "INC-20260512-002",
    "titulo": "Caída celda 5G Teruel",
    "severidad": "P2",
    "estado": "EN_PROGRESO",
    "clientesAfectados": 320,
    "fechaApertura": "2026-05-12T10:30:00"
  }
]
```

---

## 🛠️ Gestión de Órdenes de Trabajo

### Ciclo de Vida de una Orden

```
PENDIENTE (Creada, sin asignar)
    ↓
DESPACHADA (Técnico asignado)
    ↓
EN_CAMINO (Técnico viajando)
    ↓
EN_PROGRESO (Técnico trabajando)
    ↓
COMPLETADA (Trabajo terminado) ✅
    ↓
[Incidencia se marca como RESUELTA]

Alternativa:
    ↓
CANCELADA (Se cancela por cualquier razón)
```

### Crear una Orden de Trabajo

**Request:**
```http
POST http://localhost:8080/api/v1/ordenes-trabajo
Content-Type: application/json

{
  "incidencia_id": 1,
  "tecnico_id": 1,
  "prioridad": "P1",
  "sla_deadline": "2026-05-12T09:45:00",
  "eta_minutos": 12,
  "notas": "Equipo crítico. Prioridad máxima."
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "codigo": "WO-2026-08847",
  "incidencia_id": 1,
  "tecnico": {
    "id": 1,
    "nombre": "Carlos",
    "apellidos": "Martínez López",
    "email": "carlos.martinez@smartnoc.es",
    "skills": ["FTTH_L3", "OPGW"],
    "zona": "Zaragoza Norte",
    "disponible": true
  },
  "estado": "PENDIENTE",
  "prioridad": "P1",
  "sla_deadline": "2026-05-12T09:45:00",
  "eta_minutos": 12,
  "notas": "Equipo crítico. Prioridad máxima.",
  "creadoEn": "2026-05-12T09:17:00"
}
```

### Actualizar Estado de Orden

**Request: Cambiar a DESPACHADA**
```http
PATCH http://localhost:8080/api/v1/ordenes-trabajo/1/estado
Content-Type: application/json

{
  "estado": "DESPACHADA"
}
```

**Request: Cambiar a EN_CAMINO**
```http
PATCH http://localhost:8080/api/v1/ordenes-trabajo/1/estado
Content-Type: application/json

{
  "estado": "EN_CAMINO"
}
```

**Request: Actualizar con notas (EN_PROGRESO)**
```http
PATCH http://localhost:8080/api/v1/ordenes-trabajo/1
Content-Type: application/json

{
  "estado": "EN_PROGRESO",
  "notas": "En sitio. Inspeccionando OLT. Tarjeta GPON #3 con baja potencia."
}
```

**Request: Completar orden**
```http
PATCH http://localhost:8080/api/v1/ordenes-trabajo/1/estado
Content-Type: application/json

{
  "estado": "COMPLETADA"
}
```

**Response:**
```json
{
  "id": 1,
  "codigo": "WO-2026-08847",
  "estado": "COMPLETADA",
  "fecha_despacho": "2026-05-12T09:17:00",
  "fecha_cierre": "2026-05-12T09:47:00",
  "tiempo_total_minutos": 30
}
```

### Consultar Órdenes por Técnico

**Request:**
```http
GET http://localhost:8080/api/v1/ordenes-trabajo/tecnico/1
```

**Response:**
```json
[
  {
    "id": 1,
    "codigo": "WO-2026-08847",
    "incidencia": {
      "titulo": "Degradación OLT Zaragoza Norte"
    },
    "estado": "COMPLETADA",
    "prioridad": "P1",
    "fecha_cierre": "2026-05-12T09:47:00"
  },
  {
    "id": 2,
    "codigo": "WO-2026-08848",
    "incidencia": {
      "titulo": "Mantenimiento preventivo OPGW"
    },
    "estado": "EN_PROGRESO",
    "prioridad": "P3"
  }
]
```

---

## 📊 Ejemplos Prácticos

### Escenario 1: Incidencia de Fibra Cortada

#### Situación
Se corta accidentalmente una fibra óptica en Huesca. 180 clientes sin servicio.

#### Flujo de Datos

**1. Creación de Incidencia**
```bash
POST /api/v1/incidencias

Input:
{
  "titulo": "Fallo ONU masivo Huesca",
  "descripcion": "Corte de fibra en conducto subterráneo. Zona industrial.",
  "severidad": "P1",
  "tipo": "FTTH_MASS_ONU_FAIL",
  "clientesAfectados": 180,
  "equipo_id": 3
}

Output:
{
  "id": 3,
  "codigo": "INC-20260510-003",
  "estado": "ABIERTA",
  "fecha_apertura": "2026-05-10T14:30:00"
}
```

**2. El Sistema identifica técnicos en Huesca**
```sql
SELECT * FROM tecnicos 
WHERE zona = 'Huesca' AND disponible = TRUE;

Resultado:
- Miguel Fernández (NO disponible - en otra orden)

SELECT * FROM tecnicos 
WHERE skills LIKE '%FTTH_L3%' AND disponible = TRUE;

Resultado:
- Ana García (Zaragoza Sur) - DISPONIBLE ✅
- Laura Sánchez (Teruel) - DISPONIBLE ✅
```

**3. Asignar a técnico más cercano**
```bash
# Calcular distancia entre Huesca y ubicación de técnicos
# Ana: 71 km
# Laura: 95 km

# Resultado: Asignar a Ana García

POST /api/v1/ordenes-trabajo
{
  "incidencia_id": 3,
  "tecnico_id": 2,
  "prioridad": "P1",
  "eta_minutos": 45,
  "sla_deadline": "2026-05-10T15:00:00"
}

Output:
{
  "id": 3,
  "codigo": "WO-2026-08849",
  "tecnico_id": 2,
  "estado": "PENDIENTE"
}
```

**4. Ana recibe notificación**
- App: "NUEVA ORDEN P1 - Fallo masivo en Huesca"
- Ana cambia estado a "EN_CAMINO"

**5. Durante viaje: Actualizar ubicación**
```bash
# Cada 5 minutos, app envía ubicación GPS
{
  "latitud": 41.6801,
  "longitud": -0.8650,
  "velocidad_kmh": 85
}

# Sistema calcula ETA actualizado: 42 minutos
```

**6. Ana llega a sitio**
- Estado: EN_PROGRESO
- 15:05: Comienza trabajo

**7. Ana trabaja en reparación**
```bash
# Ana realiza empalme de fibra
# Pruebas: Todas las ONUs reportan conectividad

PATCH /api/v1/ordenes-trabajo/3
{
  "estado": "EN_PROGRESO",
  "notas": "Empalme de fibra completado. Todas las ONUs reconectadas. Pruebas satisfactorias."
}

# 15:12: Trabajo completado
```

**8. Sistema actualiza automáticamente**
```json
{
  "orden": {
    "estado": "COMPLETADA",
    "fecha_cierre": "2026-05-10T15:12:00"
  },
  "incidencia": {
    "estado": "RESUELTA",
    "ttrMinutos": 42,
    "sla_cumplido": true
  },
  "kpis": {
    "clientes_recuperados": 180,
    "mttr_promedio": 42,
    "sla_cumplimiento": "100%"
  }
}
```

---

### Escenario 2: Problema de 5G - Análisis Detallado

#### Situación
Celda 5G en Teruel envía alerta de baja cobertura. 320 clientes móviles afectados.

**Database Records:**
```sql
-- Equipo afectado
SELECT * FROM equipos WHERE id = 4;
/*
id: 4,
nombre: RRU-5G-TER-003,
tipo: RRU,
modelo: Huawei AAU5619,
estado: FALLO,
zona: Teruel
*/

-- Técnico disponible con skill 5G
SELECT * FROM tecnicos WHERE skills LIKE '%5G%' AND disponible = TRUE
ORDER BY zona = 'Teruel' DESC;
/*
1. Laura Sánchez (Teruel) ✅
2. Ana García (Zaragoza Sur) - Si Laura no disponible
3. Miguel Fernández (Huesca)
*/
```

#### Timeline Completo

| Tiempo | Acción | Base de Datos | API Call |
|--------|--------|---------------|----------|
| 10:30 | Alerta de sensor | equipos.estado = FALLO | - |
| 10:32 | INC creada | incidencias INSERT | POST /incidencias |
| 10:33 | Orden creada | ordenes_trabajo INSERT | POST /ordenes-trabajo |
| 10:35 | Laura en camino | tecnicos.disponible = FALSE | PATCH /ordenes-trabajo/estado |
| 10:48 | Laura llega | ordenes_trabajo.estado = EN_PROGRESO | PATCH /ordenes-trabajo/estado |
| 10:55 | Diagnóstico | ordenes_trabajo.notas UPDATE | PATCH /ordenes-trabajo/notas |
| 11:05 | Reparación | ordenes_trabajo.estado = COMPLETADA | PATCH /ordenes-trabajo/estado |
| 11:05 | Sistema actualiza | incidencias.ttrMinutos = 35 | (Automático) |

#### Cambios en la Base de Datos

**BEFORE (Situación Inicial):**
```sql
-- tecnicos
id: 4, nombre: Laura, disponible: TRUE

-- equipos  
id: 4, nombre: RRU-5G-TER-003, estado: OPERATIVO

-- incidencias
(vacío - no hay incidencias)

-- ordenes_trabajo
(vacío)
```

**AFTER (Situación Final):**
```sql
-- tecnicos
id: 4, nombre: Laura, disponible: TRUE
(se actualiza de nuevo cuando completa)

-- equipos
id: 4, nombre: RRU-5G-TER-003, estado: OPERATIVO
(se actualiza cuando se repara)

-- incidencias
id: 4, codigo: INC-20260512-004, estado: RESUELTA,
ttrMinutos: 35, clientesAfectados: 320

-- ordenes_trabajo
id: 4, codigo: WO-2026-08850, tecnico_id: 4,
estado: COMPLETADA, fecha_cierre: 2026-05-12T11:05:00
```

---

## 💾 Base de Datos

### Estructura Completa

#### Tabla: `tecnicos`

```sql
CREATE TABLE tecnicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    skills VARCHAR(255),
    zona VARCHAR(100),
    disponible BOOLEAN DEFAULT TRUE,
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Datos de Ejemplo:**
```sql
INSERT INTO tecnicos VALUES
(1, 'Carlos', 'Martínez López', 'carlos.martinez@smartnoc.es', '612345678', 
 'FTTH_L3,OPGW', 'Zaragoza Norte', TRUE, 41.6601, -0.8774, '2026-01-01 08:00:00'),

(2, 'Ana', 'García Ruiz', 'ana.garcia@smartnoc.es', '623456789', 
 'FTTH_L3,5G', 'Zaragoza Sur', TRUE, 41.6350, -0.8891, '2026-01-01 08:00:00'),

(3, 'Miguel', 'Fernández Díaz', 'miguel.fernandez@smartnoc.es', '634567890', 
 '5G,RRU,OPGW', 'Huesca', FALSE, 42.1358, -0.4082, '2026-01-01 08:00:00'),

(4, 'Laura', 'Sánchez Torres', 'laura.sanchez@smartnoc.es', '645678901', 
 'FTTH_L3,SWITCH', 'Teruel', TRUE, 40.3440, -1.1067, '2026-01-01 08:00:00');
```

---

#### Tabla: `equipos`

```sql
CREATE TABLE equipos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo ENUM('OLT','BTS_5G','ONU','ROUTER','SWITCH','RRU'),
    modelo VARCHAR(100),
    direccion_ip VARCHAR(45),
    site_id VARCHAR(50),
    zona VARCHAR(100),
    estado ENUM('OPERATIVO','DEGRADADO','FALLO','MANTENIMIENTO') DEFAULT 'OPERATIVO',
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    instalado_en DATE,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Datos de Ejemplo:**
```sql
INSERT INTO equipos VALUES
(1, 'OLT-ZGZ-042', 'OLT', 'Huawei MA5800-X7', '10.100.42.1', 
 'ZGZ-042', 'Zaragoza Norte', 'DEGRADADO', 41.6560, -0.8800, '2024-03-15', NOW()),

(2, 'BTS-5G-ZGZ-011', 'BTS_5G', 'Ericsson AIR 6449', '10.200.11.1', 
 'ZGZ-011', 'Zaragoza Centro', 'OPERATIVO', 41.6560, -0.8773, '2024-06-01', NOW()),

(3, 'OLT-HUE-007', 'OLT', 'Nokia 7360 FX', '10.100.17.1', 
 'HUE-007', 'Huesca', 'OPERATIVO', 42.1400, -0.4100, '2024-05-10', NOW()),

(4, 'RRU-5G-TER-003', 'RRU', 'Huawei AAU5619', '10.200.53.1', 
 'TER-003', 'Teruel', 'FALLO', 40.3450, -1.1050, '2024-07-20', NOW());
```

---

#### Tabla: `incidencias`

```sql
CREATE TABLE incidencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    severidad ENUM('P1','P2','P3','P4') DEFAULT 'P3',
    tipo VARCHAR(100),
    estado ENUM('ABIERTA','EN_PROGRESO','RESUELTA','CERRADA') DEFAULT 'ABIERTA',
    clientes_afect INT DEFAULT 0,
    equipo_id BIGINT,
    fecha_apertura DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME NULL,
    ttd_minutos INT,
    ttr_minutos INT,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id)
);
```

**Datos de Ejemplo:**
```sql
INSERT INTO incidencias VALUES
(1, 'INC-20260512-001', 'Degradación OLT Zaragoza Norte', 
 'Potencia óptica por debajo del umbral en 3 tarjetas GPON', 
 'P1', 'FTTH_OLT_DEGRADATION', 'EN_PROGRESO', 1240, 1, 
 '2026-05-12 09:15:00', NULL, 4, NULL, NOW()),

(2, 'INC-20260512-002', 'Caída celda 5G Teruel', 
 'NR Cell Down – sin cobertura 5G en zona industrial', 
 'P2', '5G_CELL_DOWN', 'ABIERTA', 320, 4, 
 '2026-05-12 10:30:00', NULL, 8, NULL, NOW()),

(3, 'INC-20260510-003', 'Fallo ONU masivo Huesca', 
 '60 ONUs sin servicio tras corte de fibra', 
 'P1', 'FTTH_MASS_ONU_FAIL', 'RESUELTA', 180, 3, 
 '2026-05-10 14:30:00', '2026-05-10 15:12:00', 12, 42, NOW());
```

---

#### Tabla: `ordenes_trabajo`

```sql
CREATE TABLE ordenes_trabajo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    incidencia_id BIGINT NOT NULL,
    tecnico_id BIGINT,
    estado ENUM('PENDIENTE','DESPACHADA','EN_CAMINO','EN_PROGRESO','COMPLETADA','CANCELADA') DEFAULT 'PENDIENTE',
    prioridad ENUM('P1','P2','P3','P4') DEFAULT 'P3',
    sla_deadline DATETIME,
    eta_minutos INT,
    notas TEXT,
    fecha_despacho DATETIME NULL,
    fecha_cierre DATETIME NULL,
    creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incidencia_id) REFERENCES incidencias(id),
    FOREIGN KEY (tecnico_id) REFERENCES tecnicos(id)
);
```

**Datos de Ejemplo:**
```sql
INSERT INTO ordenes_trabajo VALUES
(1, 'WO-2026-08847', 1, 1, 'EN_PROGRESO', 'P1', 
 '2026-05-12 09:45:00', 12, 'Equipo crítico. Prioridad máxima.', 
 '2026-05-12 09:17:00', NULL, NOW()),

(2, 'WO-2026-08848', 2, 4, 'PENDIENTE', 'P2', 
 '2026-05-12 11:30:00', 30, 'Enviar técnico a Teruel urgente', 
 NULL, NULL, NOW()),

(3, 'WO-2026-08849', 3, 2, 'COMPLETADA', 'P1', 
 '2026-05-10 15:00:00', 45, 'Empalme de fibra. Éxito total.', 
 '2026-05-10 14:35:00', '2026-05-10 15:12:00', NOW());
```

---

## 📈 Consultas Comunes

### 1. Obtener todas las incidencias activas

```sql
SELECT i.codigo, i.titulo, i.severidad, i.estado, 
       i.clientes_afect, e.nombre AS equipo, 
       TIMESTAMPDIFF(MINUTE, i.fecha_apertura, NOW()) AS minutos_abierta
FROM incidencias i
LEFT JOIN equipos e ON i.equipo_id = e.id
WHERE i.estado IN ('ABIERTA', 'EN_PROGRESO')
ORDER BY i.severidad, i.fecha_apertura;

Resultado:
┌──────────────────┬────────────────────────────────┬───────────┬────────────┬────────────────┬────────────────┬──────────────────┐
│ codigo           │ titulo                         │ severidad │ estado     │ clientes_afect │ equipo         │ minutos_abierta  │
├──────────────────┼────────────────────────────────┼───────────┼────────────┼────────────────┼────────────────┼──────────────────┤
│ INC-20260512-001 │ Degradación OLT Zaragoza Norte │ P1        │ EN_PROGRESO│ 1240           │ OLT-ZGZ-042    │ 32               │
│ INC-20260512-002 │ Caída celda 5G Teruel          │ P2        │ ABIERTA    │ 320            │ RRU-5G-TER-003 │ 22               │
└──────────────────┴────────────────────────────────┴───────────┴────────────┴────────────────┴────────────────┴──────────────────┘
```

---

### 2. MTTR (Mean Time To Repair) promedio

```sql
SELECT 
    ROUND(AVG(TIMESTAMPDIFF(MINUTE, i.fecha_apertura, i.fecha_cierre))) AS mttr_minutos,
    COUNT(*) AS incidencias_resueltas,
    i.severidad
FROM incidencias i
WHERE i.estado = 'RESUELTA' 
  AND i.fecha_cierre >= DATE_SUB(NOW(), INTERVAL 7 DAY)
GROUP BY i.severidad
ORDER BY i.severidad;

Resultado:
┌──────────────────┬───────────────────────┬───────────┐
│ mttr_minutos     │ incidencias_resueltas │ severidad │
├──────────────────┼───────────────────────┼───────────┤
│ 32               │ 1                     │ P1        │
│ 45               │ 1                     │ P2        │
│ 120              │ 1                     │ P3        │
└──────────────────┴───────────────────────┴───────────┘
```

---

### 3. Técnicos disponibles por zona y skill

```sql
SELECT 
    t.nombre, t.apellidos, t.email, 
    t.skills, t.zona, 
    COUNT(ot.id) AS ordenes_activas
FROM tecnicos t
LEFT JOIN ordenes_trabajo ot ON t.id = ot.tecnico_id 
                             AND ot.estado NOT IN ('COMPLETADA', 'CANCELADA')
WHERE t.disponible = TRUE
  AND (t.skills LIKE '%FTTH_L3%' OR t.skills LIKE '%5G%')
GROUP BY t.id
HAVING ordenes_activas < 3;

Resultado:
┌────────┬──────────────┬────────────────────────┬──────────────┬─────────────────┬─────────────────┐
│ nombre │ apellidos    │ email                  │ skills       │ zona            │ ordenes_activas │
├────────┼──────────────┼────────────────────────┼──────────────┼─────────────────┼─────────────────┤
│ Carlos │ Martínez López│ carlos.martinez@smartnoc│ FTTH_L3,OPGW │ Zaragoza Norte  │ 1               │
│ Ana    │ García Ruiz  │ ana.garcia@smartnoc.es │ FTTH_L3,5G   │ Zaragoza Sur    │ 0               │
│ Laura  │ Sánchez Torres│ laura.sanchez@smartnoc│ FTTH_L3,SWITCH│ Teruel         │ 0               │
└────────┴──────────────┴────────────────────────┴──────────────┴─────────────────┴─────────────────┘
```

---

### 4. Equipos con problemas en últimas 24 horas

```sql
SELECT 
    e.nombre, e.tipo, e.estado, e.zona,
    COUNT(i.id) AS incidencias_recientes,
    MAX(i.fecha_apertura) AS ultima_incidencia
FROM equipos e
LEFT JOIN incidencias i ON e.id = i.equipo_id 
                       AND i.fecha_apertura >= DATE_SUB(NOW(), INTERVAL 1 DAY)
WHERE e.estado != 'OPERATIVO'
   OR COUNT(i.id) > 0
GROUP BY e.id
ORDER BY incidencias_recientes DESC;

Resultado:
┌────────────────┬─────────┬────────────┬──────────────────┬──────────────────────┬────────────────────────┐
│ nombre         │ tipo    │ estado     │ zona             │ incidencias_recientes│ ultima_incidencia      │
├────────────────┼─────────┼────────────┼──────────────────┼──────────────────────┼────────────────────────┤
│ OLT-ZGZ-042    │ OLT     │ DEGRADADO  │ Zaragoza Norte   │ 1                    │ 2026-05-12 09:15:00    │
│ RRU-5G-TER-003 │ RRU     │ FALLO      │ Teruel           │ 1                    │ 2026-05-12 10:30:00    │
│ OLT-HUE-007    │ OLT     │ OPERATIVO  │ Huesca           │ 0                    │ NULL                   │
└────────────────┴─────────┴────────────┴──────────────────┴──────────────────────┴────────────────────────┘
```

---

### 5. Cumplimiento de SLAs

```sql
SELECT 
    ROUND(100.0 * 
        SUM(CASE WHEN ot.fecha_cierre <= ot.sla_deadline THEN 1 ELSE 0 END) / 
        COUNT(*), 2) AS sla_cumplimiento_porcentaje,
    i.severidad,
    COUNT(*) AS total_ordenes
FROM ordenes_trabajo ot
JOIN incidencias i ON ot.incidencia_id = i.id
WHERE ot.estado = 'COMPLETADA'
  AND ot.fecha_cierre >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY i.severidad
ORDER BY i.severidad;

Resultado:
┌────────────────────────────┬───────────┬──────────────┐
│ sla_cumplimiento_porcentaje│ severidad │ total_ordenes│
├────────────────────────────┼───────────┼──────────────┤
│ 100.00                     │ P1        │ 15           │
│ 98.50                      │ P2        │ 33           │
│ 96.00                      │ P3        │ 50           │
│ 99.00                      │ P4        │ 8            │
└────────────────────────────┴───────────┴──────────────┘
```

---

### 6. Performance por técnico

```sql
SELECT 
    t.nombre, t.apellidos, t.email,
    COUNT(ot.id) AS total_ordenes,
    AVG(TIMESTAMPDIFF(MINUTE, i.fecha_apertura, ot.fecha_cierre)) AS mttr_promedio_minutos,
    ROUND(100.0 * 
        SUM(CASE WHEN ot.fecha_cierre <= ot.sla_deadline THEN 1 ELSE 0 END) / 
        COUNT(*), 2) AS sla_cumplimiento_porcentaje
FROM tecnicos t
LEFT JOIN ordenes_trabajo ot ON t.id = ot.tecnico_id AND ot.estado = 'COMPLETADA'
LEFT JOIN incidencias i ON ot.incidencia_id = i.id
WHERE ot.fecha_cierre >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY t.id
ORDER BY sla_cumplimiento_porcentaje DESC;

Resultado:
┌────────┬────────────────┬────────────────────────┬───────────────┬─────────────────┬──────────────────────────┐
│ nombre │ apellidos      │ email                  │ total_ordenes │ mttr_promedio   │ sla_cumplimiento         │
├────────┼────────────────┼────────────────────────┼───────────────┼─────────────────┼──────────────────────────┤
│ Carlos │ Martínez López │ carlos.martinez@smartnoc│ 12            │ 28.5            │ 100.00                   │
│ Laura  │ Sánchez Torres │ laura.sanchez@smartnoc │ 10            │ 35.2            │ 100.00                   │
│ Ana    │ García Ruiz    │ ana.garcia@smartnoc.es │ 9             │ 42.1            │ 98.00                    │
│ Miguel │ Fernández Díaz │ miguel.fernandez@smartnoc│ 5            │ 55.0            │ 95.00                    │
└────────┴────────────────┴────────────────────────┴───────────────┴─────────────────┴──────────────────────────┘
```

---

## 🔧 Troubleshooting

### Problema 1: No aparecen técnicos disponibles

**Síntoma:** Sistema no asigna técnico a una orden

**Posibles causas:**

```sql
-- 1. Verificar técnicos disponibles
SELECT * FROM tecnicos WHERE disponible = TRUE;

-- 2. Verificar que tengan skills requeridos
SELECT * FROM tecnicos 
WHERE disponible = TRUE 
  AND skills LIKE '%FTTH_L3%';

-- 3. Verificar que no tienen demasiadas órdenes
SELECT t.id, t.nombre, COUNT(ot.id) AS activas
FROM tecnicos t
LEFT JOIN ordenes_trabajo ot ON t.id = ot.tecnico_id
WHERE ot.estado NOT IN ('COMPLETADA', 'CANCELADA')
GROUP BY t.id;

-- 4. Si ninguno disponible, buscar próximo técnico
SELECT * FROM tecnicos 
ORDER BY disponible DESC, 
         (SELECT COUNT(*) FROM ordenes_trabajo 
          WHERE tecnico_id = tecnicos.id 
          AND estado NOT IN ('COMPLETADA', 'CANCELADA')) ASC
LIMIT 1;
```

**Solución:**
1. Marcar técnico como disponible: `UPDATE tecnicos SET disponible = TRUE WHERE id = 3;`
2. Completar órdenes pendientes
3. Agregar más técnicos con el skill requerido

---

### Problema 2: Incidencia no cambia de estado

**Síntoma:** PATCH al endpoint retorna error 404

**Posible causa:**
```json
Request: PATCH /api/v1/incidencias/999/estado

Response:
{
  "error": "Incidencia no encontrada",
  "status": 404
}
```

**Verificar:**
```sql
SELECT * FROM incidencias WHERE id = 999;
-- Si está vacío, la incidencia no existe

-- Obtener ID correcto
SELECT id, codigo FROM incidencias WHERE codigo LIKE 'INC-20260512%';
```

---

### Problema 3: MTTR muy alto

**Síntoma:** Promedios de TTR > 90 minutos

**Investigar:**
```sql
-- Top 5 incidencias con TTR más alto
SELECT codigo, titulo, severidad, 
       TIMESTAMPDIFF(MINUTE, fecha_apertura, fecha_cierre) AS ttr_minutos,
       clientes_afect
FROM incidencias
WHERE estado = 'RESUELTA'
ORDER BY ttr_minutos DESC
LIMIT 5;

-- Calcular por tipo de problema
SELECT tipo, 
       AVG(TIMESTAMPDIFF(MINUTE, fecha_apertura, fecha_cierre)) AS ttr_promedio,
       COUNT(*) AS casos
FROM incidencias
WHERE estado = 'RESUELTA'
GROUP BY tipo
ORDER BY ttr_promedio DESC;

-- Calcular por técnico
SELECT t.nombre, 
       AVG(TIMESTAMPDIFF(MINUTE, i.fecha_apertura, i.fecha_cierre)) AS ttr_promedio
FROM ordenes_trabajo ot
JOIN incidencias i ON ot.incidencia_id = i.id
JOIN tecnicos t ON ot.tecnico_id = t.id
WHERE i.estado = 'RESUELTA'
GROUP BY ot.tecnico_id
ORDER BY ttr_promedio DESC;
```

**Acciones correctivas:**
- Proporcionar capacitación a técnicos lentos
- Mejorar diagnosis remota
- Aumentar stock de repuestos

---

### Problema 4: Errores de conexión a BD

**Síntoma:** `Timeout connecting to database` al iniciar backend

**Verificar conexión:**
```bash
# Probar conectividad TCP
ps aux | grep mysql
# O: netstat -an | grep 3306

# Conectar manualmente
mysql -h localhost -u root -p
# Contraseña: root

# Una vez dentro, verificar BD
SHOW DATABASES;
USE smart_noc;
SHOW TABLES;
```

**Si la BD no existe, crearla:**
```bash
mysql -h localhost -u root -p < schema.sql
# Contraseña: root
```

---

### Problema 5: API devuelve JSON mal formado

**Síntoma:** 
```
Unexpected token H in JSON at position 0
```

**Causa probable:** API retorna HTML en lugar de JSON

**Verificar:**
```bash
# Test directo
curl http://localhost:8080/api/v1/incidencias

# Si muestra HTML (error 404 o 500), verificar:
# 1. ¿Está el backend corriendo?
ps aux | grep java

# 2. ¿Puerto correcto?
netstat -an | grep 8080

# 3. ¿Logs del backend?
# Ver application.properties
tail -f target/logs/app.log
```

---

## 📞 Contacto y Soporte

**Para preguntas sobre:**
- 🔧 **Funcionalidad técnica**: Equipo de Desarrollo
- 📊 **Datos y reportes**: Equipo de Operaciones
- 🚀 **Deployment**: DevOps
- 📚 **Documentación**: Equipo de Documentación

---

**FIN DEL MANUAL FUNCIONAL**

*Última actualización: 12 de mayo de 2026*  
*Versión: 1.0*  
*Próxima revisión: 12 de noviembre de 2026*
