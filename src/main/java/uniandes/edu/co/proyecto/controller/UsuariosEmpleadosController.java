package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import uniandes.edu.co.proyecto.modelo.UsuarioEmpleado;
import uniandes.edu.co.proyecto.repositorios.UsuarioClienteRepository;
import uniandes.edu.co.proyecto.repositorios.UsuarioEmpleadoRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class UsuariosEmpleadosController {

    @Autowired
    private UsuarioEmpleadoRepository usuarioEmpleadoRepository;

    @Autowired
    private UsuarioClienteRepository usuarioClienteRepository;

    @GetMapping("/usuariosEmpleados")
    public String usuariosEmpleados(Model model) {
        model.addAttribute("usuariosEmpleados", usuarioEmpleadoRepository.darUsuariosEmpleados());
        return "usuariosEmpleados";
    }

    @GetMapping("/administrador")
    public String volverAdmin(Model model) {
        return "administrador";
    }

    @GetMapping("/gerenteOficina")
    public String volverGerenteOficina(Model model) {
        return "gerenteOficina";
    }

    @GetMapping("/cajeroDos")
    public String volverCajeroDos(Model model) {
        return "cajeroDos";
    }

    @RequestMapping(path= "/usuarios", method= RequestMethod.GET)
    public String usuariosEmpleadosCargo(@RequestParam("login") String login, @RequestParam("password") String password, Model model) {

        String paginaDestino = "index";
        
        if(login != null && !login.equals("") && password != null && !password.equals(""))
        { 
            model.addAttribute("login", login);
            model.addAttribute("password", password);

            String cargo= usuarioEmpleadoRepository.verificarUsuarioEmpleadoYObtenerCargo(login, password);
            String cliente= usuarioClienteRepository.verificarUsuarioCliente(login, password);



            if (cargo != null) {
                if (cargo.equals("Gerente oficina")) {
                paginaDestino = "gerenteOficina";
            } else if (cargo.equals("Gerente general")) {
                paginaDestino = "gerenteGeneral";
            } else if (cargo.equals("Cajero")) {
                paginaDestino = "cajeroDos";
            }else if (cargo.equals("Administrador")) {
                paginaDestino = "administrador";
            }
            }else if(cliente != null){
                paginaDestino="clientes";
            }

        } else {
            
            paginaDestino = "loginIncorrecto";
        }

        return paginaDestino;
    }

    @GetMapping("/usuariosEmpleados/new")
    public String usuarioEmpleadoForm(Model model) {
        model.addAttribute("usuarioEmpleado", new UsuarioEmpleado());
        return "empleadoNew";
    }

    @PostMapping("/usuariosEmpleados/new/save")
    public String usuarioEmpleadoSave(@ModelAttribute UsuarioEmpleado usuarioEmpleado) {

        usuarioEmpleadoRepository.insertarUsuarioEmpleado(usuarioEmpleado.getLogin(), usuarioEmpleado.getPassword_empleado(), 
                                                          usuarioEmpleado.getId_empleado().getId());
        
        return "administrador";
    }

    @GetMapping("/usuariosEmpleados/{id}/edit")
    public String usuarioEmpleadoEditForm(@PathVariable("id") int id, Model model) {
        UsuarioEmpleado usuarioEmpleado = usuarioEmpleadoRepository.darUsuarioEmpleado(id);
        if (usuarioEmpleado != null) {
            model.addAttribute("usuarioEmpleado", usuarioEmpleado);
            return "usuarioEmpleadoEdit";
        } else {
            return "redirect:/usuariosEmpleados";
        }
    }

    @PostMapping("/usuariosEmpleados/{id}/save")
    public String usuarioEmpleadoEditSave(@PathVariable("id") long id, @ModelAttribute UsuarioEmpleado usuarioEmpleado) {
        usuarioEmpleadoRepository.insertarUsuarioEmpleado(usuarioEmpleado.getLogin(), usuarioEmpleado.getPassword_empleado(),
                                                        usuarioEmpleado.getId());
        return "redirect:/usuariosEmpleados";
    }

    @GetMapping("/usuariosEmpleados/{id}/delete")
    public String usuarioEmpleadoDelete(@PathVariable("id") long id) {
        usuarioEmpleadoRepository.eliminarUsuarioEmpleado(id);
        return "redirect:/usuariosEmpleados";
    }
    
}
