package br.com.ogawadev.bluefoodgroovy.domain.pagamento

import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
