package br.com.ogawadev.bluefoodgroovy.infrastructure.web.security

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.domain.usuario.Usuario
import br.com.ogawadev.bluefoodgroovy.util.CollectionUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LoggedUser implements UserDetails{

    Usuario usuario
    Role role
    private Collection<? extends GrantedAuthority> roles

    LoggedUser(Usuario usuario) {
        this.usuario = usuario

        Role role

        if(usuario instanceof Cliente) {
            role = Role.CLIENTE
        } else if(usuario instanceof  Restaurante) {
            role = Role.RESTAURANTE
        } else {
            throw new IllegalStateException("O tipo de usuário não é válido")
        }

        this.role = role
        this.roles = CollectionUtils.listOf(new SimpleGrantedAuthority("ROLE_" + role))


    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
    }

    @Override
    String getPassword() {
        return usuario.senha
    }

    @Override
    String getUsername() {
        return usuario.email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
