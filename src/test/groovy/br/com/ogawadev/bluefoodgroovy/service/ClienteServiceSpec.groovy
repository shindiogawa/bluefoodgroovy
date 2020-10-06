package br.com.ogawadev.bluefoodgroovy.service

import br.com.ogawadev.bluefoodgroovy.application.service.ClienteService
import br.com.ogawadev.bluefoodgroovy.application.service.ValidationException
import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.cliente.ClienteRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification


@SpringBootTest
@ActiveProfiles("test")
class ClienteServiceSpec extends Specification{

    @Autowired
    private ClienteService clienteService

    @SpringBean
    ClienteRepository clienteRepository = Mock()

    def setup() {
        assert clienteService != null
    }

    def "test when has duplicated email"(){
        given: "creating new cliente"

        Cliente c1 = new Cliente()
        Cliente c2 = new Cliente()

        and: "insert attributes to clientes"
        c1.id = 1
        c1.nome = "José"
        c1.email = "a@a.com"
        c2.email = "a@a.com"

        and: "insert fake cliente 1"
        clienteRepository.findByEmail(c1.email) >> c1

        when: "saved cliente 2"
        clienteService.saveCliente(c2)

        then: "check for exceptions"
        thrown ValidationException

    }
}
