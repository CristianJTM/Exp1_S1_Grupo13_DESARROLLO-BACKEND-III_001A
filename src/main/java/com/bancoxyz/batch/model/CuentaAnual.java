package com.bancoxyz.batch.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "estados_anuales")
public class CuentaAnual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuenta_id", nullable = false)
    private Long cuentaId;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "total_depositos", precision = 15, scale = 2)
    private BigDecimal totalDepositos;

    @Column(name = "total_retiros", precision = 15, scale = 2)
    private BigDecimal totalRetiros;

    @Column(name = "saldo_movimiento", precision = 15, scale = 2)
    private BigDecimal saldoMovimiento;

    @Column(name = "cantidad_operaciones", nullable = false)
    private Integer cantidadOperaciones;

    @Column(name = "observacion", length = 500)
    private String observacion;

    public CuentaAnual() {
    }

    public CuentaAnual(
            Long cuentaId,
            Integer anio,
            BigDecimal totalDepositos,
            BigDecimal totalRetiros,
            BigDecimal saldoMovimiento,
            Integer cantidadOperaciones,
            String observacion) {

        this.cuentaId = cuentaId;
        this.anio = anio;
        this.totalDepositos = totalDepositos;
        this.totalRetiros = totalRetiros;
        this.saldoMovimiento = saldoMovimiento;
        this.cantidadOperaciones = cantidadOperaciones;
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getTotalDepositos() {
        return totalDepositos;
    }

    public void setTotalDepositos(BigDecimal totalDepositos) {
        this.totalDepositos = totalDepositos;
    }

    public BigDecimal getTotalRetiros() {
        return totalRetiros;
    }

    public void setTotalRetiros(BigDecimal totalRetiros) {
        this.totalRetiros = totalRetiros;
    }

    public BigDecimal getSaldoMovimiento() {
        return saldoMovimiento;
    }

    public void setSaldoMovimiento(BigDecimal saldoMovimiento) {
        this.saldoMovimiento = saldoMovimiento;
    }

    public Integer getCantidadOperaciones() {
        return cantidadOperaciones;
    }

    public void setCantidadOperaciones(Integer cantidadOperaciones) {
        this.cantidadOperaciones = cantidadOperaciones;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
