package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Cuenta;
import uniandes.edu.co.proyecto.modelo.OperacionCuenta;
import uniandes.edu.co.proyecto.repositorios.OperacionCuentaRepository;
import uniandes.edu.co.proyecto.services.OperacionesCuentasServicio;
import uniandes.edu.co.proyecto.repositorios.CuentaRepository;

import java.sql.Date;
import java.util.Optional;

@Controller
public class OperacionesCuentasController {
  @Autowired
  private OperacionCuentaRepository operacionCuentaRepository;

  @Autowired
  private OperacionesCuentasServicio operacionesCuentasServicio;

  @Autowired
  private CuentaRepository cuentaRepository;

  @GetMapping("/operacionesCuentas")
  public String operaciones_cuentas(Model model, Integer numero_cuenta,Integer numero_cuentaCm) {
    Date fecha = new Date(System.currentTimeMillis());
    int retryCount = 0;
    while (true) {
      try {
        if (numero_cuenta != null) {
          model.addAttribute("operacionesCuentas1", operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta).get("operacion_cuenta1"));
          model.addAttribute("operacionesCuentas2", operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta).get("operacion_cuenta2"));
          return "operacionesCuentasDos";
        }else if (numero_cuentaCm != null) {  
          model.addAttribute("operacionesCuentas1", operacionesCuentasServicio.consultaOpCuentaUltimoMesReadCommited(numero_cuentaCm).get("operacion_cuenta1"));
          model.addAttribute("operacionesCuentas2", operacionesCuentasServicio.consultaOpCuentaUltimoMesReadCommited(numero_cuentaCm).get("operacion_cuenta2"));
          return "operacionesCuentasDos";
        } else {
          model.addAttribute("operacionesCuentas", operacionCuentaRepository.darOperacionesCuentas());
        }
        break; // Si se completa con éxito, salir del bucle
      } catch (Exception e) {
          System.out.println("Intento " + (retryCount + 1) + ": " + e);
      }
  }
  return "operacionesCuentas";

  }

  
  @GetMapping("/ConsultaOpCuentasSe")
  public String consultaOpCuentasSe(Model model) {
    return "ConsultaOpCuentasSe";
  }

  @GetMapping("/ConsultaOpCuentasRc")
  public String consultaOpCuentasRc(Model model) {
    return "ConsultaOpCuentasRc";
  }
  
  @GetMapping("/operacionCuentaCajero")
  public String operacionesCuentasCajero(Model model) {
    model.addAttribute("operacionesCuentas", operacionCuentaRepository.darOperacionesCuentas());
    return "operacionCuentaCajero";
  }

  @GetMapping("/operacionesCuentas/new")
  public String operaciones_cuentasForm(Model model) {
    model.addAttribute("operacionCuenta", new OperacionCuenta());
    return "operacionesCuentasNew";
  }

  @GetMapping("/operacionesCuentasCajero/new")
  public String operaciones_cuentasCajeroForm(Model model) {
    model.addAttribute("operacionCuenta", new OperacionCuenta());
    return "operacionesCuentaCajeroNew";
  }

  @PostMapping("/operacionesCuentas/new/save")
  public String operaciones_cuentasSave(@ModelAttribute OperacionCuenta operacionCuenta) {
    //Cuenta cuentaLlegada=cuentaRepository.darCuenta(operacionCuenta.getCuenta_llegada());
    Cuenta cuentaSalida=cuentaRepository.darCuenta(operacionCuenta.getCuenta_salida());
    
    if(operacionCuenta.getTipo_operacion().equals("Consignacion")){
        //ACA SOLO SE MODIFICA CUENTA LLEGADA
        Float valorOperacion=operacionCuenta.getMonto_operacion();
        Float saldo=cuentaSalida.getSaldo();
        valorOperacion=valorOperacion+saldo;
        cuentaRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), valorOperacion, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
    }
    if (operacionCuenta.getTipo_operacion().equals("Retiro") ){
      Float valorOperacion=operacionCuenta.getMonto_operacion();
      Float saldo=cuentaSalida.getSaldo();
      valorOperacion=saldo-valorOperacion;
      cuentaRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), valorOperacion, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
    }

    if (operacionCuenta.getTipo_operacion().equals("Transferencia") ){
      Cuenta cuentaLlegada=cuentaRepository.darCuenta(operacionCuenta.getCuenta_llegada());
      Float valorOperacion=operacionCuenta.getMonto_operacion();
      //AFECTO CUENTA DE SALIDA
      Float saldoSalida=cuentaSalida.getSaldo();
      saldoSalida=saldoSalida-valorOperacion;
      //AFECTO CUENTA DE LLEGADA
      Float saldoLLegada=cuentaLlegada.getSaldo();
      saldoLLegada=saldoLLegada+valorOperacion;
      cuentaRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), saldoSalida, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
      cuentaRepository.actualizarCuenta(cuentaLlegada.getId(), cuentaLlegada.getNumero_cuenta(), cuentaLlegada.getEstado(), saldoLLegada, cuentaLlegada.getTipo(), cuentaLlegada.getCliente().getId(), cuentaLlegada.getUltima_transaccion(), cuentaLlegada.getGerente_oficina_creador(), cuentaLlegada.getFecha_creacion());

    }
    
    operacionCuentaRepository.insertarOperacioneCuenta(operacionCuenta.getTipo_operacion(), operacionCuenta.getFecha(),
        operacionCuenta.getCuenta_salida(), operacionCuenta.getMonto_operacion(), operacionCuenta.getCliente(),
        operacionCuenta.getPunto_atencion().getId(), operacionCuenta.getCuenta_llegada());
    return "redirect:/operacionesCuentas";
  }

  @GetMapping("/operacionesCuentas/{id}/edit")
  public String operaciones_cuentasEditForm(@PathVariable("id") int id, Model model) {
    OperacionCuenta operacionCuenta = operacionCuentaRepository.darOperacioneCuenta(id);
    if (operacionCuenta != null) {
      model.addAttribute("operacionesCuentas", operacionCuenta);
      return "operacionesCuentas";
    } else {
      return "redirect:/operacionesCuentas";
    }
  }

  @PostMapping("/operacionesCuentas/{id}/edit/save")
  public String operaciones_cuentasEditSave(@PathVariable("id") long id,
      @ModelAttribute OperacionCuenta operacionCuenta) {
    operacionCuentaRepository.actualizarOperacioneCuenta(id, operacionCuenta.getTipo_operacion(),
        operacionCuenta.getFecha(), operacionCuenta.getCuenta_salida(), operacionCuenta.getMonto_operacion(),
        operacionCuenta.getCliente(), operacionCuenta.getCliente(), operacionCuenta.getCuenta_llegada());
    return "redirect:/operacionesCuentas";
  }

  @GetMapping("/operacionesCuentas/{id}/delete")
  public String operaciones_cuentasBorrar(@PathVariable("id") long id) {
    operacionCuentaRepository.eliminaOperacioneCuenta(id);
    return "redirect:/operacionesCuentas";
  }

  @GetMapping("/operacionesCuentas/consultaSerializable")
  public String consultaOpCuentaUltimoMesSerializable(RedirectAttributes redirectAttributes, Integer numero_cuenta,Model model) {
    try {
      System.out.println("entro");
      operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta);
      model.addAttribute("operacionesCuentas", operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta));
    } 
    catch (InterruptedException e) {
      System.err.println("Error durante la consulta : " + e.getMessage());
      redirectAttributes.addFlashAttribute("errorMessage", "No se pudo consultar las operaciones correctamente.");
      return "redirect:/operacionesCuentas";
      }

    return "redirect:/operacionesCuentas";
  }

  @GetMapping("/operacionesCuentas/consultaReadUncommited")
  public String consultaOpCuentaUltimoMesReadCommited(RedirectAttributes redirectAttributes, Integer numero_cuenta) {
    try {
      operacionesCuentasServicio.consultaOpCuentaUltimoMesReadCommited(numero_cuenta);
    } 
    catch (InterruptedException e) {
      System.err.println("Error durante la consulta : " + e.getMessage());
      redirectAttributes.addFlashAttribute("errorMessage", "No se pudo consultar las operaciones correctamente.");
      return "redirect:/operacionesCuentas";
      }

    return "redirect:/operacionesCuentas";
  }

}






