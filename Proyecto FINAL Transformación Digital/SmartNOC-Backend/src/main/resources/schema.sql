-- ================================================================
-- SmartNOC - Script de Base de Datos MySQL
-- Ejecutar en MySQL Workbench o línea de comandos
-- ================================================================

DROP DATABASE IF EXISTS smart_noc;
CREATE DATABASE IF NOT EXISTS smart_noc
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE smart_noc;

-- ── TABLA: tecnicos ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS tecnicos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellidos   VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    telefono    VARCHAR(20),
    skills      VARCHAR(255) COMMENT 'Ej: FTTH_L3,5G,OPGW separados por coma',
    zona        VARCHAR(100) COMMENT 'Zona geográfica asignada',
    disponible  BOOLEAN DEFAULT TRUE,
    latitud     DECIMAL(10,7),
    longitud    DECIMAL(10,7),
    creado_en   DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ── TABLA: equipos ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS equipos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    tipo            ENUM('OLT','BTS_5G','ONU','ROUTER','SWITCH','RRU') NOT NULL,
    modelo          VARCHAR(100),
    direccion_ip    VARCHAR(45),
    site_id         VARCHAR(50) COMMENT 'Identificador del emplazamiento',
    zona            VARCHAR(100),
    estado          ENUM('OPERATIVO','DEGRADADO','FALLO','MANTENIMIENTO') DEFAULT 'OPERATIVO',
    latitud         DECIMAL(10,7),
    longitud        DECIMAL(10,7),
    instalado_en    DATE,
    creado_en       DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ── TABLA: incidencias ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS incidencias (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo          VARCHAR(30) NOT NULL UNIQUE COMMENT 'Ej: INC-20260512-00847',
    titulo          VARCHAR(255) NOT NULL,
    descripcion     TEXT,
    severidad       ENUM('P1','P2','P3','P4') NOT NULL DEFAULT 'P3',
    tipo            VARCHAR(100) COMMENT 'Ej: FTTH_OLT_DEGRADATION, 5G_CELL_DOWN',
    estado          ENUM('ABIERTA','EN_PROGRESO','RESUELTA','CERRADA') DEFAULT 'ABIERTA',
    clientes_afect  INT DEFAULT 0,
    equipo_id       BIGINT,
    fecha_apertura  DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre    DATETIME NULL,
    ttd_minutos     INT COMMENT 'Time To Detect',
    ttr_minutos     INT COMMENT 'Time To Repair (MTTR)',
    creado_en       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipo_id) REFERENCES equipos(id) ON DELETE SET NULL
);

-- ── TABLA: ordenes_trabajo ───────────────────────────────────────
CREATE TABLE IF NOT EXISTS ordenes_trabajo (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo          VARCHAR(30) NOT NULL UNIQUE COMMENT 'Ej: WO-2026-08847',
    incidencia_id   BIGINT NOT NULL,
    tecnico_id      BIGINT,
    estado          ENUM('PENDIENTE','DESPACHADA','EN_CAMINO','EN_PROGRESO','COMPLETADA','CANCELADA') DEFAULT 'PENDIENTE',
    prioridad       ENUM('P1','P2','P3','P4') DEFAULT 'P3',
    sla_deadline    DATETIME,
    eta_minutos     INT COMMENT 'Tiempo estimado de llegada',
    notas           TEXT,
    fecha_despacho  DATETIME NULL,
    fecha_cierre    DATETIME NULL,
    creado_en       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incidencia_id) REFERENCES incidencias(id),
    FOREIGN KEY (tecnico_id) REFERENCES tecnicos(id) ON DELETE SET NULL
);

-- ── TABLA: facturas ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS facturas (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura      VARCHAR(50) NOT NULL UNIQUE,
    importe             DECIMAL(10,2) NOT NULL,
    cliente             VARCHAR(150),
    fecha_emision       DATETIME DEFAULT CURRENT_TIMESTAMP,
    enviado_ticketbai   BOOLEAN DEFAULT FALSE,
    firma_ticketbai     VARCHAR(255),
    qr_url              VARCHAR(255)
);

