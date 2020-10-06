package br.com.ogawadev.bluefoodgroovy.domain.pedido

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.pagamento.Pagamento
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import lombok.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import java.time.LocalDateTime

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pedido")
class Pedido implements Serializable{

    enum Status {
        Producao(1, "Em produção", false),
        Entrega(2, "Saiu para Entrega", false),
        Concluido(3, "Concluído", true);

        Status(int ordem, String descricao, boolean ultimo) {

            this.ordem = ordem
            this.descricao = descricao
            this.ultimo = ultimo
        }

        int ordem
        String descricao
        boolean ultimo

        static Status fromOrdem(int ordem) {
            for(Status status : Status.values()) {
                if(status.ordem == ordem) {
                    return status
                }
            }
            return null
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @NotNull
    LocalDateTime data

    @NotNull
    Status status

    @NotNull
    @ManyToOne
    Cliente cliente

    @NotNull
    @ManyToOne
    Restaurante restaurante

    @NotNull
    BigDecimal subtotal

    @NotNull
    @Column(name ="taxa_entrega")
    BigDecimal taxaEntrega

    @NotNull
    BigDecimal total

    @OneToMany(mappedBy = "id.pedido", fetch = FetchType.EAGER)
    Set<ItemPedido> itens

    @OneToOne(mappedBy = "pedido")
    Pagamento pagamento

    String getFormattedId() {
        return String.format("#%04d",id)
    }

    void definirProximoStatus() {
        int ordem = status.getOrdem()

        Status newStatus = Status.fromOrdem(ordem + 1)

        if(newStatus != null) {
            this.status = newStatus
        }
    }
}
