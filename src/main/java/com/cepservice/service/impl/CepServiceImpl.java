package com.cepservice.service.impl;

import com.cepservice.client.CepClient;
import com.cepservice.dto.CepResponseDTO;
import com.cepservice.mapper.CepLogMapper;
import com.cepservice.repository.CepLogRepository;
import com.cepservice.service.CepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CepServiceImpl implements CepService {

    private final CepClient cepClient;
    private final CepLogRepository cepLogRepository;

    @Override
    public CepResponseDTO consultarCep(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        log.info("Consultando CEP: {}", cepLimpo);

        CepResponseDTO response = cepClient.buscarCep(cepLimpo);

        var logEntry = CepLogMapper.toLog(cepLimpo, response);
        cepLogRepository.save(logEntry);
        log.info("Log salvo para CEP: {} | Sucesso: {}", cepLimpo, logEntry.getSucesso());

        return response;
    }
}
