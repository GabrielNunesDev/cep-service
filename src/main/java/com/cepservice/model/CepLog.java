package com.cepservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cep_logs")
public class CepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    private String cep;

    @Column(length = 200)
    private String logradouro;

    @Column(length = 200)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String localidade;

    @Column(length = 2)
    private String uf;

    @Column(length = 10)
    private String ibge;

    @Column(length = 3)
    private String ddd;

    @Column(nullable = false)
    private LocalDateTime consultadoEm;

    @Column(nullable = false)
    private Boolean sucesso;

    @Column(length = 100)
    private String mensagemErro;
}
