package br.com.ogawadev.bluefoodgroovy.domain.pagamento

import br.com.ogawadev.bluefoodgroovy.domain.pedido.Pedido
import groovy.transform.Canonical
import lombok.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.LocalDateTime

@Canonical
@Entity
@Table(name = "pagamento")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class Pagamento implements Serializable{

    @Id
    Integer id

    @NotNull
    @OneToOne
    @MapsId // Chave primária e estrangeira ao mesmo tempo
    Pedido pedido

    @NotNull
    LocalDateTime data

    @Column(name = "num_cartao_final")
    @NotNull
    @Size(min = 4, max = 4)
    String numCartaoFinal

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    BandeiraCartao bandeiraCartao

    void definirNumeroEBandeira(String numCartao) {
        numCartaoFinal = numCartao.substring(12)
        bandeiraCartao = getBandeira(numCartao)
    }

    private BandeiraCartao getBandeira(String numCartao) {

        if(numCartao.startsWith("0000")) {
            return BandeiraCartao.AMEX
        }

        if(numCartao.startsWith("1111")) {
            return BandeiraCartao.ELO
        }

        if(numCartao.startsWith("2222")) {
            return BandeiraCartao.MASTER
        }

        return BandeiraCartao.VISA
    }

}
