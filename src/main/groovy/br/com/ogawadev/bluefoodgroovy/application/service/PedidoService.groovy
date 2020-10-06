package br.com.ogawadev.bluefoodgroovy.application.service

import br.com.ogawadev.bluefoodgroovy.domain.pagamento.DadosCartao
import br.com.ogawadev.bluefoodgroovy.domain.pagamento.Pagamento
import br.com.ogawadev.bluefoodgroovy.domain.pagamento.PagamentoRepository
import br.com.ogawadev.bluefoodgroovy.domain.pagamento.StatusPagamento
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Carrinho
import br.com.ogawadev.bluefoodgroovy.domain.pedido.ItemPedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.ItemPedidoPK
import br.com.ogawadev.bluefoodgroovy.domain.pedido.ItemPedidoRepository
import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import br.com.ogawadev.bluefoodgroovy.util.IOUtils
import br.com.ogawadev.bluefoodgroovy.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import java.time.LocalDateTime

@Service
class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository

    @Autowired
    private ItemPedidoRepository itemPedidoRepository

    @Autowired
    private PagamentoRepository pagamentoRepository

    @Value('${bluefood.sbpay.url}')
    private String sbPayUrl

    @Value('${bluefood.sbpay.token}')
    private String sbPayToken



    @Transactional(rollbackFor = PagamentoException.class)
    Pedido criarEPagar(Carrinho carrinho, String numCartao) throws PagamentoException{

        Pedido pedido = new Pedido()
        pedido.data = LocalDateTime.now()
        pedido.cliente = SecurityUtils.loggedCliente()
        pedido.restaurante = carrinho.restaurante
        pedido.status = Pedido.Status.Producao
        pedido.taxaEntrega = carrinho.getRestaurante().getTaxaEntrega()
        pedido.subtotal = carrinho.getPrecoTotal(false)
        pedido.total = carrinho.getPrecoTotal(true)

        pedidoRepository.save(pedido)

        int ordem = 1

        for(ItemPedido itemPedido : carrinho.itens) {
            itemPedido.id = new ItemPedidoPK(pedido, ordem++)
            itemPedidoRepository.save(itemPedido)
        }

        DadosCartao dadosCartao = new DadosCartao()
        dadosCartao.numCartao = numCartao

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>()
        headers.add("Token", sbPayToken)


        HttpEntity<DadosCartao> requestEntity = new HttpEntity<>(dadosCartao,headers )

        RestTemplate restTemplate = new RestTemplate()

        Map<String, String> response

        try {
            response = restTemplate.postForObject(sbPayUrl, requestEntity, Map.class)
        } catch(Exception e) {
            throw new PagamentoException("Erro no servidor de pagamento")
        }

        StatusPagamento statusPagamento = StatusPagamento.valueOf(response.get("statusPagamento") as String)

        if(statusPagamento != StatusPagamento.Autorizado) {
            throw new PagamentoException(statusPagamento.descricao)
        }

        Pagamento pagamento = new Pagamento()
        pagamento.data = LocalDateTime.now()
        pagamento.pedido = pedido
        pagamento.definirNumeroEBandeira(numCartao)

        pagamentoRepository.save(pagamento)

        return pedido
    }
    
}
