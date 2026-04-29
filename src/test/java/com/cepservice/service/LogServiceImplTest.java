package com.cepservice.service;

import com.cepservice.model.CepLog;
import com.cepservice.repository.CepLogRepository;
import com.cepservice.service.impl.LogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do LogServiceImpl")
class LogServiceImplTest {

    @Mock
    private CepLogRepository cepLogRepository;

    @InjectMocks
    private LogServiceImpl logService;

    private CepLog logValido;

    @BeforeEach
    void setUp() {
        logValido = CepLog.builder()
                .id(1L)
                .cep("01001000")
                .logradouro("Praça da Sé")
                .bairro("Sé")
                .localidade("São Paulo")
                .uf("SP")
                .consultadoEm(LocalDateTime.now())
                .sucesso(true)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os logs")
    void listarLogs_deveRetornarTodosOsLogs() {
        when(cepLogRepository.findAll()).thenReturn(List.of(logValido));

        List<CepLog> logs = logService.listarLogs();

        assertNotNull(logs);
        assertEquals(1, logs.size());
        verify(cepLogRepository).findAll();
    }

    @Test
    @DisplayName("Deve listar logs filtrados por CEP")
    void listarLogsPorCep_deveRetornarLogsDosCep() {
        when(cepLogRepository.findByCepOrderByConsultadoEmDesc("01001000"))
                .thenReturn(List.of(logValido));

        List<CepLog> logs = logService.listarLogsPorCep("01001000");

        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals("01001000", logs.get(0).getCep());
    }

    @Test
    @DisplayName("Deve remover traço do CEP antes de buscar logs")
    void listarLogsPorCep_deveRemoverTraco_antesDeBuscar() {
        when(cepLogRepository.findByCepOrderByConsultadoEmDesc("01001000"))
                .thenReturn(List.of(logValido));

        logService.listarLogsPorCep("01001-000");

        verify(cepLogRepository).findByCepOrderByConsultadoEmDesc("01001000");
    }
}
