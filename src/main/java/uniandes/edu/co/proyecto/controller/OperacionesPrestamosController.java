package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.modelo.OperacionPrestamo;
import uniandes.edu.co.proyecto.modelo.Prestamo;
import uniandes.edu.co.proyecto.repositorios.OperacionPrestamoRepository;
import uniandes.edu.co.proyecto.repositorios.PrestamoRepository;
import uniandes.edu.co.proyecto.repositorios.TransaccionPrestamoRepository;

@Controller
public class OperacionesPrestamosController {
  @Autowired
  private OperacionPrestamoRepository operacionPrestamoRepository;

  @Autowired
  private PrestamoRepository prestamoRepository;

  @Autowired
  private TransaccionPrestamoRepository transaccionPrestamoRepository;

  @GetMapping("/operacionesPrestamos")
  public String operaciones_cuentas(Model model) {
    model.addAttribute("operacionesPrestamos", operacionPrestamoRepository.darOperacionesPrestamos());
    return "cajeroDos";
  }

  @GetMapping("/operacionesPrestamos/new")
  public String operaciones_cuentasForm(Model model) {
    model.addAttribute("operacionPrestamo", new OperacionPrestamo());
    return "operacionesPrestamosNew";
  }

  @PostMapping("/operacionesPrestamos/new/save")
  public String operaciones_prestamosSave(@ModelAttribute OperacionPrestamo operacionPrestamo) {
    Prestamo prestamo = prestamoRepository.darPrestamo(operacionPrestamo.getCuenta_prestamo());
    float diferencia = Math.abs(operacionPrestamo.getMonto_operacion() - prestamo.getValor_cuota());
    if(operacionPrestamo.getTipo_operacion().equals("pago_extraordinario") && diferencia > 0.0001f){
      return "operacionIncorreta";
    } 
    if(operacionPrestamo.getMonto_operacion() > prestamo.getSaldo_pendiente()){
      return "operacionIncorreta";
    }

    operacionPrestamoRepository.insertarOperacionPrestamo(operacionPrestamo.getTipo_operacion(),
        operacionPrestamo.getFecha(), operacionPrestamo.getCuenta_prestamo(), operacionPrestamo.getMonto_operacion(),
        operacionPrestamo.getCliente(), operacionPrestamo.getPunto_atencion().getId());
    

    Integer id= operacionPrestamoRepository.darIdOperacionPrestamo(operacionPrestamo.getTipo_operacion(),
        operacionPrestamo.getCuenta_prestamo(), operacionPrestamo.getMonto_operacion(),
        operacionPrestamo.getCliente(), operacionPrestamo.getPunto_atencion().getId());

    transaccionPrestamoRepository.insertarTransaccionPrestamo(id, operacionPrestamo.getCuenta_prestamo());

    prestamoRepository.actualizarPrestamo(prestamo.getId(), prestamo.getEstado(), prestamo.getTipo(), prestamo.getMonto(),
        prestamo.getInteres(), prestamo.getNumero_cuotas(), prestamo.getDia_mes_pagar_cuota(),
        prestamo.getValor_cuota(), prestamo.getCliente().getId(), prestamo.getGerente_creador(), prestamo.getSaldo_pendiente() - operacionPrestamo.getMonto_operacion());

    return "cajeroDos";
  }

  @GetMapping("/operacionesPrestamos/{id}/edit")
  public String operaciones_prestamosEditForm(@PathVariable("id") int id, Model model) {
    OperacionPrestamo operacionPrestamo = operacionPrestamoRepository.darOperacionPrestamo(id);
    if (operacionPrestamo != null) {
      model.addAttribute("operacionPrestamo", operacionPrestamo);
      return "operacionesPrestamosEdit";
    } else {
      return "redirect:/operacionesPrestamos";
    }
  }

  @PostMapping("/operacionesPrestamos/{id}/edit/save")
  public String operaciones_prestamosEditSave(@PathVariable("id") long id,
      @ModelAttribute OperacionPrestamo operacionPrestamo) {
    operacionPrestamoRepository.actualizarOperacionPrestamo(id, operacionPrestamo.getTipo_operacion(),
        operacionPrestamo.getFecha(), operacionPrestamo.getCuenta_prestamo(), operacionPrestamo.getMonto_operacion(),
        operacionPrestamo.getCliente(), operacionPrestamo.getCliente());
    
    return "redirect:/operacionesPrestamos";
  }

  @GetMapping("/operacionesPrestamos/{id}/delete")
  public String operaciones_prestamosBorrar(@PathVariable("id") long id) {
    operacionPrestamoRepository.eliminaOperacionPrestamo(id);
    return "redirect:/operacionesPrestamos";
  }

}
