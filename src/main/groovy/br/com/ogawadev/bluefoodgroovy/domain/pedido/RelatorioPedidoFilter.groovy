package br.com.ogawadev.bluefoodgroovy.domain.pedido

import groovy.transform.Canonical
import org.springframework.format.annotation.DateTimeFormat

import java.time.LocalDate

@Canonical
class RelatorioPedidoFilter {

    Integer pedidoId

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dataInicial

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dataFinal



}
