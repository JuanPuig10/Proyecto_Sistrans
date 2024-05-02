package uniandes.edu.co.proyecto.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.modelo.UsuarioCliente;
import uniandes.edu.co.proyecto.repositorios.ClienteRepository;
import uniandes.edu.co.proyecto.repositorios.ClienteRepository.RespuestaExtracto;

@Controller
public class ClientesController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public String clientes(Model model,Integer numMes,Integer  numCuenta, Integer anio, String numero_documento) {

        if(numMes == null && numCuenta== null && anio==null && numero_documento == null )
        {
            model.addAttribute("clientes", clienteRepository.darClientes());
        }else if(numero_documento != null)
        {
            Integer id = clienteRepository.obtenerIdDadoDoc(numero_documento);
            if(id != null)
            {
                model.addAttribute("cliente", clienteRepository.darCliente(id));
                model.addAttribute("cuentas", clienteRepository.obtenerCuentasCliente(id));
                model.addAttribute("prestamos", clienteRepository.obtenerPrestamosCliente(id));
                model.addAttribute("oficinas", clienteRepository.obtenerOficinasDelCliente(id));
                return "consultaDeCliente";
            }



            model.addAttribute("clientes", clienteRepository.darClientes());
        }
        else
        {
            int a=1;
            Collection<RespuestaExtracto> informacion = clienteRepository.infoExtracto(numMes,numCuenta,anio);
            model.addAttribute("clientesExtracto", clienteRepository.infoExtracto(numMes, numCuenta, anio));

            RespuestaExtracto ultimoElemento = null;
            RespuestaExtracto primerElemento = null;

            if (!informacion.isEmpty()) {
                for (RespuestaExtracto elemento : informacion) {
                    if (primerElemento == null) {
                        primerElemento = elemento;
                    }
                    ultimoElemento = elemento;
                }
            }
            model.addAttribute("saldoFinalMes", ultimoElemento.getSALDO());
            model.addAttribute("saldoInicioMes", primerElemento.getSALDO());
            return "infoExtracto";


        }
        return "clientes";
    }

    @GetMapping("/clientes/new")
    public String clientesForm(Model model) {

        model.addAttribute("cliente", new Cliente());
        return "clienteNew";
    }

    @PostMapping("/clientes/new/save")
    public String clientesSave(@ModelAttribute Cliente cliente,Model model) {

        clienteRepository.insertarCliente(cliente.getNumero_documento(), cliente.getTipo(),
                cliente.getTipo_documento(), cliente.getNombre(),
                cliente.getNacionalidad(), cliente.getDireccion_fisica(),
                cliente.getDireccion_electronica(), cliente.getTelefono(),
                cliente.getCodigo_postal(), cliente.getCiudad(), cliente.getDepartamento());

        model.addAttribute("usuarioCliente", new UsuarioCliente());
        model.addAttribute("clientes", clienteRepository.darClientes());

        return "usuarioClienteNew";
    }

    @GetMapping("/clientes/{id}/edit")
    public String clienteEditForm(@PathVariable("id") int id, Model model) {
        Cliente cliente = clienteRepository.darCliente(id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "clienteEdit";
        } else {
            return "redirect:/clientes";
        }
    }

    @PostMapping("/clientes/{id}/edit/save")
    public String clienteEditSave(@PathVariable("id") long id, @ModelAttribute Cliente cliente) {
        clienteRepository.actualizarCliente(((long) id), cliente.getNumero_documento(), cliente.getTipo(),
                cliente.getTipo_documento(), cliente.getNombre(),
                cliente.getNacionalidad(), cliente.getDireccion_fisica(),
                cliente.getDireccion_electronica(), cliente.getTelefono(),
                cliente.getCodigo_postal(), cliente.getCiudad(), cliente.getDepartamento());
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{id}/delete")
    public String clienteBorrar(@PathVariable("id") long id) {
        clienteRepository.eliminarCliente(id);
        return "redirect:/clientes";
    }

}
