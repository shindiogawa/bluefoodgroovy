package br.com.ogawadev.bluefoodgroovy.application.service

import br.com.ogawadev.bluefoodgroovy.domain.cliente.Cliente
import br.com.ogawadev.bluefoodgroovy.domain.cliente.ClienteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapio
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.ItemCardapioRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.Restaurante
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteComparator
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.RestauranteRepository
import br.com.ogawadev.bluefoodgroovy.domain.restaurante.SearchFilter
import br.com.ogawadev.bluefoodgroovy.util.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
class RestauranteService {
    @Autowired
    private RestauranteRepository restauranteRepository

    @Autowired
    private ClienteRepository clienteRepository

    @Autowired
    private ImageService imageService

    @Autowired
    private ItemCardapioRepository itemCardapioRepository
    @Transactional
    void saveRestaurante(Restaurante restaurante) throws ValidationException {

        if(!validateEmail(restaurante.email, restaurante.id)) {
            throw new ValidationException("O e-mail está duplicado")
        }

        if(restaurante.getId() != null) {
            Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow()
            restaurante.senha = restauranteDB.senha
            restaurante.logotipo = restauranteDB.logotipo
            restauranteRepository.save(restaurante
            )
        } else {
            restaurante.encryptPassword()
            restaurante = restauranteRepository.save(restaurante)
            restaurante.setLogotipoFileName()
            imageService.uploadLogotipo(restaurante.logotipoFile, restaurante.logotipo)

        }


    }

    private boolean validateEmail(String email, Integer id) {
        Cliente cliente = clienteRepository.findByEmail(email)

        if(cliente != null) {
            return false
        }

        Restaurante restaurante = restauranteRepository.findByEmail(email)

        if(restaurante != null) {
            if(id ==  null) {
                return false
            }

            if(!restaurante.getId().equals(id)) {
                return false
            }
        }

        return true
    }

    List<Restaurante> search(SearchFilter filter) {
        List<Restaurante> restaurantes

        if(filter.searchType == SearchFilter.SearchType.Texto) {
            restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(filter.texto)
        } else if(filter.searchType == SearchFilter.SearchType.Categoria) {
            restaurantes = restauranteRepository.findByCategorias_Id(filter.categoriaId)
        } else {
            throw new IllegalStateException("O tipo de busca " + filter.getSearchType() + " não é suportado")
        }

        Iterator<Restaurante> it = restaurantes.iterator()

        while(it.hasNext()) {
            Restaurante restaurante = it.next()
            double taxaEntrega  = restaurante.taxaEntrega.doubleValue()
            if(filter.isEntregaGratis() && taxaEntrega > 0
                || !filter.isEntregaGratis() && taxaEntrega == 0) {
                it.remove()
            }
        }

        RestauranteComparator comparator = new RestauranteComparator(filter, SecurityUtils.loggedCliente().cep)
        restaurantes.sort(comparator)
        return restaurantes
    }

    @Transactional
    void saveItemItemCardapio(ItemCardapio itemCardapio) {
        itemCardapio = itemCardapioRepository.save(itemCardapio)
        itemCardapio.setImagemFileName()
        imageService.uploadComida(itemCardapio.imagemFile, itemCardapio.imagem)

    }

}
