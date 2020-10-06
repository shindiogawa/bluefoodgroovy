package br.com.ogawadev.bluefoodgroovy.domain.cliente

import br.com.ogawadev.bluefoodgroovy.domain.usuario.Usuario
import groovy.transform.ToString
import lombok.EqualsAndHashCode


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "cliente")
class Cliente extends Usuario{

    @NotBlank(message = "O CPF não pode ser vazio")
    @Pattern(regexp = "[0-9]{11}", message = "O CPF possui  formato inválido")
    @Column(length = 11, nullable = false)
    String cpf

    @NotBlank(message = "O CEP não pode ser vazio")
    @Pattern(regexp = "[0-9]{8}", message = "O CEP possui formato inválido")
    @Column(length = 8)
    String cep

    String getFormattedCep() {

        return cep.substring(0, 5) + "-" + cep.substring(5)
    }
}
