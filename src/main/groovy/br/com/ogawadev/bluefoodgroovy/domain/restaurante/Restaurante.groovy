package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import br.com.ogawadev.bluefoodgroovy.domain.usuario.Usuario
import br.com.ogawadev.bluefoodgroovy.infrastructure.web.validator.UploadConstraint
import br.com.ogawadev.bluefoodgroovy.util.FileType
import br.com.ogawadev.bluefoodgroovy.util.StringUtils
import lombok.EqualsAndHashCode
import lombok.ToString
import org.springframework.web.multipart.MultipartFile

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "restaurante")
class Restaurante extends Usuario{


    @NotBlank(message = "O CPF não pode ser vazio")
    @Pattern(regexp = "[0-9]{14}", message = "O CNPJ possui  formato inválido")
    @Column(length = 14, nullable = false)
    String cnpj

    @Size(max = 80)
    String logotipo

    @UploadConstraint(acceptedTypes = [ FileType.PNG, FileType.JPG ], message = "O arquivo não é um arquivo de imagem válido")
    transient MultipartFile logotipoFile

    @NotNull(message = "A taxa de entrega não pode ser vazia")
    @Min(0l)
    @Max(99l)
    BigDecimal taxaEntrega

    @NotNull(message = "O tempo de entrega não pode ser vazio")
    @Min(0l)
    @Max(120l)
    Integer tempoEntregaBase

    // Lado dono do relacionamento NXN
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "restaurante_has_categoria",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_restaurante_id")
    )
    @Size(min = 1, message = "O restaurante precisa ter pelo menos uma categoria")
    @ToString.Exclude
    Set<CategoriaRestaurante> categorias = new HashSet<>(0)

    @OneToMany(mappedBy = "restaurante")
    private Set<ItemCardapio> itensCardapio = new HashSet<>(0)

    void setLogotipoFileName() {
        if(getId() == null) {
            throw new IllegalStateException("É preciso primeiro gravar o registro")
        }

        this.logotipo = String.format("%04d-logo.%s", id, FileType.of(logotipoFile.getContentType()).getExtension())
    }

    String getCategoriasAsText() {

        Set<String> strings = new LinkedHashSet<>()
        for(CategoriaRestaurante categoria : categorias) {
            strings.add(categoria.getNome())
        }

        return StringUtils.concatenate(strings)
    }

    Integer calcularTempoEntrega(String cep) {
        int soma = 0

        for(char c : cep.toCharArray()) {
            int v = Character.getNumericValue(c)

            if(v > 0) {
                soma += v
            }
        }

        soma /= 2

        return tempoEntregaBase + soma
    }
}
