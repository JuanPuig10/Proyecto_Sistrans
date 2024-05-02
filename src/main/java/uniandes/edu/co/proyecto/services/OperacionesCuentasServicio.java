package uniandes.edu.co.proyecto.services;


import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import uniandes.edu.co.proyecto.modelo.Cuenta;
import uniandes.edu.co.proyecto.modelo.OperacionCuenta;
import uniandes.edu.co.proyecto.repositorios.CuentaRepository;
import uniandes.edu.co.proyecto.repositorios.OperacionCuentaRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OperacionesCuentasServicio {
    

    private OperacionCuentaRepository operacionesCuentasRepository;

    private CuentaRepository cuentasRepository;

    public OperacionesCuentasServicio(OperacionCuentaRepository operacionesCuentasRepository,CuentaRepository cuentasRepository){
      this.operacionesCuentasRepository = operacionesCuentasRepository;
      this.cuentasRepository =cuentasRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public HashMap<String, Collection<OperacionCuenta>>  consultaOpCuentaUltimoMesSerializable(Integer numero_cuenta) throws InterruptedException {
        try{
                HashMap<String, Collection<OperacionCuenta>> map = new HashMap<String, Collection<OperacionCuenta> >();
                
                Date fecha = new Date(System.currentTimeMillis());
        
                Collection<OperacionCuenta>  operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
                map.put("operacion_cuenta1", operacionesCuentas);
                System.out.println(operacionesCuentas.size());
        
                Thread.sleep(300);
                Collection<OperacionCuenta>  operacionesCuentas2 = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
                map.put("operacion_cuenta2", operacionesCuentas2);

                return map;

        }catch(InterruptedException e){
                throw new InterruptedException("Error en la transaccion Serializable");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public HashMap<String, Collection<OperacionCuenta>> consultaOpCuentaUltimoMesReadCommited(Integer numero_cuenta) throws InterruptedException {
        try{
                HashMap<String, Collection<OperacionCuenta>> map = new HashMap<String, Collection<OperacionCuenta> >();
                
                Date fecha = new Date(System.currentTimeMillis());
        
                Collection<OperacionCuenta>  operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
                map.put("operacion_cuenta1", operacionesCuentas);
                System.out.println(operacionesCuentas.size());
        
                Thread.sleep(300);
                Collection<OperacionCuenta>  operacionesCuentas2 = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
                map.put("operacion_cuenta2", operacionesCuentas2);

                return map;

        }catch(InterruptedException e){
                throw new InterruptedException("Error en la transaccion Read Committed");
        }
 
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void operacionRetiro(Cuenta cuentaSalida ,Float saldo, Float valorOperacion) throws InterruptedException {
        valorOperacion=saldo-valorOperacion;
        cuentasRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), valorOperacion, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
 
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void operacionConsignacion(Cuenta cuentaSalida, Float valorOperacion, Float saldo) throws InterruptedException {
        valorOperacion=valorOperacion+saldo;
        cuentasRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), valorOperacion, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
 
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void operacionTransferencia(OperacionCuenta operacionCuenta, Cuenta cuentaSalida) throws InterruptedException {
        Cuenta cuentaLlegada=cuentasRepository.darCuenta(operacionCuenta.getCuenta_llegada());
        Float valorOperacion=operacionCuenta.getMonto_operacion();
        //AFECTO CUENTA DE SALIDA
        Float saldoSalida=cuentaSalida.getSaldo();
        saldoSalida=saldoSalida-valorOperacion;
        //AFECTO CUENTA DE LLEGADA
        Float saldoLLegada=cuentaLlegada.getSaldo();
        saldoLLegada=saldoLLegada+valorOperacion;
        cuentasRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), saldoSalida, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
        cuentasRepository.actualizarCuenta(cuentaLlegada.getId(), cuentaLlegada.getNumero_cuenta(), cuentaLlegada.getEstado(), saldoLLegada, cuentaLlegada.getTipo(), cuentaLlegada.getCliente().getId(), cuentaLlegada.getUltima_transaccion(), cuentaLlegada.getGerente_oficina_creador(), cuentaLlegada.getFecha_creacion());
 
    }

}
