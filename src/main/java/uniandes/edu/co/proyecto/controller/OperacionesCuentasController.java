package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
          model.addAttribute("operacionesCuentas", operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta));
          return "operacionesCuentas";
        }else if (numero_cuentaCm != null) {
          model.addAttribute("operacionesCuentas", operacionesCuentasServicio.consultaOpCuentaUltimoMesReadCommited(numero_cuentaCm));
          return "operacionesCuentas";
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
  public String consultaOpCuentaUltimoMesSerializable(RedirectAttributes redirectAttributes, Integer numero_cuenta) {
    try {
      System.out.println("entro");
      operacionesCuentasServicio.consultaOpCuentaUltimoMesSerializable(numero_cuenta);
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
