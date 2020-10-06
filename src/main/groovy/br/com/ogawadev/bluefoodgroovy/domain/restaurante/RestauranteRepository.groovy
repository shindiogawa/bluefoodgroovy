package br.com.ogawadev.bluefoodgroovy.domain.restaurante

import org.springframework.data.jpa.repository.JpaRepository

interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {

    Restaurante findByEmail(String email)

    List<Restaurante> findByNomeIgnoreCaseContaining(String nome)

    List<Restaurante> findByCategorias_Id(Integer id)

}