package com.cepservice.client;

import com.cepservice.client.impl.CepClientImpl;
import com.cepservice.dto.CepResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@DisplayName("Testes do CepClientImpl")
class CepClientImplTest {

    private static final String CEP_API_URL = "http://localhost:8081";

    private MockRestServiceServer server;
    private CepClientImpl cepClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        cepClient = new CepClientImpl(builder, CEP_API_URL);
    }

    @Test
    @DisplayName("Deve retornar dados do endereço quando API responde com sucesso")
    void buscarCep_deveRetornarEndereco_quandoApiRespondeSucesso() {
        server.expect(requestTo(CEP_API_URL + "/cep/01001000"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {"cep":"01001-000","logradouro":"Praça da Sé","bairro":"Sé","localidade":"São Paulo","uf":"SP"}
                        """, MediaType.APPLICATION_JSON));

        CepResponseDTO resultado = cepClient.buscarCep("01001000");

        assertNotNull(resultado);
        assertEquals("01001-000", resultado.cep());
        assertEquals("Praça da Sé", resultado.logradouro());
        assertEquals("São Paulo", resultado.localidade());
        assertNull(resultado.erro());
    }

    @Test
    @DisplayName("Deve retornar erro quando API retorna 404")
    void buscarCep_deveRetornarErro_quandoApiRetorna404() {
        server.expect(requestTo(CEP_API_URL + "/cep/99999999"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        CepResponseDTO resultado = cepClient.buscarCep("99999999");

        assertNotNull(resultado);
        assertEquals("CEP não encontrado", resultado.erro());
    }

    @Test
    @DisplayName("Deve retornar erro quando API está indisponível")
    void buscarCep_deveRetornarErro_quandoApiIndisponivel() {
        server.expect(requestTo(CEP_API_URL + "/cep/01001000"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new IOException("Connection refused")));

        CepResponseDTO resultado = cepClient.buscarCep("01001000");

        assertNotNull(resultado);
        assertNotNull(resultado.erro());
        assertTrue(resultado.erro().contains("Erro ao consultar CEP"));
    }

    @Test
    @DisplayName("Deve montar a URL corretamente com o CEP")
    void buscarCep_deveMontarUrlCorreta() {
        server.expect(requestTo(CEP_API_URL + "/cep/01001000"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {"cep":"01001-000"}
                        """, MediaType.APPLICATION_JSON));

        cepClient.buscarCep("01001000");

        server.verify();
    }
}
