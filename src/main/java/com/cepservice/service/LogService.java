package com.cepservice.service;

import com.cepservice.model.CepLog;

import java.util.List;

public interface LogService {

    List<CepLog> listarLogs();

    List<CepLog> listarLogsPorCep(String cep);
}
