package com.cepservice.service.impl;

import com.cepservice.model.CepLog;
import com.cepservice.repository.CepLogRepository;
import com.cepservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final CepLogRepository cepLogRepository;

    @Override
    public List<CepLog> listarLogs() {
        return cepLogRepository.findAll();
    }

    @Override
    public List<CepLog> listarLogsPorCep(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        return cepLogRepository.findByCepOrderByConsultadoEmDesc(cepLimpo);
    }
}
