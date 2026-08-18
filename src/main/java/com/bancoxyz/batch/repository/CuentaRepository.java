package com.bancoxyz.batch.repository;

import com.bancoxyz.batch.model.Interes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository extends JpaRepository<Interes, Long> {

}
