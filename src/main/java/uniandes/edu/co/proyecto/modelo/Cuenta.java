package uniandes.edu.co.proyecto.modelo;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String numero_cuenta;
    private String estado;
    private Float saldo;
    private String tipo;
    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    private Cliente cliente;

    private Date ultima_transaccion;
    private Integer gerente_oficina_creador;
    private Date fecha_creacion;

    public Cuenta() {
        ;
    }

    public Cuenta(String numero_cuenta, String estado, Float saldo, String tipo, Cliente cliente,
            Date ultima_transaccion, int gerente_oficina_creador, Date fecha_creacion) {
        this.numero_cuenta = numero_cuenta;
        this.estado = estado;
        this.saldo = saldo;
        this.tipo = tipo;
        this.cliente = cliente;
        this.ultima_transaccion = ultima_transaccion;
        this.gerente_oficina_creador = gerente_oficina_creador;
        this.fecha_creacion = fecha_creacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getUltima_transaccion() {
        return ultima_transaccion;
    }

    public void setUltima_transaccion(Date ultima_transaccion) {
        this.ultima_transaccion = ultima_transaccion;
    }

    public Integer getGerente_oficina_creador() {
        return gerente_oficina_creador;
    }
    
    public void setGerente_oficina_creador(int gerente_oficina_creador) {
        this.gerente_oficina_creador = gerente_oficina_creador;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

}