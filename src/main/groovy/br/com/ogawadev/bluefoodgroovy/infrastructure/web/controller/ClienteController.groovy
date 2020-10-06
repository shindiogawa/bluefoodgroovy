package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.ClienteService
import br.com.ogawadev.bluefoodgroovy.application.service.RestauranteService
import br.com.ogawadev.bluefoodgroovy.application.service.ValidationException
import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.cliente.ClienteRepository
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapioRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.SearchFilter
import br.com.ogawadev.bluefoodgroovy.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.validation.Valid

@Controller
@RequestMapping(path = "/cliente")
class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository

    @Autowired
    private ClienteService clienteService

    @Autowired
    private RestauranteService restauranteService

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository

    @Autowired
    private RestauranteRepository restauranteRepository

    @Autowired
    private ItemCardapioRepository itemCardapioRepository

    @Autowired
    private PedidoRepository pedidoRepository

    @GetMapping(path = "/home")
    String home(Model model) {
        List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"))

        List<Pedido> pedidos = pedidoRepository.listPedidosByCliente(SecurityUtils.loggedCliente().id)
        model.addAttribute("pedidos", pedidos)
        model.addAttribute("categorias", categorias)
        model.addAttribute("searchFilter", new SearchFilter())
        return "cliente-home"
    }

    @GetMapping("/edit")
    String edit(Model model) {
        Integer clienteId = SecurityUtils.loggedCliente().getId()
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow({ -> new NoSuchElementException() })

        model.addAttribute("cliente", cliente)

        ControllerHelper.setEditMode(model, true)

        return "cliente-cadastro"
    }

    @PostMapping("/save")
    String save(
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

        ControllerHelper.setEditMode(model, true)

        return "cliente-cadastro"
    }

    @GetMapping(path = "/search")
    String search(
            @ModelAttribute("searchFilter") SearchFilter filter,
            @RequestParam(value = "cmd", required = false) String command,
            Model model) {

        filter.processFilter(command)

        List<Restaurante> restaurantes = restauranteService.search(filter)

        model.addAttribute("restaurantes",restaurantes)

        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model)

        model.addAttribute("searchFilter", filter)
        model.addAttribute("cep", SecurityUtils.loggedCliente().cep)

        return "cliente-busca"
    }

    @GetMapping(path = "/restaurante")
    String viewRestaurante(
            @RequestParam("restauranteId") Integer restauranteId,
            @RequestParam(value = "categoria", required = false) String categoria,
            Model model
    ) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow({ -> new NoSuchElementException() })

        model.addAttribute("restaurante", restaurante)
        model.addAttribute("cep", SecurityUtils.loggedCliente().cep)

        List<String> categorias = itemCardapioRepository.findCategorias(restauranteId)
        model.addAttribute("categorias", categorias)

        List<ItemCardapio> itensCardapioDestaque
        List<ItemCardapio> itensCardapioNaoDestaque

        if(categoria == null ) {
            itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true)
            itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false)

        } else {
            itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria)
            itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria)
        }

        model.addAttribute("itensCardapioDestaque", itensCardapioDestaque)
        model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque)
        model.addAttribute("categoriaSelecionada", categoria)
        return "cliente-restaurante"
    }
}
