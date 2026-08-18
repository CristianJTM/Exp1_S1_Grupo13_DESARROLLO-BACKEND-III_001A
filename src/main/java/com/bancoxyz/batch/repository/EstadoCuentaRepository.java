package com.bancoxyz.batch.repository;

import com.bancoxyz.batch.model.CuentaAnual;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoCuentaRepository
        extends JpaRepository<CuentaAnual, Long> {

    List<CuentaAnual> findByCuentaId(Long cuentaId);

    List<CuentaAnual> findByAnio(Integer anio);

    List<CuentaAnual> findByCuentaIdAndAnio(
            Long cuentaId,
            Integer anio
    );
}
