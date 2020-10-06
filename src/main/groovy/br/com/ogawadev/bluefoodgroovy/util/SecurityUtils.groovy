package br.com.ogawadev.bluefoodgroovy.util

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.infrastructure.web.security.LoggedUser
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtils {
    static LoggedUser loggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()

        if(authentication instanceof AnonymousAuthenticationToken) {
            return null
        }

        return authentication.getPrincipal() as LoggedUser

    }

    static Cliente loggedCliente() {
        LoggedUser loggedUser = loggedUser()
        if(loggedUser == null) {
            throw new  IllegalStateException("Não existe um usuário logado")
        }

        if(!(loggedUser.usuario instanceof Cliente)) {
            throw new IllegalStateException("O usuário logado não é um cliente")
        }

        return (Cliente) loggedUser.usuario
    }

    static Restaurante loggedRestaurante() {
        LoggedUser loggedUser = loggedUser()
        if(loggedUser == null) {
            throw new  IllegalStateException("Não existe um usuário logado")
        }

        if(!(loggedUser.usuario instanceof Restaurante)) {
            throw new IllegalStateException("O usuário logado não é um restaurante")
        }

        return (Restaurante) loggedUser.usuario
    }
}
