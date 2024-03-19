package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="cuentas")
public class Cuenta {

    @Id
    private Integer numeroCuenta;
    private String estado;
    private Integer saldo;
    private String tipo;
    private Integer cliente;
    private String ultimaTransaccion;
    private String gerenteOficinaCreador;
    private String fechaCreacion;

    public Cuenta(Integer numeroCuenta, String estado, Integer saldo, String tipo, String ultimaTransaccion, String gerenteOficinaCreador, String fechaCreacion) {
        super();
        this.numeroCuenta = numeroCuenta;
        this.estado = estado;
        this.saldo = saldo;
        this.tipo = tipo;
        this.ultimaTransaccion = ultimaTransaccion;
        this.gerenteOficinaCreador=gerenteOficinaCreador;
        this.fechaCreacion=fechaCreacion;
    }

    public Cuenta() {
    }

    public Integer getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Integer numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUltimaTransaccion() {
        return ultimaTransaccion;
    }

    public void setUltimaTransaccion(String ultimaTransaccion) {
        this.ultimaTransaccion = ultimaTransaccion;
    }

    public String getGerenteOficinaCreador() {
        return gerenteOficinaCreador;
    }

    public void setGerenteOficinaCreador(String gerenteOficinaCreador) {
        this.gerenteOficinaCreador = gerenteOficinaCreador;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}