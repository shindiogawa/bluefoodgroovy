package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {
    @Query("SELECT DISTINCT ic.categoria FROM ItemCardapio ic WHERE ic.restaurante.id = ?1 ORDER BY ic.categoria")
    List<String> findCategorias(Integer restauranteId)

    List<ItemCardapio> findByRestaurante_IdOrderByNome(Integer restauranteId)

    List<ItemCardapio> findByRestaurante_IdAndDestaqueOrderByNome(Integer restauranteId, boolean destaque)
    List<ItemCardapio> findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(Integer restauranteId, boolean destaque, String categoria)

}