package com.cepservice.repository;

import com.cepservice.model.CepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CepLogRepository extends JpaRepository<CepLog, Long> {

    List<CepLog> findByCepOrderByConsultadoEmDesc(String cep);
}
