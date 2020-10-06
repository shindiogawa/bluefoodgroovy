package br.com.ogawadev.bluefoodgroovy.domain.pedido


import org.springframework.data.jpa.repository.JpaRepository

interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoPK> {
}
