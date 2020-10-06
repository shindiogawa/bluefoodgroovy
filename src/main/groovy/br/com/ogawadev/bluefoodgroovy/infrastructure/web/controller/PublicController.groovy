package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.ClienteService
import br.com.ogawadev.bluefoodgroovy.application.service.RestauranteService
import br.com.ogawadev.bluefoodgroovy.application.service.ValidationException
import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

import javax.validation.Valid
import org.springframework.ui.Model
@Controller
@RequestMapping(path = "/public")
class PublicController {

    @Autowired
    private ClienteService clienteService

    @Autowired
    private RestauranteService restauranteService

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository

    @GetMapping("/cliente/new")
    String newCliente(Model model) {

        model.addAttribute("cliente", new Cliente())

        ControllerHelper.setEditMode(model, false)

        return "cliente-cadastro"
    }

    @GetMapping("/restaurante/new")
    String newRestaurante(Model model) {


        model.addAttribute("restaurante", new Restaurante())

        ControllerHelper.setEditMode(model, false)
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model)
        return "restaurante-cadastro"
    }

    @PostMapping(path = "/cliente/save")
    String saveCliente(
            @ModelAttribute("cliente") @Valid Cliente cliente,
            Errors errors,
            Model model) {

        if(!errors.hasErrors()) {
            try{
                clienteService.saveCliente(cliente)
                model.addAttribute("msg","Cliente gravado com sucesso!")
            } catch (ValidationException e) {
                errors.rejectValue("email",null, e.getMessage())
            }

        }

        ControllerHelper.setEditMode(model, false)

        return "cliente-cadastro"
    }

    @PostMapping(path = "/restaurante/save")
    String saveRestaurante(
            @ModelAttribute("restaurante") @Valid Restaurante restaurante,
            Errors errors,
            Model model) {

        if(!errors.hasErrors()) {
            try{
                restauranteService.saveRestaurante(restaurante)
                model.addAttribute("msg","Restaurante gravado com sucesso!")
            } catch (ValidationException e) {
                errors.rejectValue("email",null, e.getMessage())
            }

        }

        ControllerHelper.setEditMode(model, false)
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model)
        return "restaurante-cadastro"
    }

}
