package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import lombok.EqualsAndHashCode

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name ="categoria_restaurante")
class CategoriaRestaurante implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Integer id

    @NotNull
    @Size(max = 20)
    String nome

    @NotNull
    @Size(max = 50)
    String imagem

    // Se quiser relacionamento bidirecional utilizar o conjunto nessa classe também
    @ManyToMany(mappedBy = "categorias")
    Set<Restaurante> restaurantes = new HashSet<>(0)
}