@PostMapping("/operacionCuenta/new/save")
@Transactional
public String transaccionGuardar (@ModelAttribute OperacionCuenta operacionCuenta, Model model) {
  try {
  Cuenta cuentaSalida=cuentaRepository.darCuenta(operacionCuenta.getCuenta_salida());

  Float valorOperacion=operacionCuenta.getMonto_operacion();
  Float saldo=cuentaSalida.getSaldo();
  valorOperacion=valorOperacion+saldo;
  cuentaRepository.actualizarCuenta(cuentaSalida.getId(), cuentaSalida.getNumero_cuenta(), cuentaSalida.getEstado(), valorOperacion, cuentaSalida.getTipo(), cuentaSalida.getCliente().getId(), cuentaSalida.getUltima_transaccion(), cuentaSalida.getGerente_oficina_creador(), cuentaSalida.getFecha_creacion());
int rowsAffectedConsignar = cuentaRepository.actualizarSaldoConsignar (cuentaSalida.getId(), operacionCuenta.getMonto_operacion()); 
int rowsAffectedRetirar = cuentaRepository.actualizarSaldoRetiro (operacionCuenta.getCuenta_salida().getId(), operacionCuenta.getMonto_operacion()); 
if (rowsAffectedConsignar > 0 && rowsAffectedRetirar > 0) {
  OperacionCuentaRepository.insertarTransaccion (operacionCuenta.getFecha(), operacionCuenta.getMonto_operacion(), operacionCuenta.getCuenta_salida().getId(), operacionCuenta.getCuenta_llegada().getId(), operacionCuenta.getPunto_atencion().getId());
  return "redirect:/cuentas";
} else {

throw new RuntimeException("Error al hacer la transaccion: No se pudieron completar las actualizaciones de saldo");
} 
} catch (Exception e) {
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
model.addAttribute("errorMessage", e.getMessage());
System.out.println(e.getMessage());
return "error";
}
}
}




@PostMapping("/retirar/new/save")
@Transactional
public String retirarDinero (@ModelAttribute OperacionCuenta operacion_cuenta, Model model) {
try {
  int rowsAffectedRetirar = cuentaRepository.actualizarSaldoRetiro (operacion_cuenta.getId(), operacion_cuenta.getMonto_operacion()); 
  if (rowsAffectedRetirar > 0) {
    operacionCuentaRepository.insertarOperacion_cuenta (operacion_cuenta.getTipo_operacion(), operacion_cuenta.getFecha_operacion(), operacion_cuenta.getMonto_pago(), operacion_cuenta.getPunto_atencion().getId());
return "redirect:/cuentas";
  }
else {
throw new RuntimeException("Error al hacer el retiro: No se pudieron completar las actualizaciones de saldo");
} }catch (Exception e) {
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
model.addAttribute("errorMessage", "Error al retirar dinero: " + e.getMessage());
System.out.println(e.getMessage());
return "redirect:/error";
}
}
}