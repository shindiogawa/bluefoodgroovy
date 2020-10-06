package br.com.ogawadev.bluefoodgroovy.domain.pedido


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import java.time.LocalDate
import java.time.LocalDateTime

interface PedidoRepository extends JpaRepository<Pedido, Integer> {

//    List<Pedido> findByCliente_Id(Integer clienteId)

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = ?1 ORDER BY p.data DESC")
    List<Pedido> listPedidosByCliente(Integer clienteId)

//    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = ?1 AND p.status = 'Em produção' ORDER BY p.data DESC")
//    List<Pedido> listPedidosByRestaurante(Integer restauranteId)

    List<Pedido> findByRestaurante_idOrderByDataDesc(Integer restauranteId)

    Pedido findByIdAndRestaurante_id(Integer pedidoId, Integer restauranteId)

    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = ?1 AND p.data BETWEEN ?2 AND ?3")
    List<Pedido> findByDateInterval(Integer restauranteId, LocalDateTime dataInicial, LocalDateTime dataFinal)

    @Query("SELECT i.itemCardapio.nome, COUNT(i.itemCardapio.id), SUM(i.preco) FROM Pedido p INNER JOIN p.itens i WHERE p.restaurante.id = ?1 AND i.itemCardapio.id = ?2 AND p.data BETWEEN ?3 AND ?4 GROUP BY i.itemCardapio.nome")
    List<Object []> findItensForFaturamento(Integer restauranteId, Integer itemCardapio, LocalDateTime dataInicial, LocalDateTime dataFinal)

    @Query("SELECT i.itemCardapio.nome, SUM(i.quantidade), SUM(i.preco * i.quantidade) FROM Pedido p INNER JOIN p.itens i WHERE p.restaurante.id = ?1 AND p.data BETWEEN ?2 AND ?3 GROUP BY i.itemCardapio.nome")
    List<Object []> findItensForFaturamento(Integer restauranteId, LocalDateTime dataInicial, LocalDateTime dataFinal)


}
