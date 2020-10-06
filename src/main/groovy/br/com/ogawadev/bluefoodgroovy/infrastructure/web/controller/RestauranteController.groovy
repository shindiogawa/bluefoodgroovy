package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.RelatorioService
import br.com.ogawadev.bluefoodgroovy.application.service.RestauranteService
import br.com.ogawadev.bluefoodgroovy.application.service.ValidationException
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioItemFaturamento
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioItemFilter
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioPedidoFilter
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.CategoriaRestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapioRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteRepository
import br.com.ogawadev.bluefoodgroovy.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
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
@RequestMapping(path = "/restaurante")
class RestauranteController {

    @Autowired
    private PedidoRepository pedidoRepository

    @Autowired
    private RestauranteRepository restauranteRepository

    @Autowired
    private RestauranteService restauranteService

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository

    @Autowired
    private ItemCardapioRepository itemCardapioRepository

    @Autowired
    private RelatorioService relatorioService

    @GetMapping(path = "/home")
    String home(Model model) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
        List<Pedido> pedidos = pedidoRepository.findByRestaurante_idOrderByDataDesc(restauranteId)
        model.addAttribute("pedidos", pedidos)
        return "restaurante-home"
    }

    @GetMapping("/edit")
    String edit(Model model) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow({ -> new NoSuchElementException() })
        model.addAttribute("restaurante", restaurante)
        ControllerHelper.setEditMode(model, true)
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model)

        return "restaurante-cadastro"
    }

    @PostMapping("/save")
    String save(
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

        ControllerHelper.setEditMode(model, true)
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model)
        return "restaurante-cadastro"
    }

    @GetMapping(path = "/comidas")
    String viewComidas(Model model) {

        Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow({ -> new NoSuchElementException() })

        model.addAttribute("restaurante", restaurante)

        List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId)
        model.addAttribute("itensCardapio", itensCardapio)

        model.addAttribute("itemCardapio", new ItemCardapio())


        return "restaurante-comidas"
    }

    @GetMapping(path = "/comidas/remover")
    String remover(@RequestParam("itemId") Integer itemId,
                   Model model) {

        itemCardapioRepository.deleteById(itemId)

        return "redirect:/restaurante/comidas"
    }

    @PostMapping(path = "/comidas/cadstrar")
    String cadastrar(
            @Valid @ModelAttribute("itemCardapio") ItemCardapio itemCardapio,
            Errors errors,
            Model model
    ) {
        if(errors.hasErrors()) {
            Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
            Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow({ -> new NoSuchElementException() })

            model.addAttribute("restaurante", restaurante)

            List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId)
            model.addAttribute("itensCardapio", itensCardapio)

            return "restaurante-comidas"
        }

        restauranteService.saveItemItemCardapio(itemCardapio)
        return "redirect:/restaurante/comidas"
    }

    @GetMapping(path = "/pedido")
    String viewPedido(
            @RequestParam("pedidoId") Integer pedidoId,
            Model model
    ) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow({ -> new NoSuchElementException() })
        model.addAttribute("pedido", pedido)

        return "restaurante-pedido"
    }

    @PostMapping(path = "/pedido/proximoStatus")
    String proximoStatus(
            @RequestParam("pedidoId") Integer pedidoId,
            Model model

    ) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow({ -> new NoSuchElementException() })

        pedido.definirProximoStatus()

        pedidoRepository.save(pedido)

        model.addAttribute("pedido", pedido)
        model.addAttribute("msg","Status alterado com sucesso")

        return "restaurante-pedido"
    }
    
    @GetMapping("/relatorio/pedidos")
    String relatorioPedidos(
            @ModelAttribute("relatorioPedidoFilter") RelatorioPedidoFilter filter,
            Model model
    ) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
        List<Pedido> pedidos = relatorioService.listPedidos(restauranteId,filter)
        model.addAttribute("pedidos", pedidos)

        model.addAttribute("filter", filter)
        return "restaurante-relatorio-pedidos"
    }

    @GetMapping("/relatorio/itens")
    String relatorioItens(
            @ModelAttribute("relatorioItemFilter") RelatorioItemFilter filter,
            Model model
    ) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId()
        List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId)
        model.addAttribute("itensCardapio", itensCardapio)
        List<RelatorioItemFaturamento> itensCalculados = relatorioService.calcularFaturamentoItens(restauranteId, filter)

        model.addAttribute("itensCalculados", itensCalculados)

        model.addAttribute("relatorioItemFilter", filter)
        return "restaurante-relatorio-itens"
    }
}
