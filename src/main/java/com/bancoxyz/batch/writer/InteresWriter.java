package com.bancoxyz.batch.writer;

import com.bancoxyz.batch.config.BatchDataConfig.InteresProcesado;
import com.bancoxyz.batch.model.Interes;
import com.bancoxyz.batch.repository.CuentaRepository;


import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class InteresWriter
        implements ItemWriter<InteresProcesado> {

    private final CuentaRepository cuentaRepository;

    public InteresWriter(
            CuentaRepository cuentaRepository) {

        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public void write(
            Chunk<? extends InteresProcesado> chunk) {

        for (InteresProcesado item : chunk.getItems()) {

            Interes interes = new Interes();

            interes.setCuentaId(item.cuentaId());
            interes.setNombre(item.nombre());
            interes.setSaldoInicial(item.saldoInicial());
            interes.setEdad(item.edad());
            interes.setTipo(item.tipo());
            interes.setTasa(item.tasa());
            interes.setInteres(item.interes());
            interes.setSaldoFinal(item.saldoFinal());

            cuentaRepository.save(interes);
        }
    }
}
