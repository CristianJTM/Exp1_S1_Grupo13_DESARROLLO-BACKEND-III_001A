package com.bancoxyz.batch.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "intereses_procesados")
public class Interes {

    @Id
    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "saldo_inicial", precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "tasa", precision = 5, scale = 4)
    private BigDecimal tasa;

    @Column(name = "interes", precision = 15, scale = 2)
    private BigDecimal interes;

    @Column(name = "saldo_final", precision = 15, scale = 2)
    private BigDecimal saldoFinal;

    public Interes() {
    }

    public Interes(
            Long cuentaId,
            String nombre,
            BigDecimal saldoInicial,
            Integer edad,
            String tipo,
            BigDecimal tasa,
            BigDecimal interes,
            BigDecimal saldoFinal) {

        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldoInicial = saldoInicial;
        this.edad = edad;
        this.tipo = tipo;
        this.tasa = tasa;
        this.interes = interes;
        this.saldoFinal = saldoFinal;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getTasa() {
        return tasa;
    }

    public void setTasa(BigDecimal tasa) {
        this.tasa = tasa;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
}
