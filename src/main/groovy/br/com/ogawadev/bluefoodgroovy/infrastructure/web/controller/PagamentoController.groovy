package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.PagamentoException
import br.com.ogawadev.bluefoodgroovy.application.service.PedidoService
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Carrinho
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus

@Controller
@RequestMapping("/cliente/pagamento")
@SessionAttributes("carrinho")
class PagamentoController {

    @Autowired
    private PedidoService pedidoService

    @PostMapping(path = "/pagar")
    String pagar(
            @RequestParam("numCartao") String numCartao,
            @ModelAttribute("carrinho") Carrinho carrinho,
            SessionStatus sessionStatus,
            Model model) {

        try {
            Pedido pedido = pedidoService.criarEPagar(carrinho, numCartao)
            sessionStatus.setComplete()
            return "redirect:/cliente/pedido/view?pedidoId=" + pedido.id

        } catch( PagamentoException e) {
            model.addAttribute("msg", e.getMessage())
            return "cliente-carrinho"
        }

    }

}
