package com.cepservice.client.impl;

import com.cepservice.client.CepClient;
import com.cepservice.dto.CepResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CepClientImpl implements CepClient {

    private final RestClient restClient;

    public CepClientImpl(RestClient.Builder builder,
                         @Value("${cep.api.url}") String cepApiUrl) {
        this.restClient = builder.baseUrl(cepApiUrl).build();
    }

    @Override
    public CepResponseDTO buscarCep(String cep) {
        try {
            return restClient.get()
                    .uri("/cep/{cep}", cep)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new RuntimeException("CEP não encontrado");
                    })
                    .body(CepResponseDTO.class);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().equals("CEP não encontrado")) {
                return CepResponseDTO.builder().erro("CEP não encontrado").build();
            }
            return CepResponseDTO.builder().erro("Erro ao consultar CEP: " + e.getMessage()).build();
        }
    }
}
