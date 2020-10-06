package br.com.ogawadev.bluefoodgroovy.domain.pagamento

enum StatusPagamento {
    Autorizado("Autorizado"),
    NaoAutorizado("Não autorizado pela instituição financeira"),
    CartaoInvalido("Cartão inválido ou bloqueado")

    String descricao

    StatusPagamento(String descricao) {
        this.descricao = descricao
    }

}