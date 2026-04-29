package com.cepservice.controller;

import com.cepservice.model.CepLog;
import com.cepservice.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do LogController")
class LogControllerTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private LogController logController;

    private MockMvc mockMvc;

    private CepLog logValido;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(logController).build();

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
    @DisplayName("Deve retornar lista de todos os logs")
    void listarLogs_deveRetornar200_comListaDeLogs() throws Exception {
        when(logService.listarLogs()).thenReturn(List.of(logValido));

        mockMvc.perform(get("/api/v1/logs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cep").value("01001000"))
                .andExpect(jsonPath("$[0].logradouro").value("Praça da Sé"))
                .andExpect(jsonPath("$[0].sucesso").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista de logs filtrada por CEP")
    void listarLogsPorCep_deveRetornar200_comLogsDosCep() throws Exception {
        when(logService.listarLogsPorCep("01001000")).thenReturn(List.of(logValido));

        mockMvc.perform(get("/api/v1/logs/01001000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cep").value("01001000"))
                .andExpect(jsonPath("$[0].sucesso").value(true));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há logs")
    void listarLogs_deveRetornarListaVazia_quandoNaoHaLogs() throws Exception {
        when(logService.listarLogs()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/logs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
