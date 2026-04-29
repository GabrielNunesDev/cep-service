package com.cepservice.controller;

import com.cepservice.dto.CepResponseDTO;
import com.cepservice.service.CepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cep")
@RequiredArgsConstructor
@Tag(name = "CEP", description = "API de consulta de CEP")
public class CepController {

    private final CepService cepService;

    @GetMapping("/{cep}")
    @Operation(summary = "Consultar CEP", description = "Busca informações de endereço pelo CEP informado")
    public ResponseEntity<CepResponseDTO> consultarCep(
            @Parameter(description = "CEP a ser consultado (somente números)", example = "01001000")
            @PathVariable String cep) {

        CepResponseDTO response = cepService.consultarCep(cep);

        if (response.erro() != null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
