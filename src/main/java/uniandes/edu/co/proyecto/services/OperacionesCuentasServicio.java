package uniandes.edu.co.proyecto.services;


import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import uniandes.edu.co.proyecto.modelo.OperacionCuenta;
import uniandes.edu.co.proyecto.repositorios.OperacionCuentaRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OperacionesCuentasServicio {
    

    private OperacionCuentaRepository operacionesCuentasRepository;

    public OperacionesCuentasServicio(OperacionCuentaRepository operacionesCuentasRepository){
      this.operacionesCuentasRepository = operacionesCuentasRepository;
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

}
