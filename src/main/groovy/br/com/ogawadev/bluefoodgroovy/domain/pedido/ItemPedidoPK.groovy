package br.com.ogawadev.bluefoodgroovy.domain.pedido

import groovy.transform.Canonical
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor

import javax.persistence.Embeddable
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Canonical
@Embeddable
class ItemPedidoPK implements Serializable{

    @NotNull
    @ManyToOne
    Pedido pedido

    @NotNull
    Integer ordem

}