-- ── DATOS DE PRUEBA ──────────────────────────────────────────────
INSERT INTO tecnicos (nombre, apellidos, email, telefono, skills, zona, disponible, latitud, longitud)
VALUES
('Carlos',  'Martínez López',  'carlos.martinez@smartnoc.es',  '612345678', 'FTTH_L3,OPGW',  'Zaragoza Norte', TRUE,  41.6601, -0.8774),
('Ana',     'García Ruiz',     'ana.garcia@smartnoc.es',       '623456789', 'FTTH_L3,5G',    'Zaragoza Sur',   TRUE,  41.6350, -0.8891),
('Miguel',  'Fernández Díaz',  'miguel.fernandez@smartnoc.es', '634567890', '5G,RRU,OPGW',   'Huesca',         FALSE, 42.1358, -0.4082),
('Laura',   'Sánchez Torres',  'laura.sanchez@smartnoc.es',    '645678901', 'FTTH_L3,SWITCH','Teruel',         TRUE,  40.3440, -1.1067);

INSERT INTO equipos (nombre, tipo, modelo, direccion_ip, site_id, zona, estado, latitud, longitud)
VALUES
('OLT-ZGZ-042',    'OLT',    'Huawei MA5800-X7', '10.100.42.1',  'ZGZ-042', 'Zaragoza Norte', 'DEGRADADO', 41.6560, -0.8800),
('BTS-5G-ZGZ-011', 'BTS_5G', 'Ericsson AIR 6449','10.200.11.1',  'ZGZ-011', 'Zaragoza Centro','OPERATIVO', 41.6560, -0.8773),
('OLT-HUE-007',    'OLT',    'Nokia 7360 FX',    '10.100.17.1',  'HUE-007', 'Huesca',         'OPERATIVO', 42.1400, -0.4100),
('RRU-5G-TER-003', 'RRU',    'Huawei AAU5619',   '10.200.53.1',  'TER-003', 'Teruel',         'FALLO',     40.3450, -1.1050);

INSERT INTO incidencias (codigo, titulo, descripcion, severidad, tipo, estado, clientes_afect, equipo_id, ttd_minutos)
VALUES
('INC-20260512-001','Degradación OLT Zaragoza Norte','Potencia óptica por debajo del umbral en 3 tarjetas GPON','P1','FTTH_OLT_DEGRADATION','EN_PROGRESO',1240,1,4),
('INC-20260512-002','Caída celda 5G Teruel','NR Cell Down – sin cobertura 5G en zona industrial','P2','5G_CELL_DOWN','ABIERTA',320,4,8),
('INC-20260510-003','Fallo ONU masivo Huesca','60 ONUs sin servicio tras corte de fibra','P1','FTTH_MASS_ONU_FAIL','RESUELTA',180,3,12);

INSERT INTO ordenes_trabajo (codigo, incidencia_id, tecnico_id, estado, prioridad, sla_deadline, eta_minutos, notas)
VALUES
('WO-2026-001',1,1,'EN_PROGRESO','P1', DATE_ADD(NOW(), INTERVAL 3 HOUR), 45,'Llevar tarjeta GPON repuesto modelo H901GPHF'),
('WO-2026-002',2,3,'DESPACHADA', 'P2', DATE_ADD(NOW(), INTERVAL 6 HOUR), 90,'Revisar alimentación RRU y conectores RF'),
('WO-2026-003',3,2,'COMPLETADA',  'P1', DATE_ADD(NOW(), INTERVAL -2 HOUR),30,'Empalme de fibra completado, servicio restaurado');

INSERT INTO facturas (numero_factura, importe, cliente, enviado_ticketbai)
VALUES
('FAC-2026-0001', 1250.50, 'Telecomunicaciones Zaragoza', FALSE),
('FAC-2026-0002', 450.00, 'Empresa de Servicios Huesca', FALSE);
