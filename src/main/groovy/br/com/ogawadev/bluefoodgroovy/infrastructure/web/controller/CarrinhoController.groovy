package br.com.ogawadev.bluefoodgroovy.infrastructure.web.controller

import br.com.ogawadev.bluefoodgroovy.application.service.ImageService
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Carrinho
import br.com.ogawadev.bluefoodgroovy.domain.pedido.ItemPedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RestauranteDiferenteException
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus

@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
class CarrinhoController {

    @Autowired
    private ItemCardapioRepository itemCardapioRepository

    @Autowired
    private PedidoRepository pedidoRepository

    @ModelAttribute("carrinho")
    Carrinho carrinho() {
        return new Carrinho()
    }

    @GetMapping(path = "/visualizar")
    String viewCarrinho() {
        return "cliente-carrinho"
    }

   @GetMapping(path = "/adicionar")
    String adicionarItem(
           @RequestParam("itemId") Integer itemId,
           @RequestParam("quantidade") Integer quantidade,
           @RequestParam("observacoes") String observacoes,
           @ModelAttribute("carrinho") Carrinho carrinho,
           Model model
   ) {

        ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow({ -> new NoSuchElementException() })

       try{
           carrinho.adicionarItem(itemCardapio, quantidade, observacoes)
       } catch(RestauranteDiferenteException e) {
           model.addAttribute("msg", "Não é possível misturar comidas de restaurantes diferentes")
       }

       return "cliente-carrinho"
   }


    @GetMapping(path = "/refazerCarrinho")
    String refazerCarrinho(
        @RequestParam("pedidoId") Integer pedidoId,
        @ModelAttribute("carrinho") Carrinho carrinho,
        Model model ) {


        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow({ -> new NoSuchElementException() })

        carrinho.limpar()

        for(ItemPedido itemPedido : pedido.itens) {
            carrinho.adicionarItem(itemPedido)
        }

        return "cliente-carrinho"
    }

    @GetMapping(path = "/remover")
    String removerItem(
            @RequestParam("itemId") Integer itemId,
            @ModelAttribute("carrinho") Carrinho carrinho,
            SessionStatus sessionStatus,
            Model model
    ) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow({ -> new NoSuchElementException() })
        carrinho.removerItem(itemCardapio)

        if(carrinho.vazio()) {
            sessionStatus.setComplete()
        }

        return "cliente-carrinho"
    }
}
