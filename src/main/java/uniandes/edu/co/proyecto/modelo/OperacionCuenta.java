package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="operacion_cuenta")
public class OperacionCuenta {

    @Id
    private Integer id;
    private String tipoOperacion;
    private String fecha;
    private String cuentaSalida;
    private Float montoOperacion;
    private String cliente;
    private String cuentaLlegada;


    public OperacionCuenta(Integer id, String tipoOperacion, String fecha, String cuentaSalida, Float montoOperacion, String cliente, String cuentaLlegada) {
        super();
        this.id = id;
        this.tipoOperacion = tipoOperacion;
        this.fecha = fecha;
        this.cuentaSalida = cuentaSalida;
        this.montoOperacion = montoOperacion;
        this.cliente=cliente;
        this.cuentaLlegada=cuentaLlegada;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCuentaSalida() {
        return cuentaSalida;
    }

    public void setCuentaSalida(String cuentaSalida) {
        this.cuentaSalida = cuentaSalida;
    }

    public Float getMontoOperacion() {
        return montoOperacion;
    }

    public void setMontoOperacion(Float montoOperacion) {
        this.montoOperacion = montoOperacion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCuentaLlegada() {
        return cuentaLlegada;
    }

    public void setCuentaLlegada(String cuentaLlegada) {
        this.cuentaLlegada = cuentaLlegada;
    }
}
