package br.com.ogawadev.bluefoodgroovy.application.service

import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import br.com.ogawadev.bluefoodgroovy.domain.pedido.PedidoRepository
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioItemFaturamento
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioItemFilter
import br.com.ogawadev.bluefoodgroovy.domain.pedido.RelatorioPedidoFilter
import br.com.ogawadev.bluefoodgroovy.util.CollectionUtils
import br.com.ogawadev.bluefoodgroovy.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository

    List<Pedido> listPedidos(Integer restauranteId, RelatorioPedidoFilter relatorioPedidoFilter) {
        Integer pedidoId = relatorioPedidoFilter.pedidoId

        if(pedidoId != null) {
            Pedido pedido = pedidoRepository.findByIdAndRestaurante_id(pedidoId, restauranteId)
            return CollectionUtils.listOf(pedido)
        }

        LocalDate dataInicial = relatorioPedidoFilter.dataInicial
        LocalDate dataFinal = relatorioPedidoFilter.dataFinal

        if(dataInicial == null) {

            return CollectionUtils.listOf()
        }

        if(dataFinal == null) {
            dataFinal = LocalDate.now()
        }

        return pedidoRepository.findByDateInterval(restauranteId, dataInicial.atStartOfDay(), dataFinal.atTime(23, 59, 59))
    }

    List<RelatorioItemFaturamento> calcularFaturamentoItens(Integer restauranteId, RelatorioItemFilter relatorioItemFilter) {

        List<Object[]> itensObj

        Integer itemId = relatorioItemFilter.itemId
        LocalDate dataInicial = relatorioItemFilter.dataInicial
        LocalDate dataFinal = relatorioItemFilter.dataFinal

        if(dataInicial == null) {

            return CollectionUtils.listOf()
        }

        if(dataFinal == null) {
            dataFinal = LocalDate.now()
        }

        LocalDateTime dataHoraInicial = dataInicial.atStartOfDay()
        LocalDateTime dataHoraFinal = dataFinal.atTime(23,59,59)

        if(itemId != 0) {
            itensObj = pedidoRepository.findItensForFaturamento(restauranteId, itemId, dataHoraInicial, dataHoraFinal)
        } else {
            itensObj = pedidoRepository.findItensForFaturamento(restauranteId, dataHoraInicial, dataHoraFinal)
        }

        List<RelatorioItemFaturamento> itens  = new ArrayList<>()

        for(Object[] item : itensObj) {
            String nome  = (String) item[0]
            Long quantidade = (Long) item[1]
            BigDecimal valor = (BigDecimal) item[2]
            itens.add(new RelatorioItemFaturamento(nome, quantidade, valor))

        }

        return itens
    }
}
