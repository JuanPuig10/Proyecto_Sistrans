package uniandes.edu.co.proyecto.services;


import java.sql.Date;
import java.util.Collection;


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
    public Collection<OperacionCuenta> consultaOpCuentaUltimoMesSerializable(Integer numero_cuenta) throws InterruptedException {
        try{
                Date fecha = new Date(System.currentTimeMillis());
        
                Collection<OperacionCuenta>  operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
                System.out.println(operacionesCuentas.size());
        
                Thread.sleep(300);
                operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
    
                return operacionesCuentas;

        }catch(InterruptedException e){
                throw new InterruptedException("Error en la transaccion Serializable");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Collection<OperacionCuenta> consultaOpCuentaUltimoMesReadCommited(Integer numero_cuenta) throws InterruptedException {
    
            Date fecha = new Date(System.currentTimeMillis());
        
            Collection<OperacionCuenta>  operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 
            System.out.println(operacionesCuentas.size());
    
            Thread.sleep(300);
            operacionesCuentas = operacionesCuentasRepository.consultaOpCuentaUltimoMes(fecha,numero_cuenta); 

            return operacionesCuentas;


    }

}
