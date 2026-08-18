package com.bancoxyz.batch.writer;

import com.bancoxyz.batch.config.BatchDataConfig.CuentaAnualProcesada;
import com.bancoxyz.batch.model.CuentaAnual;
import com.bancoxyz.batch.repository.EstadoCuentaRepository;


import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class CuentaAnualWriter
        implements ItemWriter<CuentaAnualProcesada> {

    private final EstadoCuentaRepository estadoCuentaRepository;

    public CuentaAnualWriter(
            EstadoCuentaRepository estadoCuentaRepository) {

        this.estadoCuentaRepository =
                estadoCuentaRepository;
    }

    @Override
    public void write(
            Chunk<? extends CuentaAnualProcesada> chunk) {

        for (CuentaAnualProcesada item : chunk.getItems()) {

            CuentaAnual cuentaAnual =
                    new CuentaAnual();

            cuentaAnual.setCuentaId(
                    item.cuentaId()
            );

            cuentaAnual.setAnio(
                    item.anio()
            );

            cuentaAnual.setTotalDepositos(
                    item.totalDepositos()
            );

            cuentaAnual.setTotalRetiros(
                    item.totalRetiros()
            );

            cuentaAnual.setSaldoMovimiento(
                    item.saldoMovimiento()
            );

            cuentaAnual.setCantidadOperaciones(
                    item.cantidadOperaciones()
            );

            cuentaAnual.setObservacion(
                    item.observacion()
            );

            estadoCuentaRepository.save(
                    cuentaAnual
            );
        }
    }
}
