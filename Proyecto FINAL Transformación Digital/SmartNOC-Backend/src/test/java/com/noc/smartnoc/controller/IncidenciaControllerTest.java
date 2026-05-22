package com.noc.smartnoc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noc.smartnoc.model.Incidencia;
import com.noc.smartnoc.model.Incidencia.Severidad;
import com.noc.smartnoc.service.IncidenciaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para IncidenciaController.
 * Verifica los endpoints REST sin levantar el servidor completo.
 */
@WebMvcTest(IncidenciaController.class)
@DisplayName("Tests del IncidenciaController (REST)")
class IncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidenciaService incidenciaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/incidencias debe retornar 200 con lista")
    void testListarTodas() throws Exception {
        Incidencia inc = new Incidencia();
        inc.setCodigo("INC-20260512-00001");
        inc.setTitulo("OLT degradada");
        inc.setSeveridad(Severidad.P1);

        when(incidenciaService.findAll()).thenReturn(List.of(inc));

        mockMvc.perform(get("/api/v1/incidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("INC-20260512-00001"))
                .andExpect(jsonPath("$[0].titulo").value("OLT degradada"));
    }

    @Test
    @DisplayName("GET /api/v1/incidencias/999 debe retornar 404")
    void testObtenerPorIdNoExiste() throws Exception {
        when(incidenciaService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/incidencias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/incidencias debe crear y retornar 201")
    void testCrearIncidencia() throws Exception {
        Incidencia nueva = new Incidencia();
        nueva.setTitulo("Celda 5G caída");
        nueva.setSeveridad(Severidad.P2);
        nueva.setCodigo("INC-20260512-00002");

        when(incidenciaService.crear(any())).thenReturn(nueva);

        mockMvc.perform(post("/api/v1/incidencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Celda 5G caída"));
    }

    @Test
    @DisplayName("GET /api/v1/incidencias/kpis debe retornar métricas")
    void testKpis() throws Exception {
        when(incidenciaService.getMttrPromedio()).thenReturn(102.5);
        when(incidenciaService.getIncidenciasActivas()).thenReturn(3L);

        mockMvc.perform(get("/api/v1/incidencias/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mttrPromedioMinutos").value(102.5))
                .andExpect(jsonPath("$.incidenciasActivas").value(3));
    }
}
