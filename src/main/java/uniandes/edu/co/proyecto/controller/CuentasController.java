package uniandes.edu.co.proyecto.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import uniandes.edu.co.proyecto.modelo.Cuenta;
import uniandes.edu.co.proyecto.repositorios.CuentaRepository;

@Controller
public class CuentasController {
  @Autowired
  private CuentaRepository cuentaRepository;

  @GetMapping("/cuentas")
  public String cuentas(Model model) {
    model.addAttribute("cuentas", cuentaRepository.darCuentas());
    return "cuentas";
  }

  @GetMapping("/cuentas/new")
  public String cuentasForm(Model model) {
    model.addAttribute("cuenta", new Cuenta());
    return "cuentaNew";
  }

  @PostMapping("/cuentas/new/save")
  public String cuentasSave(@ModelAttribute Cuenta cuenta) {
    cuentaRepository.insertarCuenta(cuenta.getNumero_cuenta(),
        cuenta.getEstado(), cuenta.getSaldo(), cuenta.getTipo(),
        cuenta.getCliente().getId(), cuenta.getUltima_transaccion(),
        cuenta.getGerente_oficina_creador(),
        cuenta.getFecha_creacion());
    return "redirect:/cuentas";

  }

  @GetMapping("/cuentas/{id}/edit")
  public String cuentaEditForm(@PathVariable("id") int id, Model model) {
    Cuenta cuenta = cuentaRepository.darCuenta(id);
    if (cuenta != null) {
      model.addAttribute("cuenta", cuenta);
      return "cuentaEdit";
    } else {
      return "redirect:/cuentas";
    }
  }

  @PostMapping("/cuentas/{id}/edit/save")
  public String cuentaEditSave(@PathVariable("id") long id, @ModelAttribute Cuenta cuenta) {
    cuentaRepository.actualizarCuenta(id, cuenta.getNumero_cuenta(), cuenta.getEstado(), cuenta.getSaldo(),
        cuenta.getTipo(), cuenta.getCliente().getId(), cuenta.getUltima_transaccion(),
        cuenta.getGerente_oficina_creador(), cuenta.getFecha_creacion());
    return "redirect:/cuentas";
  }

  @GetMapping("/cuentas/{id}/delete")
  public String cuentaBorrar(@PathVariable("id") long id) {
    cuentaRepository.eliminarCuenta(id);
    return "redirect:/cuentas";
  }
}
