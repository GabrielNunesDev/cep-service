package com.cepservice.service;

import com.cepservice.client.CepClient;
import com.cepservice.dto.CepResponseDTO;
import com.cepservice.model.CepLog;
import com.cepservice.repository.CepLogRepository;
import com.cepservice.service.impl.CepServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CepServiceImpl")
class CepServiceImplTest {

    @Mock
    private CepClient cepClient;

    @Mock
    private CepLogRepository cepLogRepository;

    @InjectMocks
    private CepServiceImpl cepService;

    private CepResponseDTO responseValida;
    private CepLog logValido;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Deve retornar endereço quando CEP é válido")
    void consultarCep_deveRetornarEndereco_quandoCepValido() {
        when(cepClient.buscarCep("01001000")).thenReturn(responseValida);
        when(cepLogRepository.save(any(CepLog.class))).thenReturn(logValido);

        CepResponseDTO resultado = cepService.consultarCep("01001000");

        assertNotNull(resultado);
        assertEquals("01001-000", resultado.cep());
        assertEquals("Praça da Sé", resultado.logradouro());
        assertEquals("São Paulo", resultado.localidade());
        assertEquals("SP", resultado.uf());
        assertNull(resultado.erro());
    }

    @Test
    @DisplayName("Deve salvar log com sucesso=true quando CEP é encontrado")
    void consultarCep_deveSalvarLogSucesso_quandoCepEncontrado() {
        when(cepClient.buscarCep("01001000")).thenReturn(responseValida);
        when(cepLogRepository.save(any(CepLog.class))).thenReturn(logValido);

        cepService.consultarCep("01001000");

        ArgumentCaptor<CepLog> captor = ArgumentCaptor.forClass(CepLog.class);
        verify(cepLogRepository).save(captor.capture());

        CepLog logSalvo = captor.getValue();
        assertTrue(logSalvo.getSucesso());
        assertEquals("01001000", logSalvo.getCep());
        assertEquals("Praça da Sé", logSalvo.getLogradouro());
        assertNull(logSalvo.getMensagemErro());
    }

    @Test
    @DisplayName("Deve salvar log com sucesso=false quando CEP não encontrado")
    void consultarCep_deveSalvarLogFalha_quandoCepNaoEncontrado() {
        CepResponseDTO responseErro = CepResponseDTO.builder()
                .erro("CEP não encontrado")
                .build();

        when(cepClient.buscarCep("99999999")).thenReturn(responseErro);
        when(cepLogRepository.save(any(CepLog.class))).thenReturn(new CepLog());

        cepService.consultarCep("99999999");

        ArgumentCaptor<CepLog> captor = ArgumentCaptor.forClass(CepLog.class);
        verify(cepLogRepository).save(captor.capture());

        CepLog logSalvo = captor.getValue();
        assertFalse(logSalvo.getSucesso());
        assertEquals("CEP não encontrado", logSalvo.getMensagemErro());
    }

    @Test
    @DisplayName("Deve remover traço do CEP antes de consultar")
    void consultarCep_deveRemoverTraco_antesDeChamarClient() {
        when(cepClient.buscarCep("01001000")).thenReturn(responseValida);
        when(cepLogRepository.save(any(CepLog.class))).thenReturn(logValido);

        cepService.consultarCep("01001-000");

        verify(cepClient).buscarCep("01001000");
    }

    @Test
    @DisplayName("Deve chamar o client exatamente uma vez por consulta")
    void consultarCep_deveChamarClientUmaVez() {
        when(cepClient.buscarCep(anyString())).thenReturn(responseValida);
        when(cepLogRepository.save(any())).thenReturn(logValido);

        cepService.consultarCep("01001000");

        verify(cepClient, times(1)).buscarCep("01001000");
    }
}
