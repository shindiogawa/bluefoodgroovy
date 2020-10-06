package br.com.ogawadev.bluefoodgroovy.domain.usuario

import br.com.ogawadev.bluefoodgroovy.util.StringUtils
import lombok.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
class Usuario implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Integer id

    @NotBlank(message = "O nome nao pode ser vazio")
    @Size(max = 80, message =  "O nome é muito grande")
    String nome

    @NotBlank(message = "O e-mail nao pode ser vazio")
    @Size(max = 60, message =  "O e-mail é muito grande")
    @Email(message = " O e-mail não é valido")
    String email

    @NotBlank(message = "A senha nao pode ser vazio")
    @Size(max = 80, message =  "A senha é muito grande")
    String senha

    @NotBlank(message = "O telefone nao pode ser vazio")
    @Pattern(regexp = "[0-9]{11}", message = "O telefone possui formato inválido")
    @Column(length = 11, nullable = false)
    String telefone

    void encryptPassword() {
        this.senha = StringUtils.encrypt(this.senha)
    }
}
