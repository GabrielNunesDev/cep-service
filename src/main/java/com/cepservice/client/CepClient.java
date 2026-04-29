package com.cepservice.client;


import com.cepservice.dto.CepResponseDTO;

public interface CepClient {

    CepResponseDTO buscarCep(String cep);
}
