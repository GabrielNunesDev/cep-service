package com.cepservice.controller;

import com.cepservice.dto.CepResponseDTO;
import com.cepservice.service.CepService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CepController")
class CepControllerTest {

    @Mock
    private CepService cepService;

    @InjectMocks
    private CepController cepController;

    private MockMvc mockMvc;

    private CepResponseDTO responseValida;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cepController).build();

        responseValida = CepResponseDTO.builder()
                .cep("01001-000")
                .logradouro("Praça da Sé")
                .complemento("lado ímpar")
                .bairro("Sé")
                .localidade("São Paulo")
                .uf("SP")
                .ibge("3550308")
                .ddd("11")
                .build();
    }

    @Test
    @DisplayName("Deve retornar 200 com dados do endereço quando CEP é válido")
    void consultarCep_deveRetornar200_quandoCepValido() throws Exception {
        when(cepService.consultarCep("01001000")).thenReturn(responseValida);

        mockMvc.perform(get("/api/v1/cep/01001000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01001-000"))
                .andExpect(jsonPath("$.logradouro").value("Praça da Sé"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"))
                .andExpect(jsonPath("$.uf").value("SP"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando CEP não é encontrado")
    void consultarCep_deveRetornar404_quandoCepNaoEncontrado() throws Exception {
        CepResponseDTO responseErro = CepResponseDTO.builder()
                .erro("CEP não encontrado")
                .build();

        when(cepService.consultarCep("99999999")).thenReturn(responseErro);

        mockMvc.perform(get("/api/v1/cep/99999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
