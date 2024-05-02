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
  public String operaciones_cuentas(Model model, Integer numero_cuenta ,Integer numero_cuentaCm) {
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
  public String operaciones_cuentasSave(@ModelAttribute OperacionCuenta operacionCuenta, RedirectAttributes redirectAttributes) {
    //Cuenta cuentaLlegada=cuentaRepository.darCuenta(operacionCuenta.getCuenta_llegada());
    Cuenta cuentaSalida=cuentaRepository.darCuenta(operacionCuenta.getCuenta_salida());
    
    if(operacionCuenta.getTipo_operacion().equals("Consignacion")){
        //ACA SOLO SE MODIFICA CUENTA LLEGADA
        try {
          Float valorOperacion=operacionCuenta.getMonto_operacion();
          Float saldo=cuentaSalida.getSaldo();
          operacionesCuentasServicio.operacionConsignacion(cuentaSalida,valorOperacion,saldo);
        } 
        catch (InterruptedException e) {
          System.err.println("Error : " + e.getMessage());
          redirectAttributes.addFlashAttribute("errorMessage", "error");
          return "redirect:/operacionesCuentas";
          }
    }
    if (operacionCuenta.getTipo_operacion().equals("Retiro") ){
      try {
        Float valorOperacion=operacionCuenta.getMonto_operacion();
        Float saldo=cuentaSalida.getSaldo();
        operacionesCuentasServicio.operacionRetiro(cuentaSalida, valorOperacion,saldo);
      } 
      catch (InterruptedException e) {
        System.err.println("Error : " + e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "error");
        return "redirect:/operacionesCuentas";
        }
    }

    if (operacionCuenta.getTipo_operacion().equals("Transferencia") ){
      try {
        operacionesCuentasServicio.operacionTransferencia(operacionCuenta,cuentaSalida);

      } 
      catch (InterruptedException e) {
        System.err.println("Error : " + e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "error");
        return "redirect:/operacionesCuentas";
        }

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
