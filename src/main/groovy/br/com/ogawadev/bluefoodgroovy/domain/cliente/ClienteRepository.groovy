package br.com.ogawadev.bluefoodgroovy.domain.cliente

import org.springframework.data.jpa.repository.JpaRepository

interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByEmail(String email)
}
