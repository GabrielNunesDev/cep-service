package com.cepservice.controller;

import com.cepservice.model.CepLog;
import com.cepservice.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "Histórico de consultas de CEP")
public class LogController {

    private final LogService logService;

    @GetMapping
    @Operation(summary = "Listar todos os logs", description = "Retorna todos os logs de consultas realizadas")
    public ResponseEntity<List<CepLog>> listarLogs() {
        return ResponseEntity.ok(logService.listarLogs());
    }

    @GetMapping("/{cep}")
    @Operation(summary = "Listar logs por CEP", description = "Retorna os logs de consultas de um CEP específico")
    public ResponseEntity<List<CepLog>> listarLogsPorCep(
            @Parameter(description = "CEP para filtrar os logs", example = "01001000")
            @PathVariable String cep) {
        return ResponseEntity.ok(logService.listarLogsPorCep(cep));
    }
}
