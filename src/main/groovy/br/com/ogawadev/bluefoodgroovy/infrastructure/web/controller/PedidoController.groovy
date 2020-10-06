package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.ImageService
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/cliente/pedido")
class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository

    @GetMapping(path = "/view")
    String viewPedido(
            @RequestParam("pedidoId") Integer pedidoId,
            Model model) {

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow({ -> new NoSuchElementException() })
        model.addAttribute("pedido", pedido)

        return "cliente-pedido"
    }

}
