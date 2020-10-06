package br.com.ogawadev.bluefoodgroovy.application.service

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.cliente.ClienteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository

    @Autowired
    private RestauranteRepository restauranteRepository

    @Transactional
    void saveCliente(Cliente cliente) throws ValidationException {

        if(!validateEmail(cliente.email, cliente.id)) {
            throw new ValidationException("O e-mail está duplicado")
        }

        if(cliente.getId() != null) {
            Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElseThrow({ -> new NoSuchElementException() })
            cliente.senha = clienteDB.senha
        } else {
            cliente.encryptPassword()
        }

        clienteRepository.save(cliente)
    }

    private boolean validateEmail(String email, Integer id) {
        Restaurante restaurante = restauranteRepository.findByEmail(email)

        if(restaurante != null) {

            return false
        }
        Cliente cliente = clienteRepository.findByEmail(email)

        if(cliente != null) {
            if(id ==  null) {
                return false
            }

            if(!cliente.getId().equals(id)) {
                return false
            }
        }
        return true
    }
}
