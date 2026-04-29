package com.cepservice.mapper;

import com.cepservice.dto.CepResponseDTO;
import com.cepservice.model.CepLog;

import java.time.LocalDateTime;

public class CepLogMapper {

    private CepLogMapper() {}

    public static CepLog toLog(String cep, CepResponseDTO response) {
        boolean sucesso = response != null && response.erro() == null;
        return CepLog.builder()
                .cep(cep)
                .logradouro(sucesso ? response.logradouro() : null)
                .complemento(sucesso ? response.complemento() : null)
                .bairro(sucesso ? response.bairro() : null)
                .localidade(sucesso ? response.localidade() : null)
                .uf(sucesso ? response.uf() : null)
                .ibge(sucesso ? response.ibge() : null)
                .ddd(sucesso ? response.ddd() : null)
                .consultadoEm(LocalDateTime.now())
                .sucesso(sucesso)
                .mensagemErro(sucesso ? null : (response != null ? response.erro() : "Resposta nula da API"))
                .build();
    }
}
