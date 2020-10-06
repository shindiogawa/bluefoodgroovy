package br.com.ogawadev.bluefoodgroovy.domain.pedido

import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import groovy.transform.Canonical
import lombok.EqualsAndHashCode
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Canonical
@Entity
@Table(name = "item_pedido")
class ItemPedido implements Serializable{

    @EmbeddedId
    @EqualsAndHashCode.Include
    ItemPedidoPK id

    @NotNull
    @ManyToOne
    ItemCardapio itemCardapio

    @NotNull
    Integer quantidade

    @Size(max = 50)
    String observacoes

    @NotNull
    BigDecimal preco

    BigDecimal getPrecoCalculado() {
        preco.multiply(BigDecimal.valueOf(quantidade))
    }
}
