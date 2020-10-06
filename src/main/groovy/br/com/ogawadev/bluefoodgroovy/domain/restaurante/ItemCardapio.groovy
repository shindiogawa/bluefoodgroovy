package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import br.com.ogawadev.bluefoodgroovy.infrastructure.web.validator.UploadConstraint
import br.com.ogawadev.bluefoodgroovy.util.FileType
import lombok.EqualsAndHashCode
import org.springframework.web.multipart.MultipartFile

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class ItemCardapio implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @NotBlank(message = "O nome não pode ser vazio")
    @Size(max = 50)
    String nome

    @NotBlank(message = "A categoria não pode ser vazia")
    @Size(max = 25)
    String categoria

    @NotBlank(message = "A descrição não pode ser vazia")
    @Size(max = 80)
    String descricao

    @Size(max = 50)
    String imagem

    @NotNull(message = "O preço não pode ser vazio")
    @Min(0l)
    BigDecimal preco

    @NotNull
    Boolean destaque

    @NotNull
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    Restaurante restaurante

    @UploadConstraint(acceptedTypes = FileType.PNG, message = "O arquivo não é um arquivo de imagem válido")
    transient MultipartFile imagemFile

    void setImagemFileName() {
        if(id == null) {
            throw new IllegalStateException("O objeto precisa primeiro ser criado")
        }

        this.imagem = String.format("%04d-comida.%s",id,FileType.of(imagemFile.getContentType()).getExtension())
    }
}
