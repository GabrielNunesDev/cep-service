package com.cepservice.service;

import com.cepservice.dto.CepResponseDTO;

public interface CepService {

    CepResponseDTO consultarCep(String cep);
}
