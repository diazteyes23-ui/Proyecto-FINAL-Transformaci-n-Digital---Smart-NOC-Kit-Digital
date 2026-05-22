package com.noc.smartnoc.service;

import com.noc.smartnoc.model.Incidencia;
import com.noc.smartnoc.model.Incidencia.Severidad;
import com.noc.smartnoc.repository.IncidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para IncidenciaService.
 * Usa Mockito para aislar el servicio del repositorio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de IncidenciaService")
class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @InjectMocks
    private IncidenciaService incidenciaService;

    private Incidencia incidenciaEjemplo;

    @BeforeEach
    void setUp() {
        incidenciaEjemplo = new Incidencia();
        incidenciaEjemplo.setCodigo("INC-20260512-00001");
        incidenciaEjemplo.setTitulo("Degradación OLT Zaragoza Norte");
        incidenciaEjemplo.setSeveridad(Severidad.P1);
        incidenciaEjemplo.setClientesAfectados(1240);
    }

    @Test
    @DisplayName("Debe retornar todas las incidencias")
    void testFindAll() {
        when(incidenciaRepository.findAll()).thenReturn(List.of(incidenciaEjemplo));

        List<Incidencia> resultado = incidenciaService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Degradación OLT Zaragoza Norte");
        verify(incidenciaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe crear incidencia con código autogenerado")
    void testCrearGeneraCodigo() {
        when(incidenciaRepository.count()).thenReturn(5L);
        when(incidenciaRepository.save(any(Incidencia.class))).thenAnswer(inv -> inv.getArgument(0));

        Incidencia nueva = new Incidencia();
        nueva.setTitulo("Nueva incidencia test");
        nueva.setSeveridad(Severidad.P2);

        Incidencia resultado = incidenciaService.crear(nueva);

        assertThat(resultado.getCodigo()).isNotNull();
        assertThat(resultado.getCodigo()).startsWith("INC-");
        assertThat(resultado.getFechaApertura()).isNotNull();
        verify(incidenciaRepository).save(any(Incidencia.class));
    }

    @Test
    @DisplayName("Debe retornar vacío si la incidencia no existe")
    void testFindByIdNoExiste() {
        when(incidenciaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Incidencia> resultado = incidenciaService.findById(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Al resolver incidencia debe calcular TTR")
    void testActualizarEstadoCalculaTtr() {
        incidenciaEjemplo.setFechaApertura(java.time.LocalDateTime.now().minusMinutes(90));
        when(incidenciaRepository.findById(1L)).thenReturn(Optional.of(incidenciaEjemplo));
        when(incidenciaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Incidencia> resultado = incidenciaService.actualizarEstado(1L, Incidencia.EstadoIncidencia.RESUELTA);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTtrMinutos()).isGreaterThan(0);
        assertThat(resultado.get().getFechaCierre()).isNotNull();
    }

    @Test
    @DisplayName("KPI: MTTR promedio debe retornar 0 si no hay datos")
    void testGetMttrPromedioSinDatos() {
        when(incidenciaRepository.calcularMttrPromedio()).thenReturn(null);

        Double mttr = incidenciaService.getMttrPromedio();

        assertThat(mttr).isEqualTo(0.0);
    }
}
