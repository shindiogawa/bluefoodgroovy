package br.com.ogawadev.bluefoodgroovy.domain.pagamento

import javax.validation.constraints.Pattern


class DadosCartao {

    @Pattern(regexp = "\\d{16}", message = "O número do cartão é inválido")
    String numCartao


}
