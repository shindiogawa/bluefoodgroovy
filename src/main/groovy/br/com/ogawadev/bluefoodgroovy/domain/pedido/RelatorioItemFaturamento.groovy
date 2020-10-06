package br.com.ogawadev.bluefoodgroovy.domain.pedido

import groovy.transform.Canonical
import org.springframework.format.annotation.DateTimeFormat

import java.time.LocalDate

@Canonical
class RelatorioItemFaturamento {

    String nome
    Long quantidade
    BigDecimal valor



}
