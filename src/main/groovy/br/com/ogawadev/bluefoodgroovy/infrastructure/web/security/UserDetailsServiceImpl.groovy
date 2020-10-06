package br.com.ogawadev.bluefoodgroovy.infrastructure.web.security

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.cliente.ClienteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.usuario.Usuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private ClienteRepository clienteRepository

    @Autowired
    private RestauranteRepository restauranteRepository

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario  = clienteRepository.findByEmail(username)

        if(usuario == null) {
            usuario = restauranteRepository.findByEmail(username)

            if(usuario == null) {
                throw new UsernameNotFoundException(username)
            }
        }

        return new LoggedUser(usuario)
    }
}
